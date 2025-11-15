package org.example.service;

import org.example.model.Film;
import org.example.model.Foglalas;
import org.example.model.Idopont;
import org.example.model.Ulohely;
import org.example.model.Felhasznalo;
import org.example.model.Ertekeles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockMoziService implements MoziService {

    private static final List<Film> FILMEK = new ArrayList<>();
    private static final List<Idopont> IDOPONTOK = new ArrayList<>();
    private static final List<Ulohely> ULOHELYEK = new ArrayList<>();
    private static final List<Foglalas> FOGLALASOK = new ArrayList<>();
    private static final List<Felhasznalo> FELHASZNALOK = new ArrayList<>();
    private static final List<Ertekeles> ERTEKELESEK = new ArrayList<>();

    private static int nextFilmId = 1;
    private static int nextIdopontId = 1;
    private static int nextUlohelyId = 1;
    private static int nextFoglalasId = 1;
    private static int nextFelhasznaloId = 1;
    private static int nextErtekelesId = 1;

    public MockMoziService() {
        if (FILMEK.isEmpty()) {
            loadTestData();
        }
    }

    private void loadTestData() {
        Film film1 = new Film();
        film1.setFilmId(nextFilmId++);
        film1.setCim("Dűne: Második rész");
        FILMEK.add(film1);

        Film film2 = new Film();
        film2.setFilmId(nextFilmId++);
        film2.setCim("A galaxis őrzői 3");
        FILMEK.add(film2);

        for (int i = 1; i <= 5; i++) {
            Ulohely u = new Ulohely(1, "A", i);
            u.setUlohelyId(nextUlohelyId++);
            ULOHELYEK.add(u);
        }
        for (int i = 1; i <= 5; i++) {
            Ulohely u = new Ulohely(2, "B", i);
            u.setUlohelyId(nextUlohelyId++);
            ULOHELYEK.add(u);
        }

        Idopont idopont1 = new Idopont(LocalDateTime.now().plusHours(2), 1, 1, 3000);
        idopont1.setIdopontId(nextIdopontId++);
        IDOPONTOK.add(idopont1);

        Idopont idopont2 = new Idopont(LocalDateTime.now().plusHours(3), 2, 2, 2800);
        idopont2.setIdopontId(nextIdopontId++);
        IDOPONTOK.add(idopont2);
    }

    @Override
    public List<Film> getMindenFilm() {
        return FILMEK;
    }

    @Override
    public List<Idopont> getIdopontokFilmhez(int filmId) {
        return IDOPONTOK.stream()
                .filter(idopont -> idopont.getFilmId() == filmId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ulohely> getUlohelyekIdoponthoz(int idopontId) {
        Idopont idopont = IDOPONTOK.stream()
                .filter(i -> i.getIdopontId() == idopontId)
                .findFirst()
                .orElse(null);

        if (idopont == null) return new ArrayList<>();

        int teremId = idopont.getTeremId();

        return ULOHELYEK.stream()
                .filter(ulohely -> ulohely.getTeremId() == teremId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Foglalas> getFoglalasokIdoponthoz(int idopontId) {
        return FOGLALASOK.stream()
                .filter(foglalas -> foglalas.getIdopontId() == idopontId)
                .collect(Collectors.toList());
    }

    @Override
    public FoglalasiEredmeny megprobalFoglalni(int idopontId, int ulohelyId, int felhasznaloId) {
        boolean foglalt = FOGLALASOK.stream()
                .anyMatch(f -> f.getIdopontId() == idopontId &&
                        f.getUlohelyId() == ulohelyId);

        if (foglalt) {
            return new FoglalasiEredmeny(false, "Sikertelen foglalás: Ez a hely már foglalt!");
        }

        Foglalas ujFoglalas = new Foglalas(felhasznaloId, idopontId, ulohelyId);
        ujFoglalas.setFoglalasId(nextFoglalasId++);
        FOGLALASOK.add(ujFoglalas);

        return new FoglalasiEredmeny(true, "Foglalás rögzítve! Azonosító: " + ujFoglalas.getFoglalasId());
    }

    @Override
    public AuthEredmeny megprobalRegisztralni(String felhasznalonev, String jelszo) {
        boolean foglalt = FELHASZNALOK.stream()
                .anyMatch(f -> f.getFelhasznaloNev().equalsIgnoreCase(felhasznalonev));

        if (foglalt) {
            return new AuthEredmeny(false, "Hiba: Ez a felhasználónév már foglalt!");
        }
        if (jelszo == null || jelszo.isEmpty()) {
            return new AuthEredmeny(false, "Hiba: A jelszó nem lehet üres!");
        }
        Felhasznalo ujFelhasznalo = new Felhasznalo();
        ujFelhasznalo.setFelhasznaloId(nextFelhasznaloId++);
        ujFelhasznalo.setFelhasznaloNev(felhasznalonev);
        ujFelhasznalo.setJelszoHASH(jelszo);
        FELHASZNALOK.add(ujFelhasznalo);

        return new AuthEredmeny(true, "Regisztráció sikeres!");
    }

    @Override
    public AuthEredmeny megprobalBejelentkezni(String felhasznalonev, String jelszo) {
        Felhasznalo talaltFelhasznalo = FELHASZNALOK.stream()
                .filter(f -> f.getFelhasznaloNev().equalsIgnoreCase(felhasznalonev))
                .findFirst()
                .orElse(null);

        if (talaltFelhasznalo == null) {
            return new AuthEredmeny(false, "Hiba: Hibás felhasználónév!");
        }

        if (talaltFelhasznalo.getJelszoHASH().equals(jelszo)) {
            return new AuthEredmeny(true, "Bejelentkezés sikeres!", talaltFelhasznalo);
        } else {
            return new AuthEredmeny(false, "Hiba: Hibás jelszó!");
        }
    }

    @Override
    public ErtekelesEredmeny megprobalErtekelni(int filmId, int felhasznaloId, int pontszam, String szovegesErtekeles) {

        boolean filmExtist = FILMEK.stream().anyMatch(f -> f.getFilmId() == filmId);
        if (!filmExtist) {
            return new ErtekelesEredmeny(false, "Hiba: A film (ID: " + filmId + ") nem létezik.");
        }

        boolean userExist = FELHASZNALOK.stream().anyMatch(u -> u.getFelhasznaloId() == felhasznaloId);
        if (!userExist) {
            return new ErtekelesEredmeny(false, "Hiba: A felhasználó (ID: " + felhasznaloId + ") nem létezik.");
        }

        boolean marErtekelte = ERTEKELESEK.stream()
                .anyMatch(e -> e.getFilmId() == filmId && e.getFelhasznaloId() == felhasznaloId);

        if (marErtekelte) {
            return new ErtekelesEredmeny(false, "Hiba: Ezt a filmet már értékelted!");
        }

        if (pontszam < 1 || pontszam > 5) {
            return new ErtekelesEredmeny(false, "Hiba: A pontszámnak 1 és 5 között kell lennie.");
        }

        Ertekeles ujErtekeles = new Ertekeles(filmId, felhasznaloId, pontszam, szovegesErtekeles);
        ujErtekeles.setErtekelesId(nextErtekelesId++);
        ERTEKELESEK.add(ujErtekeles);

        return new ErtekelesEredmeny(true, "Értékelés sikeresen rögzítve!");
    }
}