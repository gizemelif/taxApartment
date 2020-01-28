package com.tax.verify.dto;

import java.util.HashMap;
import java.util.Map;

public class TaxDetailResult {
    private Metadata metadata;
    private Data data;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /*private String adres;
    private String nacekoduaciklama;
    private String isebaslamatarihi;
    private String matrah;
    private String tahakkukeden;
    private String yil;*/

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

   /* public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getNacekoduaciklama() {
        return nacekoduaciklama;
    }

    public void setNacekoduaciklama(String nacekoduaciklama) {
        this.nacekoduaciklama = nacekoduaciklama;
    }

    public String getIsebaslamatarihi() {
        return isebaslamatarihi;
    }

    public void setIsebaslamatarihi(String isebaslamatarihi) {
        this.isebaslamatarihi = isebaslamatarihi;
    }

    public String getMatrah() {
        return matrah;
    }

    public void setMatrah(String matrah) {
        this.matrah = matrah;
    }

    public String getTahakkukeden() {
        return tahakkukeden;
    }

    public void setTahakkukeden(String tahakkukeden) {
        this.tahakkukeden = tahakkukeden;
    }

    public String getYil() {
        return yil;
    }

    public void setYil(String yil) {
        this.yil = yil;
    }*/
}
