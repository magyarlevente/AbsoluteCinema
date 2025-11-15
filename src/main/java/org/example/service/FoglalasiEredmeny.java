package org.example.service;

public class FoglalasiEredmeny {

    private final boolean siker;
    private final String uzenet;

    public FoglalasiEredmeny(boolean siker, String uzenet) {
        this.siker = siker;
        this.uzenet = uzenet;
    }

    public boolean isSiker() {
        return siker;
    }

    public String getUzenet() {
        return uzenet;
    }
}