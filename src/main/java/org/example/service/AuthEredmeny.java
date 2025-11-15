package org.example.service;

import org.example.model.Felhasznalo;

public class AuthEredmeny {

    private final boolean siker;
    private final String uzenet;
    private final Felhasznalo felhasznalo;

    public AuthEredmeny(boolean siker, String uzenet) {
        this.siker = siker;
        this.uzenet = uzenet;
        this.felhasznalo = null;
    }

    public AuthEredmeny(boolean siker, String uzenet, Felhasznalo felhasznalo) {
        this.siker = siker;
        this.uzenet = uzenet;
        this.felhasznalo = felhasznalo;
    }

    public boolean isSiker() {
        return siker;
    }

    public String getUzenet() {
        return uzenet;
    }

    public Felhasznalo getFelhasznalo() {
        return felhasznalo;
    }
}