package org.example.model;

import java.time.LocalDateTime;

//konkret vetites peldanyanak a modelje

public class Idopont {
    private int IdopontId;
    private LocalDateTime kezdesIdopont;
    private int filmId;
    private int teremId;

    public Idopont() {}

    public Idopont(LocalDateTime kezdesIdopont, int filmId, int teremId) {
        this.kezdesIdopont = kezdesIdopont;
        this.filmId = filmId;
        this.teremId = teremId;
    }

    public int getIdopontId() {
        return IdopontId;
    }

    public void setIdopontId(int idopontId) {
        IdopontId = idopontId;
    }

    public LocalDateTime getKezdesIdopont() {
        return kezdesIdopont;
    }

    public void setKezdesIdopont(LocalDateTime kezdesIdopont) {
        this.kezdesIdopont = kezdesIdopont;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getTeremId() {
        return teremId;
    }

    public void setTeremId(int teremId) {
        this.teremId = teremId;
    }

}
