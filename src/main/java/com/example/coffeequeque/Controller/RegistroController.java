package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.SesionCafeteria;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistroController {

    // Campos del formulario inyectados desde registro-view.fxml
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtMatricula;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmar;

    // Etiqueta donde se muestran los errores de validación
    @FXML private Label lblMensaje;

    /**
     * Se ejecuta al pulsar "Crear Cuenta". Las validaciones (campos vacíos, correo,
     * duplicados, contraseña) las hace AdministradorUsuarios y avisa con una excepción.
     */
    @FXML
    private void handleRegistrar() {
        try {
            // La contraseña y su confirmación se limpian con trim() para que
            // coincidan con lo que el login compara (también usa trim())
            SesionCafeteria.getAdministradorUsuarios().registrarCliente(
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtPassword.getText().trim(),
                    txtConfirmar.getText().trim(),
                    txtMatricula.getText());

            // Si no hubo excepción, la cuenta quedó guardada en memoria y en el archivo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cuenta creada");
            alert.setHeaderText(null);
            alert.setContentText("Tu cuenta fue creada. Ya puedes iniciar sesión.");
            alert.showAndWait();

            // Regresa al login para que el usuario inicie sesión con su cuenta nueva
            handleVolver();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            // IllegalArgumentException: dato inválido (correo repetido, contraseñas distintas, etc.)
            // IllegalStateException: no se pudo escribir el archivo de usuarios
            lblMensaje.setText(ex.getMessage());
        }
    }

    /**
     * Vuelve a la pantalla de inicio de sesión reemplazando la escena de la ventana actual.
     * Se usa tanto en el botón "Volver" como tras registrar con éxito.
     */
    @FXML
    private void handleVolver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/coffeequeque/login-view.fxml"));
            // Se obtiene la ventana a partir de cualquier campo del formulario
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            // Si el FXML no se encuentra o está mal formado, se avisa en pantalla
            lblMensaje.setText("Error al cargar la vista: " + e.getMessage());
        }
    }
}