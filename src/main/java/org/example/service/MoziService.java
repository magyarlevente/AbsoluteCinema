package org.example.service;

import org.example.model.Film;
import org.example.model.Idopont;
import org.example.model.Ulohely;
import org.example.model.Foglalas;
import org.example.model.Felhasznalo;

import java.util.List;

public interface MoziService {

    List<Film> getMindenFilm();

    List<Idopont> getIdopontokFilmhez(int filmId);

    List<Ulohely> getUlohelyekIdoponthoz(int idopontId);

    List<Foglalas> getFoglalasokIdoponthoz(int idopontId);

    FoglalasiEredmeny megprobalFoglalni(int idopontId, int ulohelyId, int felhasznaloId);

    AuthEredmeny megprobalRegisztralni(String felhasznalonev, String jelszo);

    AuthEredmeny megprobalBejelentkezni(String felhasznalonev, String jelszo);

    ErtekelesEredmeny megprobalErtekelni(int filmId, int felhasznaloId, int pontszam, String szovegesErtekeles);
}