package org.example.controller;

import org.example.model.Film;
import org.example.view.MainView;
import org.example.service.MoziService;
import org.example.service.MockMoziService;

import javafx.collections.FXCollections;
import java.util.List;

public class MainController {

    private MainView view;
    private MoziService service;

    public MainController(MainView view) {
        this.view = view;
        this.service = new MockMoziService();

        loadFilmsFromService();
        setupBindings();
    }

    private void loadFilmsFromService() {
        List<Film> filmek = service.getMindenFilm();
        view.getFilmLista().setItems(FXCollections.observableArrayList(
                filmek.stream().map(Film::getCim).toList()
        ));
    }

    private void setupBindings() {
        view.getFilmLista().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            // A frontendesek ide írják majd a logikát, ami betölti
            // a kiválasztott film részleteit és időpontjait.
            System.out.println("Kiválasztott film: " + newVal);
        });

        view.getFoglalasGomb().setOnAction(e -> {
            // A frontendesek ide írják majd a logikát, ami összegyűjti
            // a kiválasztott időpontot, széket, felhasználót és meghívja a servicet.
            System.out.println("\n--- Foglalás Gomb Megnyomva ---");
            // Pl: service.megprobalFoglalni(kivalasztottIdopont.getId(), kivalasztottSzek.getId(), ...);
        });
    }
}