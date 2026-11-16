package com.example.coffeequeque.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class DashboardController {

    @FXML private TabPane tabPanePrincipal;
    @FXML private CocinaController cocinaController;
    @FXML private DespachoController despachoController;
    @FXML private HistorialController historialController;

    @FXML
    public void initialize() {
        if (tabPanePrincipal != null) {
            tabPanePrincipal.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                if (cocinaController != null) cocinaController.renderizarComandas();
                if (despachoController != null) despachoController.actualizarVista();
                if (historialController != null) historialController.actualizarHistorialUI();
            });
        }
    }
}