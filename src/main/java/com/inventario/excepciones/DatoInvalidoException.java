package com.inventario.excepciones;

/**
 * Clase que gestiona las excepciones de datos inválidos.
 */
public class DatoInvalidoException extends Exception {
    /**
     * Constructor que recibe un mensaje de error.
     *
     * @param mensaje El mensaje de error.
     */
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
