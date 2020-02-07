package com.tax.verify.thirdparty.excel;

public class MyCell {
    private String vd_sorulan;
    private String tc_sorulan;
    private String plaka;

    public MyCell(String vd_sorulan, String tc_sorulan, String plaka) {
        this.vd_sorulan = vd_sorulan;
        this.tc_sorulan = tc_sorulan;
        this.plaka = plaka;
    }

    public MyCell() {
    }

    public MyCell(String vd_sorulan) {
        this.vd_sorulan = vd_sorulan;
    }

    public String getVd_sorulan() {
        return vd_sorulan;
    }

    public void setVd_sorulan(String vd_sorulan) {
        this.vd_sorulan = vd_sorulan;
    }

    public String getTc_sorulan() {
        return tc_sorulan;
    }

    public void setTc_sorulan(String tc_sorulan) {
        this.tc_sorulan = tc_sorulan;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }
}
