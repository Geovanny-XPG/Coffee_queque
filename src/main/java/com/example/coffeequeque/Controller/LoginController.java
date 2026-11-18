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

    // Componentes de la interfaz gráfica de inicio de sesión inyectados desde el FXML
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensajeError;

    // Método que se ejecuta al intentar iniciar sesión (vinculado al botón de login)
    @FXML
    private void handleLogin() {
        // Obtenemos y limpiamos los espacios en blanco del correo y contraseña ingresados
        String correo = txtCorreo.getText().trim();
        String password = txtPassword.getText().trim();

        // Usuarios de prueba simulados (puedes conectarlo a tu estructura o base de datos)
        Usuario adminPrueba = new Cajero(1, "pruebaAdmin", "admin@coffee.com", "1234", false, 1);
        Usuario clientePrueba = new Cliente(2, "pruebaCliente", "cliente@coffee.com", "1234", false, "MAT-2026");

        // Intentamos validar las credenciales como Administrador / Cajero
        adminPrueba.login(correo, password);
        if (adminPrueba.isLog()) {
            // Si el login es exitoso, abrimos el dashboard general de operaciones
            abrirDashboard("Administrador / Operaciones", "/com/example/coffeequeque/dashboard-view.fxml");
            return;
        }

        // Si no es administrador, intentamos validar como Cliente
        clientePrueba.login(correo, password);
        if (clientePrueba.isLog()) {
            // Si el login es exitoso, abrimos la vista del catálogo para clientes
            abrirDashboard("Catálogo de Cliente", "/com/example/coffeequeque/catalogo-view.fxml");
            return;
        }

        // Si ningún usuario coincide con las credenciales, mostramos un mensaje de error en la UI
        lblMensajeError.setText("Correo o contraseña incorrectos.");
    }

    // Método auxiliar para cargar la nueva ventana (Dashboard o Catálogo) y cerrar la ventana actual de login
    private void abrirDashboard(String titulo, String rutaFxml) {
        try {
            // Cargamos el archivo FXML correspondiente a la siguiente vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/coffeequeque/dashboard-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("CoffeeQueue - " + titulo);
            stage.setScene(new Scene(loader.load(), 900, 650));
            stage.show();

            // Cerramos la ventana de login actual para finalizar la transición
            Stage currentStage = (Stage) txtCorreo.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Mostramos un mensaje en caso de que ocurra un error al cargar el archivo FXML
            lblMensajeError.setText("Error al cargar la vista: " + e.getMessage());
        }
    }
}
