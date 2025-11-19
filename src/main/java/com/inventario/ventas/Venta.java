package com.inventario.ventas;

/**
 * Clase que representa una venta.
 */
public class Venta {
    /** Identificador de la venta */
    private int id;

    /** Identificador del cliente */
    private int idCliente;

    /**
     * Constructor de la clase.
     * 
     * @param id        Id de la venta en la base de datos.
     * @param idCliente Id del cliente.
     */
    public Venta(int id, int idCliente) {
        this.id = id;
        this.idCliente = idCliente;
    }

    /**
     * Constructor de la clase.
     * 
     * @param idCliente Id del cliente.
     */
    public Venta(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Obtiene el identificador de la venta.
     * 
     * @return El identificador de la venta.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador de la venta.
     * 
     * @param id El identificador de la venta.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el identificador del cliente.
     * 
     * @return El identificador del cliente.
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Establece el identificador del cliente.
     * 
     * @param idCliente El identificador del cliente.
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

}
