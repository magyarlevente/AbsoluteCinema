package org.example.model;

//konkret ulohelyet reprezental

public class Ulohely {

    private int ulohelyId;
    private int teremId;
    private String sorJele;
    private int ulohelySzam;

    public Ulohely() {
    }

    public Ulohely(int teremId, String sorJele, int ulohelySzam) {
        this.teremId = teremId;
        this.sorJele = sorJele;
        this.ulohelySzam = ulohelySzam;
    }

    public int getUlohelyId() {
        return ulohelyId;
    }

    public void setUlohelyId(int ulohelyId) {
        this.ulohelyId = ulohelyId;
    }

    public int getTeremId() {
        return teremId;
    }

    public void setTeremId(int teremId) {
        this.teremId = teremId;
    }

    public String getSorJele() {
        return sorJele;
    }

    public void setSorJele(String sorJele) {
        this.sorJele = sorJele;
    }

    public int getUlohelySzam() {
        return ulohelySzam;
    }

    public void setUlohelySzam(int ulohelySzam) {
        this.ulohelySzam = ulohelySzam;
    }
}