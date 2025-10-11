package org.example.model;

import java.util.List;

public class Film {
    private String cim;
    private List<String> idopontok;

    public Film(String cim, List<String> idopontok) {
        this.cim = cim;
        this.idopontok = idopontok;
    }

    public String getCim() {
        return cim;
    }

    public List<String> getIdopontok() {
        return idopontok;
    }
}
