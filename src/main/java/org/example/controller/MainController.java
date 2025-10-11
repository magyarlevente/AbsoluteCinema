package org.example.controller;

import org.example.model.Film;
import org.example.view.MainView;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    private MainView view;
    private List<Film> filmek;

    public MainController(MainView view) {
        this.view = view;
        loadFilms();
        setupBindings();
    }

    private void loadFilms() {
        // Példa filmadatok
        filmek = new ArrayList<>();
        filmek.add(new Film("Mozi1", List.of("12:00", "14:30", "17:00")));
        filmek.add(new Film("Mozi2", List.of("13:00", "15:30", "18:00")));

        view.getFilmLista().setItems(FXCollections.observableArrayList(
                filmek.stream().map(Film::getCim).toList()
        ));
    }

    private void setupBindings() {
        view.getFilmLista().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Kiválasztott film: " + newVal);
        });

        view.getFoglalasGomb().setOnAction(e -> {
            System.out.println("Foglalás gomb megnyomva");
        });
    }
}

