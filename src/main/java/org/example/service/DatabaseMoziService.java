package org.example.service;

import org.example.dao.*;
import org.example.model.*;
import java.util.List;

public class DatabaseMoziService implements MoziService {
    private final FilmDAO filmDAO = new FilmDAO();
    private final IdopontDAO idopontDAO = new IdopontDAO();
    private final UlohelyDAO ulohelyDAO = new UlohelyDAO();
    private final FoglalasDAO foglalasDAO = new FoglalasDAO();
    private final FelhasznaloDAO felhasznaloDAO = new FelhasznaloDAO();

    // ÚJ: TeremDAO példányosítása
    private final TeremDAO teremDAO = new TeremDAO();

    @Override
    public List<Film> getMindenFilm() { return filmDAO.findAll(); }

    @Override
    public List<Idopont> getIdopontokFilmhez(int filmId) { return idopontDAO.findByFilmId(filmId); }

    @Override
    public List<Ulohely> getUlohelyekIdoponthoz(int idopontId) { return ulohelyDAO.findByIdopont(idopontId); }

    @Override
    public List<Foglalas> getFoglalasokIdoponthoz(int idopontId) { return foglalasDAO.findByIdopont(idopontId); }

    @Override
    public FoglalasiEredmeny megprobalFoglalni(int idopontId, int ulohelyId, int felhasznaloId) {
        List<Foglalas> letezo = foglalasDAO.findByIdopont(idopontId);
        if (letezo.stream().anyMatch(f -> f.getUlohelyId() == ulohelyId)) {
            return new FoglalasiEredmeny(false, "Ez a hely már foglalt!");
        }
        Foglalas uj = new Foglalas(felhasznaloId, idopontId, ulohelyId);
        uj.setFizetve(true);
        if (foglalasDAO.create(uj)) return new FoglalasiEredmeny(true, "Sikeres foglalás!");
        return new FoglalasiEredmeny(false, "Hiba a mentéskor.");
    }

    @Override
    public AuthEredmeny megprobalRegisztralni(String u, String p) {
        if (u == null || u.isEmpty() || p == null || p.isEmpty()) return new AuthEredmeny(false, "Hiányzó adatok");
        if (felhasznaloDAO.register(u, p)) return new AuthEredmeny(true, "Sikeres regisztráció!");
        return new AuthEredmeny(false, "Foglalt felhasználónév.");
    }

    @Override
    public AuthEredmeny megprobalBejelentkezni(String u, String p) {
        Felhasznalo user = felhasznaloDAO.login(u, p);
        if (user != null) return new AuthEredmeny(true, "Sikeres belépés!", user);
        return new AuthEredmeny(false, "Hibás adatok.");
    }

    @Override
    public ErtekelesEredmeny megprobalErtekelni(int filmId, int felhasznaloId, int pontszam, String szovegesErtekeles) {
        return new ErtekelesEredmeny(false, "Nincs implementálva.");
    }

    @Override
    public List<Foglalas> getFoglalasokFelhasznalonak(int felhasznaloId) {
        return foglalasDAO.findByFelhasznaloId(felhasznaloId);
    }

    @Override
    public boolean torolFoglalas(int foglalasId) {
        return foglalasDAO.delete(foglalasId);
    }

    @Override
    public Film getFilmById(int id) { return filmDAO.findById(id); }

    @Override
    public Idopont getIdopontById(int id) { return idopontDAO.findById(id); }

    @Override
    public Ulohely getUlohelyById(int id) { return ulohelyDAO.findById(id); }

    @Override
    public Terem getTeremById(int id) { return teremDAO.findById(id); }
}