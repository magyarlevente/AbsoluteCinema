package org.example.model;

//vetitesek helyszinenek azonositasara hasznaljuk

public class Terem {
    private int teremId;
    private String teremNev;

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

}
