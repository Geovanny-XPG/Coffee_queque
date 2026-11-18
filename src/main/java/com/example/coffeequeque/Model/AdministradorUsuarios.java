package com.example.coffeequeque.Model;

import com.example.coffeequeque.structures.ListaUsuarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class AdministradorUsuarios {
    private static final int LONGITUD_MINIMA_PASSWORD = 6;
    private static final String SEPARADOR = "|";

    private final ListaUsuarios usuarios;
    private final File archivo;

    public AdministradorUsuarios() {
        this.usuarios = new ListaUsuarios();
        // Carpeta del usuario del sistema: no se pierde al recompilar el proyecto
        this.archivo = new File(System.getProperty("user.home"), "coffeequeue_usuarios.txt");
    }

    // ---------- Registro y autenticación ----------

    public Cliente registrarCliente(String nombre, String correo, String password,
                                    String confirmacion, String matricula) {
        if (nombre == null || nombre.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Completa todos los campos obligatorios.");
        }

        String mat = (matricula == null) ? "" : matricula.trim();
        if (contieneSeparador(nombre) || contieneSeparador(correo)
                || contieneSeparador(password) || contieneSeparador(mat)) {
            throw new IllegalArgumentException("No uses el carácter '|' en los campos.");
        }

        String correoNormalizado = correo.trim().toLowerCase();
        int arroba = correoNormalizado.indexOf('@');
        if (arroba < 1 || correoNormalizado.indexOf('.', arroba) < arroba + 2
                || correoNormalizado.endsWith(".")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
        if (usuarios.buscarPorCorreo(correoNormalizado) != null) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese correo.");
        }
        if (password.length() < LONGITUD_MINIMA_PASSWORD) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos " + LONGITUD_MINIMA_PASSWORD + " caracteres.");
        }
        if (!password.equals(confirmacion)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        Cliente nuevo = new Cliente(usuarios.siguienteId(), nombre.trim(),
                correoNormalizado, password, false, mat);
        usuarios.agregarUsuario(nuevo);
        guardarEnArchivo();
        return nuevo;
    }

    // Alta interna (cajeros precargados). No se expone en el registro público.
    public void agregarUsuarioInterno(Usuario usuario) {
        usuarios.agregarUsuario(usuario);
        guardarEnArchivo();
    }

    public boolean existeCorreo(String correo) {
        return usuarios.buscarPorCorreo(correo.trim()) != null;
    }

    public int siguienteId() {
        return usuarios.siguienteId();
    }

    public Usuario autenticar(String correo, String password) {
        if (correo == null || password == null) return null;
        Usuario u = usuarios.buscarPorCorreo(correo.trim());
        if (u != null && u.getPassword().equals(password)) {
            u.setLog(true);
            return u;
        }
        return null;
    }

    // ---------- Persistencia ----------

    // Formato: una línea por usuario
    //   CLIENTE|id|nombre|correo|password|matricula
    //   CAJERO|id|nombre|correo|password|numeroCaja
    public void cargarDesdeArchivo() {
        if (!archivo.exists()) return;

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                // El -1 conserva campos vacíos al final (matrícula opcional)
                String[] c = linea.split("\\|", -1);
                if (c.length != 6) continue;   // línea dañada: se ignora

                try {
                    int id = Integer.parseInt(c[1]);
                    if ("CLIENTE".equals(c[0])) {
                        usuarios.agregarUsuario(new Cliente(id, c[2], c[3], c[4], false, c[5]));
                    } else if ("CAJERO".equals(c[0])) {
                        usuarios.agregarUsuario(
                                new Cajero(id, c[2], c[3], c[4], false, Integer.parseInt(c[5])));
                    }
                } catch (NumberFormatException e) {
                    // línea con número inválido: se ignora
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo de usuarios: " + e.getMessage());
        }
    }

    public void guardarEnArchivo() {
        try (BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            for (int i = 0; i < usuarios.getTamanio(); i++) {
                Usuario u = usuarios.obtener(i);
                String tipo;
                String extra;
                if (u instanceof Cajero) {
                    tipo = "CAJERO";
                    extra = String.valueOf(((Cajero) u).getNumeroCaja());
                } else if (u instanceof Cliente) {
                    tipo = "CLIENTE";
                    extra = ((Cliente) u).getMatricula();
                } else {
                    continue;
                }
                escritor.write(tipo + SEPARADOR + u.getId() + SEPARADOR + u.getNombre() + SEPARADOR
                        + u.getCorreo() + SEPARADOR + u.getPassword() + SEPARADOR + extra);
                escritor.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el usuario en el archivo.", e);
        }
    }

    private boolean contieneSeparador(String texto) {
        return texto != null && texto.contains(SEPARADOR);
    }
}