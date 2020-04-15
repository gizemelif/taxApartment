package com.tax.verify.dao;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tax.verify.model.Data;
import com.tax.verify.mailSender.EmailSender;
import com.tax.verify.service.JsonObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import static com.tax.verify.mailSender.EmailSender.gmail_config;

@Service
public class GetHttpResponse {
    private JsonObjectMapper jsonObjectMapper;
    private static EmailSender mailer;

    @Autowired
    public GetHttpResponse(JsonObjectMapper jsonObjectMapper){
        this.jsonObjectMapper = jsonObjectMapper;
    }

    static {
        try {
            mailer = new EmailSender(gmail_config, ImmutablePair.of("errorverifyvkn@gmail.com", "gvgGroup!!*"));
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    public List<Data> getResponseNullPlate(List<Data> newList) throws MessagingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<Data> myDatas = new ArrayList<>();
        Integer satirlarSize = 0;
        String governmentNum = "";
        String plate = "";
        String responseString = "";

        JSONObject data1 = new JSONObject();
        JSONObject data2 = new JSONObject();
        JSONObject taxResult = new JSONObject();
        JSONObject resultData = new JSONObject();

        JSONObject tempData1 = new JSONObject();
        JSONObject tempData2 = new JSONObject();
        JSONObject tempTaxResult = new JSONObject();
        JSONObject tempResultData = new JSONObject();

        for (int i = 0; i < newList.size(); i++) {
            Data myData = new Data();

            governmentNum = newList.get(i).getTckn();
            governmentNum = governmentNum.replace(" ", "");
            plate = "";
            String stuation = "TERK";
            String firstFoundedPlate = "";

            AtomicReference<Boolean> isFound = new AtomicReference<>(false);

            for (int j = 1; j < 82; j++) {
                plate = String.valueOf(j);
                try {
                    HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?tc=" + governmentNum.trim() + "&plate=" + plate + "&detail=1")
                            .header("accept", "application/json")
                            .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .header("Connection", "keep-alive")
                            .socketTimeout(120000)
                            .asJson();

                    responseString = jsonResponse.getBody().toString();

                    data1 = new JSONObject(responseString);
                    data2 = (JSONObject) data1.get("data");
                    taxResult = (JSONObject) data2.get("TaxDetailResult");
                    resultData = (JSONObject) taxResult.get("data");

                    if(data2.get("durum_text").toString().equals(stuation)){
                        firstFoundedPlate = plate;
                        tempData1 = data1;
                        tempData2 = data2;
                        tempTaxResult = taxResult;
                        tempResultData = resultData;
                        continue;
                    }else if( data2.get("durum_text").toString().length() == 0 && j == 81){
                        data1 = tempData1;
                        data2 = tempData2;
                        taxResult = tempTaxResult;
                        resultData = tempResultData;
                        plate = firstFoundedPlate;

                        break;
                    }
                    if (data2.get("vdkodu").toString().length() != 0 || data2.get("unvan").toString().length() != 0) {
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    mailer.sendEmail("gizemelif.atalay@gvg.com.tr", "HTTP Response hatası", e.toString());

                    System.out.println(e.toString());
                }

            }
            if (!resultData.get("satirlarSize").equals(satirlarSize)) {
                JSONArray satirlar = resultData.getJSONArray("satirlar");
                for (Object o : satirlar) {
                    JSONObject jsonLineItem = (JSONObject) o;
                    myData.setMatrah(jsonLineItem.get("matrah").toString());
                    myData.setTahakkukeden(jsonLineItem.get("tahakkukeden").toString());
                    myData.setYil(jsonLineItem.get("yil").toString());
                    break;
                }
            }else{
                myData.setMatrah("N/A");
                myData.setTahakkukeden("N/A");
                myData.setYil("N/A");
            }
            if (data2.toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0 || data2.get("durum_text") == null ) {

                if (!isFound.get()) {
                    myData.setDurum_text("N/A");
                    myData.setTckn(governmentNum);
                    myData.setUnvan("N/A");
                    myData.setVdkodu("N/A");
                    myData.setVkn("N/A");
                    myData.setOid(newList.get(i).getOid());
                    myData.setPlaka("N/A");
                    myData.setTc_tum_il_na((long) 1);
                    myData.setIsebaslamatarihi("N/A");
                    myData.setNacekoduaciklama("N/A");
                    myData.setTc_adres_donen("N/A");


                    myDatas.add(myData);

                    continue;
                }
            }

            isFound.set(true);
            myData.setOid(newList.get(i).getOid());
            myData.setTckn(governmentNum);
            myData.setPlaka(plate);
            myData.setVdkodu(data2.get("vdkodu").toString());
            myData.setUnvan(data2.get("unvan").toString());
            myData.setVkn(data2.get("vkn").toString());
            myData.setDurum_text((String) data2.get("durum_text"));
            if(resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0){
                myData.setTc_adres_donen("N/A");
                myData.setIsebaslamatarihi("N/A");
                myData.setNacekoduaciklama("N/A");
            }else{
                myData.setTc_adres_donen(resultData.get("adres").toString());
                myData.setIsebaslamatarihi(resultData.get("isebaslamatarihi").toString());
                myData.setNacekoduaciklama(resultData.get("nacekoduaciklama").toString());
            }
            myDatas.add(myData);
        }
        return myDatas;
    }

    public List<Data> getResponseVknNullPlate(List<Data> newList) throws MessagingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<Data> myDatas = new ArrayList<>();
        Integer satirlarSize = 0;
        JSONObject data1 = new JSONObject();
        JSONObject data2 = new JSONObject();
        JSONObject taxResult = new JSONObject();
        JSONObject resultData = new JSONObject();

        JSONObject tempData1 = new JSONObject();
        JSONObject tempData2 = new JSONObject();
        JSONObject tempTaxResult = new JSONObject();
        JSONObject tempResultData = new JSONObject();



        for (int i = 0; i < newList.size(); i++) {
            Data myData = new Data();

            String taxNumber = newList.get(i).getVd_vkn();
            taxNumber = taxNumber.replace(" ", "");
            String plate = "";
            String responseString = "";
            String stuation = "TERK";
            String firstFoundedPlate = "";

            AtomicReference<Boolean> isFound = new AtomicReference<>(false);

            for (int j = 1; j < 82; j++) {
                plate = String.valueOf(j);

                try {
                    HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?vkn=" + taxNumber.trim() + "&plate=" + plate + "&detail=1")
                            .header("accept", "application/json")
                            .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .header("Connection", "keep-alive")
                            .socketTimeout(120000)
                            .asJson();

                    responseString = jsonResponse.getBody().toString();

                    data1 = new JSONObject(responseString);
                    data2 = (JSONObject) data1.get("data");
                    taxResult = (JSONObject) data2.get("TaxDetailResult");
                    resultData = (JSONObject) taxResult.get("data");

                    //eğer terk döndüğünde tüm illeri sorgulamamışsa temp değerleri gerçek değerlere assign et ve döngüden çık
                    //böylece tüm iller sorgulandıktan sonra terk ten farklı bir sonuç dönmezse ilk dönen terk bilgilerini kaybetme

                    if(data2.get("durum_text").toString().equals(stuation)){
                        firstFoundedPlate = plate;
                        tempData1 = data1;
                        tempData2 = data2;
                        tempTaxResult = taxResult;
                        tempResultData = resultData;
                        continue;
                    }else if( data2.get("durum_text").toString().length() == 0 && j == 81){
                        data1 = tempData1;
                        data2 = tempData2;
                        taxResult = tempTaxResult;
                        resultData = tempResultData;
                        plate = firstFoundedPlate;

                        break;
                    }
                    if (data2.get("vdkodu").toString().length() != 0 || data2.get("unvan").toString().length() != 0){
                        break;
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.toString());
                    mailer.sendEmail("gizemelif.atalay@gvg.com.tr", "HTTP Response hatası", e.toString());
                }
            }
            if (!resultData.get("satirlarSize").equals(satirlarSize)) {
                JSONArray satirlar = resultData.getJSONArray("satirlar");
                for (Object o : satirlar) {
                    JSONObject jsonLineItem = (JSONObject) o;
                    myData.setMatrah_vd(jsonLineItem.get("matrah").toString());
                    myData.setTahakkukeden_vd(jsonLineItem.get("tahakkukeden").toString());
                    myData.setYil_vd(jsonLineItem.get("yil").toString());
                    break;
                }
            }else{
                myData.setMatrah_vd("N/A");
                myData.setTahakkukeden_vd("N/A");
                myData.setYil_vd("N/A");
            }

            if (data2.toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0) {

                if (!isFound.get()) {
                    myData.setVd_vkn(taxNumber);
                    myData.setVd_unvan_donen("N/A");
                    myData.setVd_tc_donen("N/A");
                    myData.setVd_fiili_durum_donen("N/A");
                    myData.setVd_vdkodu("N/A");
                    myData.setOid(newList.get(i).getOid());
                    myData.setPlaka("N/A");
                    myData.setVd_tum_il_na((long) 1);
                    myData.setIsebaslamatarihi_vd("N/A");
                    myData.setNacekoduaciklama_vd("N/A");
                    myData.setVd_adres_donen("N/A");

                    myDatas.add(myData);
                    continue;
                }
            }
            isFound.set(true);
            myData.setOid(newList.get(i).getOid());
            myData.setVd_tc_donen(data2.get("tckn").toString());
            myData.setVd_vkn(newList.get(i).getVd_vkn().trim());
            myData.setVd_fiili_durum_donen(data2.get("durum_text").toString());
            myData.setVd_unvan_donen(data2.get("unvan").toString());
            myData.setVd_vdkodu(data2.get("vdkodu").toString());
            myData.setPlaka(plate);

            if(resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0 ){
                myData.setVd_adres_donen("N/A");
                myData.setIsebaslamatarihi_vd("N/A");
                myData.setNacekoduaciklama_vd("N/A");
            }else{
                myData.setVd_adres_donen(resultData.get("adres").toString());
                myData.setIsebaslamatarihi_vd(resultData.get("isebaslamatarihi").toString());
                myData.setNacekoduaciklama_vd(resultData.get("nacekoduaciklama").toString());
            }
            myDatas.add(myData);
        }
        return myDatas;
    }

        public List<Data> getResponseVkn (List <Data> newList) throws MessagingException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            List<Data> myDatas = new ArrayList<>();
            Integer satirlarSize = 0;

            for (int i = 0; i < newList.size(); i++) {
                Data myData = new Data();
                try {
                    String taxNumber = newList.get(i).getVd_vkn();
                    taxNumber = taxNumber.replace(" ", "");
                    String plate = "";
                    String responseString = "";
                    AtomicReference<Boolean> isFound = new AtomicReference<>(false);

                    HttpResponse jsonResponse = jsonObjectMapper.httpGet("vkn=",taxNumber.trim(), newList.get(i).getPlaka());
                    responseString = jsonResponse.getBody().toString();

                    JSONObject data1 = new JSONObject(responseString);
                    JSONObject data2 = (JSONObject) data1.get("data");
                    JSONObject taxResult = (JSONObject) data2.get("TaxDetailResult");
                    JSONObject resultData = (JSONObject) taxResult.get("data");
                    if (!resultData.get("satirlarSize").equals(satirlarSize)) {
                        JSONArray satirlar = resultData.getJSONArray("satirlar");
                        for (Object o : satirlar) {
                            JSONObject jsonLineItem = (JSONObject) o;
                            myData.setMatrah_vd(jsonLineItem.get("matrah").toString());
                            myData.setTahakkukeden_vd(jsonLineItem.get("tahakkukeden").toString());
                            myData.setYil_vd(jsonLineItem.get("yil").toString());
                            break;
                        }
                    }else{
                        myData.setMatrah_vd("N/A");
                        myData.setTahakkukeden_vd("N/A");
                        myData.setYil_vd("N/A");
                    }

                    if (data2.toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0) {
                        if (!isFound.get()) {
                            myData.setVd_fiili_durum_donen("N/A");
                            myData.setVd_vkn(taxNumber);
                            myData.setVd_unvan_donen("N/A");
                            myData.setVd_vdkodu("N/A");
                            myData.setVd_tc_donen("N/A");
                            myData.setOid(newList.get(i).getOid());
                            myData.setPlaka("N/A");
                            myData.setNacekoduaciklama_vd("N/A");
                            myData.setVd_adres_donen("N/A");

                            myDatas.add(myData);
                            continue;
                        }

                    }

                    isFound.set(true);

                    myData.setOid(newList.get(i).getOid());
                    myData.setVd_vkn(taxNumber);
                    myData.setPlaka(newList.get(i).getPlaka());
                    myData.setVd_vdkodu(data2.get("vdkodu").toString());
                    myData.setVd_unvan_donen(data2.get("unvan").toString());
                    myData.setVd_tc_donen(data2.get("tckn").toString());
                    myData.setVd_fiili_durum_donen((String) data2.get("durum_text"));
                    if(resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0 ){
                        myData.setVd_adres_donen("N/A");
                        myData.setIsebaslamatarihi_vd("N/A");
                        myData.setNacekoduaciklama_vd("N/A");
                    }else{
                        myData.setVd_adres_donen(resultData.get("adres").toString());
                        myData.setIsebaslamatarihi_vd(resultData.get("isebaslamatarihi").toString());
                        myData.setNacekoduaciklama_vd(resultData.get("nacekoduaciklama").toString());
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                    System.out.println(e.toString());
                    mailer.sendEmail("gizemelif.atalay@gvg.com.tr", "HTTP Response hatası", e.toString());
                }
                myDatas.add(myData);
            }
            return myDatas;
        }

        public List<Data> getResponse (List < Data > newList) throws MessagingException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            List<Data> myDatas = new ArrayList<>();
            Integer satirlarSize = 0;

            for (int i = 0; i < newList.size(); i++) {
                Data myData = new Data();
                try {
                    String governmentNum = newList.get(i).getTckn();
                    governmentNum = governmentNum.replace(" ", "");

                    AtomicReference<Boolean> isFound = new AtomicReference<>(false);

                    HttpResponse jsonResponse = jsonObjectMapper.httpGet("tc=",governmentNum.trim(), newList.get(i).getPlaka());

                    String responseString = jsonResponse.getBody().toString();

                    JSONObject data1 = new JSONObject(responseString);
                    JSONObject data2 = (JSONObject) data1.get("data");
                    JSONObject taxResult = (JSONObject) data2.get("TaxDetailResult");
                    JSONObject resultData = (JSONObject) taxResult.get("data");
                    if (!resultData.get("satirlarSize").equals(satirlarSize)) {
                        JSONArray satirlar = resultData.getJSONArray("satirlar");
                        for (Object o : satirlar) {
                            JSONObject jsonLineItem = (JSONObject) o;
                            myData.setMatrah(jsonLineItem.get("matrah").toString());
                            myData.setTahakkukeden(jsonLineItem.get("tahakkukeden").toString());
                            myData.setYil(jsonLineItem.get("yil").toString());
                            break;
                        }
                    }else{
                        myData.setMatrah("N/A");
                        myData.setTahakkukeden("N/A");
                        myData.setYil("N/A");
                    }

                    if (data2.toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0) {
                        if (!isFound.get()) {
                            myData.setDurum_text("N/A");
                            myData.setTckn(governmentNum);
                            myData.setUnvan("N/A");
                            myData.setVdkodu("N/A");
                            myData.setVkn("N/A");
                            myData.setOid(newList.get(i).getOid());
                            myData.setPlaka("N/A");
                            myData.setIsebaslamatarihi("N/A");
                            myData.setNacekoduaciklama("N/A");
                            myData.setTc_adres_donen("N/A");

                            myDatas.add(myData);
                            continue;
                        }

                    }

                    isFound.set(true);

                    myData.setOid(newList.get(i).getOid());
                    myData.setTckn(governmentNum);
                    myData.setPlaka(newList.get(i).getPlaka());
                    myData.setVdkodu(data2.get("vdkodu").toString());
                    myData.setUnvan(data2.get("unvan").toString());
                    myData.setVkn(data2.get("vkn").toString());
                    myData.setDurum_text((String) data2.get("durum_text"));

                    if(resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0 ){
                        myData.setTc_adres_donen("N/A");
                        myData.setIsebaslamatarihi("N/A");
                        myData.setNacekoduaciklama("N/A");
                    }else{
                        myData.setTc_adres_donen(resultData.get("adres").toString());
                        myData.setIsebaslamatarihi(resultData.get("isebaslamatarihi").toString());
                        myData.setNacekoduaciklama(resultData.get("nacekoduaciklama").toString());
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                    mailer.sendEmail("gizemelif.atalay@gvg.com.tr", "HTTP Response hatası", e.toString());

                    System.out.println(e.toString());

                }

                myDatas.add(myData);
            }
            return myDatas;
        }
}
