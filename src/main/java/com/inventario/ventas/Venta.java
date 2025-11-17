package com.inventario.ventas;

public class Venta {
    private int id;
    private int idCliente;

    public Venta(int id, int idCliente, String fecha) {
        this.id = id;
        this.idCliente = idCliente;
    }

    public Venta(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    
}
