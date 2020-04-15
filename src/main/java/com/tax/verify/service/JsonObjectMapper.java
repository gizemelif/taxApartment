package com.tax.verify.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tax.verify.model.Data;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JsonObjectMapper {

    public static Data jsonMapperTc(Data gelenData, String responseString, String plate){
        Integer satirlarSize = 0;
        Data myData = new Data();
        Data data = new Data();

        if(gelenData != null){
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
                    data.setTckn(data2.get("tckn").toString());
                    data.setUnvan("N/A");
                    data.setVdkodu("N/A");
                    data.setVkn("N/A");
                    data.setOid(gelenData.getOid());
                    data.setPlaka("N/A");
                    data.setIsebaslamatarihi("N/A");
                    data.setNacekoduaciklama("N/A");
                    data.setTc_adres_donen("N/A");

                }
                data.setOid(gelenData.getOid());
                data.setTckn(data2.get("tckn").toString());
                data.setPlaka(plate);
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

            return data;
        }else {
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
                myData.setTckn(data2.get("tckn").toString());
                myData.setUnvan("N/A");
                myData.setVdkodu("N/A");
                myData.setVkn("N/A");
                myData.setPlaka("N/A");
                myData.setIsebaslamatarihi("N/A");
                myData.setNacekoduaciklama("N/A");
                myData.setTc_adres_donen("N/A");

            }
            myData.setTckn(data2.get("tckn").toString());
            myData.setPlaka(plate);
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
        }
        return myData;
    }
    public static Data jsonMapperVD(Data data, String responseString, String plate){
        Integer satirlarSize = 0;
        Data myData = new Data();

        if(data != null) {
            try {
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
                } else {
                    myData.setMatrah_vd("N/A");
                    myData.setTahakkukeden_vd("N/A");
                    myData.setYil_vd("N/A");
                }
                if (data2.toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0) {

                    myData.setVd_fiili_durum_donen("N/A");
                    myData.setVd_vkn(data2.get("vkn").toString());
                    myData.setVd_unvan_donen("N/A");
                    myData.setVd_vdkodu("N/A");
                    myData.setVd_tc_donen("N/A");
                    myData.setOid(data.getOid());
                    myData.setPlaka("N/A");
                    myData.setNacekoduaciklama_vd("N/A");
                    myData.setVd_adres_donen("N/A");

                }
                myData.setOid(data.getOid());
                myData.setVd_vkn(data2.get("vkn").toString());
                myData.setPlaka(plate);
                myData.setVd_vdkodu(data2.get("vdkodu").toString());
                myData.setVd_unvan_donen(data2.get("unvan").toString());
                myData.setVd_tc_donen(data2.get("tckn").toString());
                myData.setVd_fiili_durum_donen((String) data2.get("durum_text"));

                if (resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0) {
                    myData.setVd_adres_donen("N/A");
                    myData.setIsebaslamatarihi_vd("N/A");
                    myData.setNacekoduaciklama_vd("N/A");
                } else {
                    myData.setVd_adres_donen(resultData.get("adres").toString());
                    myData.setIsebaslamatarihi_vd(resultData.get("isebaslamatarihi").toString());
                    myData.setNacekoduaciklama_vd(resultData.get("nacekoduaciklama").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
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
            } else {
                myData.setMatrah_vd("N/A");
                myData.setTahakkukeden_vd("N/A");
                myData.setYil_vd("N/A");
            }
            if (data2.toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vkn").toString().length() == 0 || data2.get("vdkodu") == null || data2.get("vdkodu").toString().length() == 0) {

                myData.setVd_fiili_durum_donen("N/A");
                myData.setVd_vkn(data2.get("vkn").toString());
                myData.setVd_unvan_donen("N/A");
                myData.setVd_vdkodu("N/A");
                myData.setVd_tc_donen("N/A");
                myData.setPlaka("N/A");
                myData.setNacekoduaciklama_vd("N/A");
                myData.setVd_adres_donen("N/A");

            }
            myData.setVd_vkn(data2.get("vkn").toString());
            myData.setPlaka(plate);
            myData.setVd_vdkodu(data2.get("vdkodu").toString());
            myData.setVd_unvan_donen(data2.get("unvan").toString());
            myData.setVd_tc_donen(data2.get("tckn").toString());
            myData.setVd_fiili_durum_donen((String) data2.get("durum_text"));

            if (resultData.get("adres").toString().length() == 0 || resultData.get("isebaslamatarihi").toString().length() == 0 || resultData.get("nacekoduaciklama").toString().length() == 0) {
                myData.setVd_adres_donen("N/A");
                myData.setIsebaslamatarihi_vd("N/A");
                myData.setNacekoduaciklama_vd("N/A");
            } else {
                myData.setVd_adres_donen(resultData.get("adres").toString());
                myData.setIsebaslamatarihi_vd(resultData.get("isebaslamatarihi").toString());
                myData.setNacekoduaciklama_vd(resultData.get("nacekoduaciklama").toString());
            }
        }
        return myData;

    }

    public HttpResponse httpGet(String queryType, String qText, String plaka){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?"+ queryType + qText + "&plate=" + plaka + "&detail=1")
                .header("accept", "application/json")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Connection", "keep-alive")
                .socketTimeout(120000)
                .asJson();

        return jsonResponse;
    }
    /*public HttpResponse httpGetVd(String queryType, String plaka){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        HttpResponse jsonResponse = Unirest.get("http://192.168.1.31:8687/vd?vkn=" + queryType + "&plate=" + plaka + "&detail=1")
                .header("accept", "application/json")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Connection", "keep-alive")
                .socketTimeout(120000)
                .asJson();

        return jsonResponse;
    }*/


}
