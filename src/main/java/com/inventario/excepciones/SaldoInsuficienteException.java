package com.inventario.excepciones;

/**
 * Excepción lanzada cuando un cliente no tiene saldo suficiente para completar
 * una venta.
 */
public class SaldoInsuficienteException extends Exception {

    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
