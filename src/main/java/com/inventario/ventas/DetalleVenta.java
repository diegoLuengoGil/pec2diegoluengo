package com.inventario.ventas;

import com.inventario.excepciones.DatoInvalidoException;

/**
 * Clase que representa un detalle de venta.
 */
public class DetalleVenta {
    /**
     * ID del detalle de venta.
     */
    private int id;
    /**
     * ID de la venta.
     */
    private int idVenta;
    /**
     * ID del producto.
     */
    private int idProducto;
    /**
     * Cantidad del producto.
     */
    private int cantidad;
    /**
     * Precio unitario del producto.
     */
    private double precioUnitario;

    /**
     * Constructor para crear un nuevo detalle de venta.
     * 
     * @param id             ID del detalle de venta.
     * @param idVenta        ID de la venta.
     * @param idProducto     ID del producto.
     * @param cantidad       Cantidad del producto.
     * @param precioUnitario Precio unitario del producto.
     * @throws DatoInvalidoException Si la cantidad es inválida.
     */
    public DetalleVenta(int id, int idVenta, int idProducto, int cantidad, double precioUnitario)
            throws DatoInvalidoException {
        validarCantidad(cantidad);

        this.id = id;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Constructor para crear un nuevo detalle de venta.
     * 
     * @param idVenta        ID de la venta.
     * @param idProducto     ID del producto.
     * @param cantidad       Cantidad del producto.
     * @param precioUnitario Precio unitario del producto.
     * @throws DatoInvalidoException Si la cantidad es inválida.
     */
    public DetalleVenta(int idVenta, int idProducto, int cantidad, double precioUnitario) throws DatoInvalidoException {
        validarCantidad(cantidad);

        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Valida que la cantidad sea mayor que cero.
     * 
     * @param cantidad Cantidad del producto.
     * @throws DatoInvalidoException Si la cantidad es inválida.
     */
    private void validarCantidad(int cantidad) throws DatoInvalidoException {
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor que cero.");
        }
    }

    /**
     * Obtiene el ID del detalle de venta.
     * 
     * @return ID del detalle de venta.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del detalle de venta.
     * 
     * @param id ID del detalle de venta.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el ID de la venta.
     * 
     * @return ID de la venta.
     */
    public int getIdVenta() {
        return idVenta;
    }

    /**
     * Establece el ID de la venta.
     * 
     * @param idVenta ID de la venta.
     */
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    /**
     * Obtiene el ID del producto.
     * 
     * @return ID del producto.
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el ID del producto.
     * 
     * @param idProducto ID del producto.
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene la cantidad del producto.
     * 
     * @return Cantidad del producto.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad del producto.
     * 
     * @param cantidad Cantidad del producto.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario del producto.
     * 
     * @return Precio unitario del producto.
     */
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece el precio unitario del producto.
     * 
     * @param precioUnitario Precio unitario del producto.
     */
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}