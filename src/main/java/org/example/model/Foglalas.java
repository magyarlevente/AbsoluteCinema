package org.example.model;

import java.time.LocalDateTime;

public class Foglalas {

    private int foglalasId;
    private int felhasznaloId;
    private int idopontId;
    private int ulohelyId;
    private LocalDateTime foglalasDatuma;
    private boolean fizetve;

    public Foglalas() {
    }

    public Foglalas(int felhasznaloId, int idopontId, int ulohelyId) {
        this.felhasznaloId = felhasznaloId;
        this.idopontId = idopontId;
        this.ulohelyId = ulohelyId;
        this.foglalasDatuma = LocalDateTime.now();
        this.fizetve = false;
    }

    public int getFoglalasId() {
        return foglalasId;
    }

    public void setFoglalasId(int foglalasId) {
        this.foglalasId = foglalasId;
    }

    public int getFelhasznaloId() {
        return felhasznaloId;
    }

    public void setFelhasznaloId(int felhasznaloId) {
        this.felhasznaloId = felhasznaloId;
    }

    public int getIdopontId() {
        return idopontId;
    }

    public void setIdopontId(int idopontId) {
        this.idopontId = idopontId;
    }

    public int getUlohelyId() {
        return ulohelyId;
    }

    public void setUlohelyId(int ulohelyId) {
        this.ulohelyId = ulohelyId;
    }

    public LocalDateTime getFoglalasDatuma() {
        return foglalasDatuma;
    }

    public void setFoglalasDatuma(LocalDateTime foglalasDatuma) {
        this.foglalasDatuma = foglalasDatuma;
    }

    public boolean isFizetve() {
        return fizetve;
    }

    public void setFizetve(boolean fizetve) {
        this.fizetve = fizetve;
    }
}