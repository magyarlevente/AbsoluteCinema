package org.example.model;

public class Terem {
    private int teremId;
    private String teremNev;
    private int ulohelyekSzama;

    public Terem() {}


    public int getTeremId() {
        return teremId;
    }

    public void setTeremId(int teremId) {
        this.teremId = teremId;
    }

    public String getTeremNev() {
        return teremNev;
    }

    public void setTeremNev(String teremNev) {
        this.teremNev = teremNev;
    }

    public int getUlohelyekSzama() {
        return ulohelyekSzama;
    }

    public void setUlohelyekSzama(int ulohelyekSzama) {
        this.ulohelyekSzama = ulohelyekSzama;
    }
}
