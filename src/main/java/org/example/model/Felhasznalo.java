package org.example.model;

/**
 * Felhasznalo modellezese
 */

public class Felhasznalo
{
    private String felhasznaloNev;
    private int felhasznaloId;
    private String jelszoHASH;

    public Felhasznalo() {

    }

    public String getFelhasznaloNev() {
        return felhasznaloNev;
    }

    public void setFelhasznaloNev(String felhasznaloNev) {
        this.felhasznaloNev = felhasznaloNev;
    }

    public int getFelhasznaloId() {
        return felhasznaloId;
    }

    public void setFelhasznaloId(int felhasznaloId) {
        this.felhasznaloId = felhasznaloId;
    }

    public String getJelszoHASH() {
        return jelszoHASH;
    }

    public void setJelszoHASH(String jelszoHASH) {
        this.jelszoHASH = jelszoHASH;
    }
}