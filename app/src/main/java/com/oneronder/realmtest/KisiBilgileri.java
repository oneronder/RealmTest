package com.oneronder.realmtest;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class KisiBilgileri extends RealmObject {

    private String kullaniciAdi;
    private String sifre;
    private String cinsiyet;
    private String isim;

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    @Override
    public String toString() {
        return "KisiBilgileri{" +
                "kullanici='" + kullaniciAdi + '\'' +
                ", sifre='" + sifre + '\'' +
                ", cinsiyet='" + cinsiyet + '\'' +
                ", isim='" + isim + '\'' +
                '}';
    }
}
