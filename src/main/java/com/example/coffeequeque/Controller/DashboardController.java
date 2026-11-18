package com.example.coffeequeque.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class DashboardController {

    // Componente de pestañas principal inyectado desde el FXML
    @FXML private TabPane tabPanePrincipal;
    
    // Controladores de las diferentes vistas secundarias incrustadas en las pestañas
    @FXML private CocinaController cocinaController;
    @FXML private DespachoController despachoController;
    @FXML private HistorialController historialController;

    // Método que se ejecuta automáticamente al iniciar el controlador
    @FXML
    public void initialize() {
        // Verificamos que el contenedor de pestañas no sea nulo
        if (tabPanePrincipal != null) {
            // Añadimos un listener para detectar cada vez que el usuario cambia de pestaña
            tabPanePrincipal.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                // Actualizamos automáticamente la vista correspondiente según la pestaña seleccionada
                if (cocinaController != null) cocinaController.renderizarComandas();
                if (despachoController != null) despachoController.actualizarVista();
                if (historialController != null) historialController.actualizarHistorialUI();
            });
        }
    }
}
