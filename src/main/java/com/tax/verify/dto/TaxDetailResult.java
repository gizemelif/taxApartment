package com.tax.verify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TaxDetailResult {
    private String adres;
    private String nacekoduaciklama;
    private String isebaslamatarihi;
    private String matrah;
    private String tahakkukeden;
    private String yil;

    public String getAdres() {
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
    }
}
