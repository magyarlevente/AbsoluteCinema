package org.example.service;

public class ErtekelesEredmeny {

    private final boolean siker;
    private final String uzenet;

    public ErtekelesEredmeny(boolean siker, String uzenet) {
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