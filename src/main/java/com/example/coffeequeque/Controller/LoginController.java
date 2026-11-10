package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Cajero;
import com.example.coffeequeque.Model.Cliente;
import com.example.coffeequeque.Model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensajeError;

    @FXML
    private void handleLogin() {
        String correo = txtCorreo.getText().trim();
        String password = txtPassword.getText().trim();

        // Usuarios de prueba simulados (puedes conectarlo a tu estructura o base de datos)
        Usuario adminPrueba = new Cajero(1, "pruebaAdmin", "admin@coffee.com", "1234", false, 1);
        Usuario clientePrueba = new Cliente(2, "pruebaCliente", "cliente@coffee.com", "1234", false, "MAT-2026");

        // Validar Administrador / Cajero
        adminPrueba.login(correo, password);
        if (adminPrueba.isLog()) {
            abrirDashboard("Administrador / Operaciones", "/com/example/coffeequeque/dashboard-view.fxml");
            return;
        }

        // Validar Cliente
        clientePrueba.login(correo, password);
        if (clientePrueba.isLog()) {
            abrirDashboard("Catálogo de Cliente", "/com/example/coffeequeque/catalogo-view.fxml");
            return;
        }

        // Si ningún usuario coincide
        lblMensajeError.setText("Correo o contraseña incorrectos.");
    }

    private void abrirDashboard(String titulo, String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/coffeequeque/dashboard-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("CoffeeQueue - " + titulo);
            stage.setScene(new Scene(loader.load(), 900, 650));
            stage.show();

            // Cerrar ventana de login actual
            Stage currentStage = (Stage) txtCorreo.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            lblMensajeError.setText("Error al cargar la vista: " + e.getMessage());
        }
    }
}
