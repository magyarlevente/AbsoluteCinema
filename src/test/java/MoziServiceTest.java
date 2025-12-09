package org.example.service;

import org.example.database.DatabaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

// testeli a regisztraciot uj adatokkal, testeli a hianyos regisztraciot, hogy a rendszer elutasitja -e
// testeli a dupla regisztracio elutasitasat

public class MoziServiceTest {

    private final MoziService service = new DatabaseMoziService();

    @BeforeAll
    public static void setup() {
        DatabaseManager.initializeDatabase();
    }

    @Test
    public void testSikeresRegisztracio() {
        String randomUser = "User_" + UUID.randomUUID().toString().substring(0, 8);
        String jelszo = "titkos123";

        AuthEredmeny eredmeny = service.megprobalRegisztralni(randomUser, jelszo);

        assertTrue(eredmeny.isSiker());
        assertEquals("Sikeres regisztráció!", eredmeny.getUzenet());
    }

    @Test
    public void testHianyosRegisztracio() {
        AuthEredmeny eredmeny = service.megprobalRegisztralni("", "jelszo");

        assertFalse(eredmeny.isSiker());
        assertEquals("Hiányzó adatok", eredmeny.getUzenet());
    }

    @Test
    public void testFoglaltFelhasznalonev() {
        String user = "DuplaUser_" + UUID.randomUUID().toString().substring(0, 5);
        String pass = "1234";

        service.megprobalRegisztralni(user, pass);

        AuthEredmeny masodikProba = service.megprobalRegisztralni(user, "masikJelszo");

        assertFalse(masodikProba.isSiker());
        assertEquals("Foglalt felhasználónév.", masodikProba.getUzenet());
    }

    @Test
    public void testSikeresBelepes() {
        String user = "LoginUser_" + UUID.randomUUID().toString().substring(0, 5);
        String pass = "password";

        service.megprobalRegisztralni(user, pass);

        AuthEredmeny loginEredmeny = service.megprobalBejelentkezni(user, pass);

        assertTrue(loginEredmeny.isSiker());
        assertNotNull(loginEredmeny.getFelhasznalo());
        assertEquals(user, loginEredmeny.getFelhasznalo().getFelhasznaloNev());
    }
}