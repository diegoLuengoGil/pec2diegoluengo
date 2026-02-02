package com.inventario.model;

import com.inventario.excepciones.DatoInvalidoException;
import jakarta.persistence.*;

/**
 * Clase que representa un cliente.
 */
@Entity
@Table(name = "Cliente")
public class Cliente {

    /**
     * ID del cliente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private int id;
    /**
     * Nombre del cliente.
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;
    /**
     * Email del cliente.
     */
    @Column(name = "email", nullable = false)
    private String email;
    /**
     * Teléfono del cliente.
     */
    @Column(name = "telefono", nullable = false)
    private String telefono;
    /**
     * Dinero del cliente.
     */
    @Column(name = "dinero", nullable = false)
    private double dinero;

    /**
     * Constructor vacío para JPA.
     */
    public Cliente() {
    }

    /**
     * Constructor con ID (usado al obtener de la base de datos).
     *
     * @param id       ID del cliente.
     * @param nombre   Nombre del cliente.
     * @param email    Email del cliente.
     * @param telefono Teléfono del cliente.
     * @param dinero   Dinero del cliente.
     * @throws DatoInvalidoException Si algún dato es inválido.
     */
    public Cliente(int id, String nombre, String email, String telefono, double dinero) throws DatoInvalidoException {
        validarNombre(nombre);
        validarEmail(email);
        validarTelefono(telefono);
        validarDinero(dinero);

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.dinero = dinero;
    }

    /**
     * Constructor sin ID (usado al insertar).
     *
     * @param nombre   Nombre del cliente.
     * @param email    Email del cliente.
     * @param telefono Teléfono del cliente.
     * @param dinero   Dinero del cliente.
     * @throws DatoInvalidoException Si algún dato es inválido.
     */
    public Cliente(String nombre, String email, String telefono, double dinero) throws DatoInvalidoException {
        validarNombre(nombre);
        validarEmail(email);
        validarTelefono(telefono);
        validarDinero(dinero);

        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.dinero = dinero;
    }

    /**
     * Valida el nombre del cliente.
     *
     * @param nombre Nombre del cliente.
     * @throws DatoInvalidoException Si el nombre es inválido.
     */
    private void validarNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del cliente no puede estar vacío.");
        }
    }

    /**
     * Valida el email del cliente.
     *
     * @param email Email del cliente.
     * @throws DatoInvalidoException Si el email es inválido.
     */
    private void validarEmail(String email) throws DatoInvalidoException {
        if (email == null || email.trim().isEmpty()) {
            throw new DatoInvalidoException("El email no puede estar vacío.");
        }
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
            throw new DatoInvalidoException("El email no es válido.");
        }
    }

    /**
     * Valida el teléfono del cliente.
     *
     * @param telefono Teléfono del cliente.
     * @throws DatoInvalidoException Si el teléfono es inválido.
     */
    private void validarTelefono(String telefono) throws DatoInvalidoException {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new DatoInvalidoException("El teléfono no puede estar vacío.");
        }
    }

    /**
     * Valida el dinero del cliente.
     *
     * @param dinero Dinero del cliente.
     * @throws DatoInvalidoException Si el dinero es inválido.
     */
    private void validarDinero(double dinero) throws DatoInvalidoException {
        if (dinero < 0.01) {
            throw new DatoInvalidoException("El dinero no puede ser negativo.");
        }
    }

    /**
     * Obtiene el ID del cliente.
     *
     * @return ID del cliente.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del cliente.
     *
     * @param id ID del cliente.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del cliente.
     *
     * @return Nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     *
     * @param nombre Nombre del cliente.
     * @throws DatoInvalidoException Si el nombre es inválido.
     */
    public void setNombre(String nombre) throws DatoInvalidoException {
        validarNombre(nombre);
        this.nombre = nombre;
    }

    /**
     * Obtiene el email del cliente.
     *
     * @return Email del cliente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del cliente.
     *
     * @param email Email del cliente.
     * @throws DatoInvalidoException Si el email es inválido.
     */
    public void setEmail(String email) throws DatoInvalidoException {
        validarEmail(email);
        this.email = email;
    }

    /**
     * Obtiene el teléfono del cliente.
     *
     * @return Teléfono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del cliente.
     *
     * @param telefono Teléfono del cliente.
     * @throws DatoInvalidoException Si el teléfono es inválido.
     */
    public void setTelefono(String telefono) throws DatoInvalidoException {
        validarTelefono(telefono);
        this.telefono = telefono;
    }

    /**
     * Obtiene el dinero del cliente.
     *
     * @return Dinero del cliente.
     */
    public double getDinero() {
        return dinero;
    }

    /**
     * Establece el dinero del cliente.
     *
     * @param dinero Dinero del cliente.
     */
    public void setDinero(double dinero) {
        this.dinero = dinero;
    }

}
