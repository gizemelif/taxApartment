package com.tax.verify.jpa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tax.verify.dto.Data;
import com.tax.verify.dto.TaxDetailResult;
import com.tax.verify.dto.VD;
import com.tax.verify.mailSender.EmailSender;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.DataInput;
import java.security.Key;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import static com.tax.verify.mailSender.EmailSender.gmail_config;

@Service
public class GetHttpResponse {
    private static EmailSender mailer;

    static {
        try {
            mailer = new EmailSender(gmail_config, ImmutablePair.of("errorverifyvkn@gmail.com","gvgGroup!!*"));
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    public List<Data> getResponseNullPlate(List<Data> newList) throws MessagingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<Data> myDatas = new ArrayList<>();
        VD vd = new VD();

        for (int i = 0; i < newList.size(); i++) {
            Data myData = new Data();

                String governmentNum = newList.get(i).getTckn();
                governmentNum = governmentNum.replace(" ","");
                String plate = "";
                String responseString = "";
                String vd_vdkodu = "";

                AtomicReference<Boolean> isFound = new AtomicReference<>(false);

                    for(int j = 1; j < 82; j++) {
                        plate = String.valueOf(j);
                            try {
                                HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?tc=" + governmentNum.trim() + "&plate=" + plate + "&detail=1" )
                                        .header("accept", "application/json")
                                        .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                                        .header("Connection", "keep-alive")
                                        .socketTimeout(120000)
                                        .asJson();

                                responseString = jsonResponse.getBody().toString();

                                vd = mapper.readValue(responseString, new TypeReference<VD>() {
                                });
                            } catch (Exception e) {
                                e.printStackTrace();

                                mailer.sendEmail("gizemelif.atalay@gvg.com.tr", "HTTP Response hatası", e.toString());

                                System.out.println(e.toString());
                            }
                        if(vd.getData().getVdkodu().length() !=0){
                            break;
                        }
                    }
                    if( vd.getData().getTckn().length() == 0 || vd == null || vd.getData().getTckn() == null || vd.getData().getVdkodu() == null || vd.getData().getVdkodu().length() == 0 || vd.getData().getDurum_text() == null || vd.getData().getDurum_text().length() == 0 ) {

                        if (!isFound.get()) {
                            vd = new VD();
                            myData.setDurum_text("N/A");
                            myData.setTckn(governmentNum);
                            myData.setUnvan("N/A");
                            myData.setVdkodu("N/A");
                            myData.setVkn("N/A");
                            myData.setOid(newList.get(i).getOid());
                            myData.setPlaka("N/A");
                            myData.setTc_tum_il_na((long) 1);
                            vd.setData(myData);

                            myDatas.add(myData);

                            continue;
                        }
                    }

                isFound.set(true);
                vd.getData().setOid(newList.get(i).getOid());
                vd.getData().setVkn(vd.getData().getVkn());
                vd.getData().setTckn(governmentNum);
                vd.getData().setUnvan(vd.getData().getUnvan());
                vd.getData().setVdkodu(vd.getData().getVdkodu());
                vd.getData().setDurum_text(vd.getData().getDurum_text());
                vd.getData().setPlaka(plate);

                myData = vd.getData();


            myDatas.add(myData);
        }
        return myDatas;
    }
    public List<Data> getResponseVknNullPlate(List<Data> newList) throws MessagingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<Data> myDatas = new ArrayList<>();
        VD vd = new VD();

        for(int i = 0; i < newList.size(); i++){
            Data myData = new Data();

                String taxNumber = newList.get(i).getVd_vkn();
                taxNumber = taxNumber.replace(" ","");
                String plate = "";
                String responseString = "";

                AtomicReference<Boolean> isFound = new AtomicReference<>(false);

                    for(int j = 1; j < 82; j++) {
                        plate = String.valueOf(j);

                        try{
                            HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?vkn=" + taxNumber.trim() + "&plate=" + plate + "&detail=1")
                                    .header("accept", "application/json")
                                    .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                                    .header("Connection", "keep-alive")
                                    .socketTimeout(120000)
                                    .asJson();

                            responseString = jsonResponse.getBody().toString();
                            vd = mapper.readValue(responseString, new TypeReference<VD>() {
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println(e.toString());
                            mailer.sendEmail("gizemelif.atalay@gvg.com.tr", "HTTP Response hatası", e.toString());
                        }

                        if(vd.getData().getVdkodu().length() !=0){
                            break;
                        }
                    }
                    if( vd.getData().getVkn().length() == 0 || vd == null || vd.getData().getVkn() == null || vd.getData().getVdkodu() == null || vd.getData().getVdkodu().length() == 0 || vd.getData().getDurum_text() == null || vd.getData().getDurum_text().length() == 0 ){

                        if(!isFound.get()){
                            vd = new VD();
                            myData.setVd_vkn(taxNumber);
                            myData.setVd_unvan_donen("N/A");
                            myData.setVd_tc_donen("N/A");
                            myData.setVd_fiili_durum_donen("N/A");
                            myData.setVd_vdkodu("N/A");
                            myData.setOid(newList.get(i).getOid());
                            myData.setPlaka("N/A");
                            myData.setVd_tum_il_na((long) 1);

                            vd.setData(myData);

                            myDatas.add(myData);
                            continue;
                        }
                    }
                isFound.set(true);

                vd.getData().setOid(newList.get(i).getOid());
                vd.getData().setVd_tc_donen(vd.getData().getTckn());
                vd.getData().setVd_vkn(newList.get(i).getVd_vkn().trim());
                vd.getData().setVd_fiili_durum_donen(vd.getData().getDurum_text());
                vd.getData().setVd_unvan_donen(vd.getData().getUnvan());
                vd.getData().setVd_vdkodu(vd.getData().getVdkodu());
                vd.getData().setPlaka(plate);

                myData = vd.getData();

            myDatas.add(myData);
        }
        return myDatas;
    }

    public List<Data> getResponseVkn(List<Data> newList) throws MessagingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<Data> myDatas = new ArrayList<>();
        for (int i = 0; i < newList.size(); i++) {
            Data myData = new Data();

            try {

                String taxNumber = newList.get(i).getVd_vkn();
                taxNumber = taxNumber.replace(" ","");

                    AtomicReference<Boolean> isFound = new AtomicReference<>(false);

                    HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?vkn=" + taxNumber.trim() + "&plate=" + newList.get(i).getPlaka() + "&detail=1")
                            .header("accept", "application/json")
                            .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .header("Connection", "keep-alive")
                            .socketTimeout(120000)
                            .asJson();

                    String responseString = jsonResponse.getBody().toString();

                    VD vd = mapper.readValue(responseString, new TypeReference<VD>() {
                    });

                    if( vd.getData().getVkn().length() == 0 || vd == null || vd.getData().getVkn() == null || vd.getData().getVdkodu() == null || vd.getData().getVdkodu().length() == 0 || vd.getData().getDurum_text() == null || vd.getData().getDurum_text().length() == 0 ){

                        if(!isFound.get()){
                            vd = new VD();
                            myData.setVd_vkn(taxNumber);
                            myData.setVd_unvan_donen("N/A");
                            myData.setVd_tc_donen("N/A");
                            myData.setVd_fiili_durum_donen("N/A");
                            myData.setVd_vdkodu("N/A");
                            myData.setOid(newList.get(i).getOid());
                            myData.setPlaka("N/A");

                            vd.setData(myData);

                            myDatas.add(myData);
                            continue;
                        }
                    }
                    isFound.set(true);

                    vd.getData().setOid(newList.get(i).getOid());
                    vd.getData().setVd_tc_donen(vd.getData().getTckn());
                    vd.getData().setVd_vkn(newList.get(i).getVd_vkn().trim());
                    vd.getData().setVd_fiili_durum_donen(vd.getData().getDurum_text());
                    vd.getData().setVd_unvan_donen(vd.getData().getUnvan());
                    vd.getData().setVd_vdkodu(vd.getData().getVdkodu());

                    myData = vd.getData();


            } catch (Exception e) {

                e.printStackTrace();
                System.out.println(e.toString());
                mailer.sendEmail("gizemelif.atalay@gvg.com.tr", "HTTP Response hatası", e.toString());
            }
            myDatas.add(myData);
        }
        return myDatas;
    }


    public List<Data> getResponse(List<Data> newList) throws MessagingException {
        //ObjectMapper mapper = new ObjectMapper();
        //mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<Data> myDatas = new ArrayList<>();
        for (int i = 0; i < newList.size(); i++) {
            Data myData = new Data();
            TaxDetailResult taxDetailResult = new TaxDetailResult();
            Integer satirlarSize = 0;

            try {
                String governmentNum = newList.get(i).getTckn();
                governmentNum = governmentNum.replace(" ","");

                    AtomicReference<Boolean> isFound = new AtomicReference<>(false);

                    HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?tc=" + governmentNum.trim() + "&plate=" + newList.get(i).getPlaka() + "&detail=1")
                            .header("accept", "application/json")
                            .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .header("Connection", "keep-alive")
                            .socketTimeout(120000)
                            .asJson();

                    String responseString = jsonResponse.getBody().toString();

                    //VD vd = mapper.readValue(responseString, new TypeReference<VD>() {
                    //});
                JSONObject data1 = new JSONObject(responseString);
                JSONObject data2 = (JSONObject) data1.get("data");
                JSONObject taxResult = (JSONObject) data2.get("TaxDetailResult");
                JSONObject resultData = (JSONObject) taxResult.get("data");
                if(!resultData.get("satirlarSize").equals(satirlarSize)){
                    JSONArray satirlar = resultData.getJSONArray("satirlar");
                    for(Object o : satirlar){
                        JSONObject jsonLineItem = (JSONObject) o;
                        myData.setMatrah(jsonLineItem.get("matrah").toString());
                        myData.setTahakkukeden(jsonLineItem.get("tahakkukeden").toString());
                        myData.setYil(jsonLineItem.get("yil").toString());
                        break;
                    }
                }

                if(data2.toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0){
                    if(!isFound.get()){
                        VD vd = new VD();
                        myData.setDurum_text("N/A");
                        myData.setTckn(governmentNum);
                        myData.setUnvan("N/A");
                        myData.setVdkodu("N/A");
                        myData.setVkn("N/A");
                        myData.setOid(newList.get(i).getOid());
                        myData.setPlaka("N/A");
                        myData.setMatrah("N/A");
                        myData.setIsebaslamatarihi("N/A");
                        myData.setNacekoduaciklama("N/A");
                        myData.setTahakkukeden("N/A");

                        vd.setData(myData);

                        myDatas.add(myData);
                        continue;
                    }

                }
                    /*if( vd.getData().getTckn().length() == 0 || vd == null || vd.getData().getTckn() == null || vd.getData().getVdkodu() == null || vd.getData().getVdkodu().length() == 0){

                        if(!isFound.get()){
                            vd = new VD();
                            myData.setDurum_text("N/A");
                            myData.setTckn(governmentNum);
                            myData.setUnvan("N/A");
                            myData.setVdkodu("N/A");
                            myData.setVkn("N/A");
                            myData.setOid(newList.get(i).getOid());
                            myData.setPlaka("N/A");

                            vd.setData(myData);

                            myDatas.add(myData);
                            continue;
                        }
                    }
                    vd.getData().setOid(newList.get(i).getOid());
                    vd.getData().setPlaka(newList.get(i).getPlaka());
                    vd.getData().setVkn(vd.getData().getVkn());
                    vd.getData().setDurum_text(vd.getData().getDurum_text());
                    */
                    isFound.set(true);

                    myData.setOid(newList.get(i).getOid());
                    myData.setTckn(governmentNum);
                    myData.setPlaka(newList.get(i).getPlaka());
                    myData.setVdkodu(resultData.get("vdkodu").toString());
                    myData.setUnvan(resultData.get("adsoyad").toString());
                    myData.setVkn(data2.get("vkn").toString());
                    myData.setDurum_text((String) data2.get("durum_text"));
                    myData.setTc_adres_donen(resultData.get("adres").toString());
                    myData.setIsebaslamatarihi(resultData.get("isebaslamatarihi").toString());
                    myData.setNacekoduaciklama(resultData.get("nacekoduaciklama").toString());
                    //satirlarSize = resultData.get("satirlarSize").toString();

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
