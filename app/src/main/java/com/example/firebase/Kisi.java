package com.example.firebase;

public class Kisi {
    private String kisiid;
    private String kisiismi;
    private String kisimobil;

    public Kisi() {

    }

    public Kisi(String kisiid, String kisiismi, String kisimobil) {
        this.kisiid = kisiid;
        this.kisiismi = kisiismi;
        this.kisimobil = kisimobil;
    }

    public String getKisiid() {
        return kisiid;
    }

    public void setKisiid(String kisiid) {
        this.kisiid = kisiid;
    }

    public String getKisiismi() {
        return kisiismi;
    }

    public void setKisiismi(String kisiismi) {
        this.kisiismi = kisiismi;
    }

    public String getKisimobil() {
        return kisimobil;
    }

    public void setKisimobil(String kisimobil) {
        this.kisimobil = kisimobil;
    }
}