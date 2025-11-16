package com.inventario.clientes;

import com.inventario.excepciones.DatoInvalidoException;

public class Cliente {

    private int id;
    private String nombre;
    private String email;
    private String telefono;

    // --- Constructor con ID (usado al obtener de la base de datos) ---
    public Cliente(int id, String nombre, String email, String telefono) throws DatoInvalidoException {
        validarNombre(nombre);
        validarEmail(email);
        validarTelefono(telefono);

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    // --- Constructor sin ID (usado al insertar) ---
    public Cliente(String nombre, String email, String telefono) throws DatoInvalidoException {
        validarNombre(nombre);
        validarEmail(email);
        validarTelefono(telefono);

        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    /**
     * Valida el nombre del cliente
     * 
     * @param nombre
     */
    private void validarNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del cliente no puede estar vacío.");
        }
    }

    private void validarEmail(String email) throws DatoInvalidoException {
        if (email == null || email.trim().isEmpty()) {
            throw new DatoInvalidoException("El email no puede estar vacío.");
        }
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
            throw new DatoInvalidoException("El email no es válido.");
        }
    }

    private void validarTelefono(String telefono) throws DatoInvalidoException {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new DatoInvalidoException("El teléfono no puede estar vacío.");
        }
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws DatoInvalidoException {
        validarNombre(nombre);
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws DatoInvalidoException {
        validarEmail(email);
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) throws DatoInvalidoException {
        validarTelefono(telefono);
        this.telefono = telefono;
    }
}
