package com.inventario.ventas;

import com.inventario.excepciones.DatoInvalidoException;

public class DetalleVenta {
    private int id;
    private int idVenta;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;

    public DetalleVenta(int id, int idVenta, int idProducto, int cantidad, double precioUnitario)
            throws DatoInvalidoException {
        validarCantidad(cantidad);

        this.id = id;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public DetalleVenta(int idVenta, int idProducto, int cantidad, double precioUnitario) throws DatoInvalidoException {
        validarCantidad(cantidad);

        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    private void validarCantidad(int cantidad) throws DatoInvalidoException {
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor que cero.");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}