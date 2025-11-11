package org.example.model;

import java.time.LocalDate;

public class Ertekeles {

    private int ertekelesId;
    private int filmId;
    private int felhasznaloId;
    private int pontszam;
    private String szovegesErtekeles;
    private LocalDate datum;

    public Ertekeles() {
    }

    // Paraméteres konstruktor új értékelés létrehozásához
    public Ertekeles(int filmId, int felhasznaloId, int pontszam, String szovegesErtekeles) {
        this.filmId = filmId;
        this.felhasznaloId = felhasznaloId;
        this.pontszam = pontszam;
        this.szovegesErtekeles = szovegesErtekeles;
        this.datum = LocalDate.now(); // Az aktuális dátum beállítása
    }

    // Getterek és Setterek

    public int getErtekelesId() {
        return ertekelesId;
    }

    public void setErtekelesId(int ertekelesId) {
        this.ertekelesId = ertekelesId;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getFelhasznaloId() {
        return felhasznaloId;
    }

    public void setFelhasznaloId(int felhasznaloId) {
        this.felhasznaloId = felhasznaloId;
    }

    public int getPontszam() {
        return pontszam;
    }

    public void setPontszam(int pontszam) {
        this.pontszam = pontszam;
    }

    public String getSzovegesErtekeles() {
        return szovegesErtekeles;
    }

    public void setSzovegesErtekeles(String szovegesErtekeles) {
        this.szovegesErtekeles = szovegesErtekeles;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
}