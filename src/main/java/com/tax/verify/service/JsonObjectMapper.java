package com.tax.verify.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tax.verify.dto.Data;
import com.tax.verify.jpa.IndexRepository;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class JsonObjectMapper {
    @Autowired
    private IndexRepository ındexRepository;

    public static Data jsonMapperTc(List<Data> dataList, String responseString){
        Integer satirlarSize = 0;
        Data myData = new Data();
        Data data = new Data();

        if(dataList.size() > 0 || dataList != null){
            for (int i = 0; i < dataList.size(); i++) {
                data = new Data();
                try {
                    JSONObject data1 = new JSONObject(responseString);
                    JSONObject data2 = (JSONObject) data1.get("data");
                    JSONObject taxResult = (JSONObject) data2.get("TaxDetailResult");
                    JSONObject resultData = (JSONObject) taxResult.get("data");
                    if (!resultData.get("satirlarSize").equals(satirlarSize)) {
                        JSONArray satirlar = resultData.getJSONArray("satirlar");
                        for (Object o : satirlar) {
                            JSONObject jsonLineItem = (JSONObject) o;
                            data.setMatrah(jsonLineItem.get("matrah").toString());
                            data.setTahakkukeden(jsonLineItem.get("tahakkukeden").toString());
                            data.setYil(jsonLineItem.get("yil").toString());
                            break;
                        }
                    }else{
                        data.setMatrah("N/A");
                        data.setTahakkukeden("N/A");
                        data.setYil("N/A");
                    }
                    if (data2.toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0) {

                        data.setDurum_text("N/A");
                        data.setTckn("tckn");
                        data.setUnvan("N/A");
                        data.setVdkodu("N/A");
                        data.setVkn("N/A");
                        data.setOid(dataList.get(i).getOid());
                        data.setPlaka("N/A");
                        data.setIsebaslamatarihi("N/A");
                        data.setNacekoduaciklama("N/A");
                        data.setTc_adres_donen("N/A");

                    }
                    data.setOid(dataList.get(i).getOid());
                    data.setTckn("tckn");
                    data.setPlaka("plaka");
                    data.setVdkodu(data2.get("vdkodu").toString());
                    data.setUnvan(data2.get("unvan").toString());
                    data.setVkn(data2.get("vkn").toString());
                    data.setDurum_text((String) data2.get("durum_text"));

                    if(resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0 ){
                        data.setTc_adres_donen("N/A");
                        data.setIsebaslamatarihi("N/A");
                        data.setNacekoduaciklama("N/A");
                    }else{
                        data.setTc_adres_donen(resultData.get("adres").toString());
                        data.setIsebaslamatarihi(resultData.get("isebaslamatarihi").toString());
                        data.setNacekoduaciklama(resultData.get("nacekoduaciklama").toString());
                    }
                }catch (Exception e){e.printStackTrace();}

            }
            return data;
        }

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
        } else {
            myData.setMatrah("N/A");
            myData.setTahakkukeden("N/A");
            myData.setYil("N/A");
        }
        if (data2.toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("tckn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0) {

            myData.setDurum_text("N/A");
            myData.setTckn("tckn");
            myData.setUnvan("N/A");
            myData.setVdkodu("N/A");
            myData.setVkn("N/A");
            myData.setPlaka("N/A");
            myData.setIsebaslamatarihi("N/A");
            myData.setNacekoduaciklama("N/A");
            myData.setTc_adres_donen("N/A");

        }
        myData.setTckn("tckn");
        myData.setPlaka("plaka");
        myData.setVdkodu(data2.get("vdkodu").toString());
        myData.setUnvan(data2.get("unvan").toString());
        myData.setVkn(data2.get("vkn").toString());
        myData.setDurum_text((String) data2.get("durum_text"));

        if (resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0) {
            myData.setTc_adres_donen("N/A");
            myData.setIsebaslamatarihi("N/A");
            myData.setNacekoduaciklama("N/A");
        } else {
            myData.setTc_adres_donen(resultData.get("adres").toString());
            myData.setIsebaslamatarihi(resultData.get("isebaslamatarihi").toString());
            myData.setNacekoduaciklama(resultData.get("nacekoduaciklama").toString());
        }

        return myData;
    }
    public static Data jsonMapperVD(List<Data> dataList, String responseString){
        Integer satirlarSize = 0;
        Data myData = new Data();
        Data data = new Data();

        if(dataList.size() > 0 || dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                data = new Data();
                try {
                    JSONObject data1 = new JSONObject(responseString);
                    JSONObject data2 = (JSONObject) data1.get("data");
                    JSONObject taxResult = (JSONObject) data2.get("TaxDetailResult");
                    JSONObject resultData = (JSONObject) taxResult.get("data");
                    if (!resultData.get("satirlarSize").equals(satirlarSize)) {
                        JSONArray satirlar = resultData.getJSONArray("satirlar");
                        for (Object o : satirlar) {
                            JSONObject jsonLineItem = (JSONObject) o;
                            data.setMatrah(jsonLineItem.get("matrah").toString());
                            data.setTahakkukeden(jsonLineItem.get("tahakkukeden").toString());
                            data.setYil(jsonLineItem.get("yil").toString());
                            break;
                        }
                    } else {
                        data.setMatrah("N/A");
                        data.setTahakkukeden("N/A");
                        data.setYil("N/A");
                    }
                    if (data2.toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0) {

                        data.setVd_fiili_durum_donen("N/A");
                        data.setVd_vkn("vkn");
                        data.setVd_unvan_donen("N/A");
                        data.setVd_vdkodu("N/A");
                        data.setVd_tc_donen("N/A");
                        data.setOid(dataList.get(i).getOid());
                        data.setPlaka("N/A");
                        data.setNacekoduaciklama_vd("N/A");
                        data.setNacekoduaciklama_vd("N/A");
                        data.setVd_adres_donen("N/A");

                    }
                    data.setOid(dataList.get(i).getOid());
                    data.setVd_vkn("vkn");
                    data.setPlaka("plaka");
                    data.setVd_vdkodu(data2.get("vdkodu").toString());
                    data.setVd_unvan_donen(data2.get("unvan").toString());
                    data.setVd_tc_donen(data2.get("tckn").toString());
                    data.setVd_fiili_durum_donen((String) data2.get("durum_text"));

                    if (resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0) {
                        data.setVd_adres_donen("N/A");
                        data.setIsebaslamatarihi("N/A");
                        data.setNacekoduaciklama("N/A");
                    } else {
                        data.setVd_adres_donen(resultData.get("adres").toString());
                        data.setIsebaslamatarihi(resultData.get("isebaslamatarihi").toString());
                        data.setNacekoduaciklama(resultData.get("nacekoduaciklama").toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return data;
        }

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
        } else {
            myData.setMatrah("N/A");
            myData.setTahakkukeden("N/A");
            myData.setYil("N/A");
        }
        if (data2.toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0) {

            myData.setVd_fiili_durum_donen("N/A");
            myData.setVd_vkn("vkn");
            myData.setVd_unvan_donen("N/A");
            myData.setVd_vdkodu("N/A");
            myData.setVd_tc_donen("N/A");
            myData.setPlaka("N/A");
            myData.setNacekoduaciklama_vd("N/A");
            myData.setNacekoduaciklama_vd("N/A");
            myData.setVd_adres_donen("N/A");

        }
        myData.setVd_vkn("vkn");
        myData.setPlaka("plaka");
        myData.setVd_vdkodu(data2.get("vdkodu").toString());
        myData.setVd_unvan_donen(data2.get("unvan").toString());
        myData.setVd_tc_donen(data2.get("tckn").toString());
        myData.setVd_fiili_durum_donen((String) data2.get("durum_text"));

        if (resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0) {
            myData.setVd_adres_donen("N/A");
            myData.setIsebaslamatarihi("N/A");
            myData.setNacekoduaciklama("N/A");
        } else {
            myData.setVd_adres_donen(resultData.get("adres").toString());
            myData.setIsebaslamatarihi(resultData.get("isebaslamatarihi").toString());
            myData.setNacekoduaciklama(resultData.get("nacekoduaciklama").toString());
        }
        return myData;
    }

    public static HttpResponse httpGet(String queryType, String plaka){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?tc=" + queryType + "&plate=" + plaka + "&detail=1")
                .header("accept", "application/json")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Connection", "keep-alive")
                .socketTimeout(120000)
                .asJson();

        return jsonResponse;
    }
    public static HttpResponse httpGetVd(String queryType, String plaka){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?vkn=" + queryType + "&plate=" + plaka + "&detail=1")
                .header("accept", "application/json")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Connection", "keep-alive")
                .socketTimeout(120000)
                .asJson();

        return jsonResponse;
    }


}
