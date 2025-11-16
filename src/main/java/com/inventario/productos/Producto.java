package com.inventario.productos;

import com.inventario.excepciones.DatoInvalidoException;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    public Producto(int idProducto, String nombre, String descripcion, double precio, int stock) throws DatoInvalidoException {

        validarNombre(nombre);
        validarPrecio(precio);
        validarStock(stock);

        this.id = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;

    }

    public Producto(String nombre, String descripcion, double precio, int stock) throws DatoInvalidoException {

        validarNombre(nombre);
        validarPrecio(precio);
        validarStock(stock);

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;

    }

    private void validarNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del producto no puede estar vacio.");
        }
    }

    private void validarPrecio(double precio) throws DatoInvalidoException {
        if (precio < 0) {
            throw new DatoInvalidoException("El precio del producto debe ser un valor válido y no puede ser nulo.");
        }
    }
    
    private void validarStock(int stock) throws DatoInvalidoException {
        if (stock < 0) {
            throw new DatoInvalidoException("El stock no puede ser negativo.");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre)throws DatoInvalidoException {
        validarNombre(nombre);
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) throws DatoInvalidoException {
        validarPrecio(precio);
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) throws DatoInvalidoException {
        validarStock(stock);
        this.stock = stock;
    }

}
