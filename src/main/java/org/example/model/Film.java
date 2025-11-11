package org.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Film
{
    private String cim;
    private int filmId;
    private int jatekido;
    private LocalDate bemutatoIdeje;
    private String mufaj;
    private List <String> szereplok;
    private String filmLeiras;

    public Film() {

    }

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getJatekido() {
        return jatekido;
    }

    public void setJatekido(int jatekido) {
        this.jatekido = jatekido;
    }

    public LocalDate getBemutatoIdeje() {
        return bemutatoIdeje;
    }

    public void setBemutatoIdeje(LocalDate bemutatoIdeje) {
        this.bemutatoIdeje = bemutatoIdeje;
    }

    public String getMufaj() {
        return mufaj;
    }

    public void setMufaj(String mufaj) {
        this.mufaj = mufaj;
    }

    public List<String> getSzereplok() {
        return szereplok;
    }

    public void setSzereplok(List<String> szereplok) {
        this.szereplok = szereplok;
    }

    public String getFilmLeiras() {
        return filmLeiras;
    }

    public void setFilmLeiras(String filmLeiras) {
        this.filmLeiras = filmLeiras;
    }
}