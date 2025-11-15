package org.example.view;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView {

    private BorderPane root;
    private ListView<String> filmLista;
    private VBox rightPane;
    private Button foglalasGomb;

    public MainView() {
        root = new BorderPane();
        setupUI();
    }

    private void setupUI() {
        filmLista = new ListView<>();
        root.setLeft(filmLista);

        rightPane = new VBox(10);
        foglalasGomb = new Button("Foglalás");
        rightPane.getChildren().add(foglalasGomb);

        root.setCenter(rightPane);
    }

    public BorderPane getRoot() {
        return root;
    }

    public ListView<String> getFilmLista() {
        return filmLista;
    }

    public Button getFoglalasGomb() {
        return foglalasGomb;
    }
}