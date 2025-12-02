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
        // --- FILM 1 ---
        Film film1 = new Film();
        film1.setFilmId(nextFilmId++);
        film1.setCim("Interstellar - Csillagok között");
        film1.setJatekido(169);
        film1.setMufaj("Sci-fi");
        film1.setKorhatar(12);
        film1.setFilmLeiras("Egy csapat felfedező egy újonnan felfedezett féregjáraton utazik keresztül, hogy túllépjen az emberi űrutazás korlátain.");
        film1.setPoszterUrl("https://image.tmdb.org/t/p/w600_and_h900_bestv2/6KiSSndIMLj1swkpPNq2lYppDVQ.jpg");
        FILMEK.add(film1);

        // --- FILM 2 ---
        Film film2 = new Film();
        film2.setFilmId(nextFilmId++);
        film2.setCim("A Grand Budapest Hotel");
        film2.setJatekido(99);
        film2.setMufaj("Vígjáték");
        film2.setKorhatar(16);
        film2.setFilmLeiras("Egy neves európai szálloda portásának és hű inasának kalandjai a világháborúk közötti Európában.");
        film2.setPoszterUrl("https://image.tmdb.org/t/p/w600_and_h900_bestv2/fEHXKrdNjtf5R7YAA0ssVZLJLOa.jpg");
        FILMEK.add(film2);

        // --- FILM 3 ---
        Film film3 = new Film();
        film3.setFilmId(nextFilmId++);
        film3.setCim("Mad Max: A harag útja");
        film3.setJatekido(120);
        film3.setMufaj("Akció");
        film3.setKorhatar(18);
        film3.setFilmLeiras("Egy poszt-apokaliptikus sivatagi pusztaságban Max Rockatansky egy női felkelőhöz csatlakozik.");
        film3.setPoszterUrl("https://image.tmdb.org/t/p/w600_and_h900_bestv2/u6nSMyGp9Cc9TKMDDxxTZPGGpiV.jpg");
        FILMEK.add(film3);

        // --- ÜLŐHELYEK GENERÁLÁSA ---
        for (int i = 1; i <= 10; i++) {
            Ulohely u = new Ulohely(1, "A", i);
            u.setUlohelyId(nextUlohelyId++);
            ULOHELYEK.add(u);
        }

        // --- IDŐPONTOK GENERÁLÁSA (Mai napra) ---
        LocalDateTime now = LocalDateTime.now();

        // Interstellar vetítések
        IDOPONTOK.add(new Idopont(now.withHour(17).withMinute(45), film1.getFilmId(), 1, 2500));
        IDOPONTOK.add(new Idopont(now.withHour(20).withMinute(30), film1.getFilmId(), 5, 2800));

        // Grand Budapest vetítések
        IDOPONTOK.add(new Idopont(now.withHour(16).withMinute(0), film2.getFilmId(), 3, 2200));

        // Mad Max vetítések
        IDOPONTOK.add(new Idopont(now.withHour(19).withMinute(0), film3.getFilmId(), 6, 2600));
        IDOPONTOK.add(new Idopont(now.withHour(22).withMinute(15), film3.getFilmId(), 1, 2600));

        // ID-k beállítása az időpontoknak
        for (Idopont i : IDOPONTOK) {
            i.setIdopontId(nextIdopontId++);
        }
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

        return new ErtekelesEredmeny(true, "Értékelés rögzítve.");
    }

    @Override
    public List<Foglalas> getFoglalasokFelhasznalonak(int felhasznaloId) {
        return FOGLALASOK.stream()
                .filter(foglalas -> foglalas.getFelhasznaloId() == felhasznaloId)
                .collect(Collectors.toList());
    }


}