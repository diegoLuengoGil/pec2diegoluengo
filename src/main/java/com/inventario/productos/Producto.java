package com.inventario.productos;

import java.math.BigDecimal;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    public Producto(int idProducto, String nombre, String descripcion, double precio, int stock) {

        // Validación de restricciones de la BD (stock >= 0 y no nulos)
        // if (nombre == null || nombre.trim().isEmpty()) {
        //     throw new IllegalArgumentException("El nombre del producto no puede ser nulo.");
        // }
        // if (precio < 0) {
        //     throw new IllegalArgumentException("El precio del producto debe ser un valor válido y no puede ser nulo.");
        // }
        // if (stock < 0) {
        //     throw new IllegalArgumentException("El stock no puede ser negativo.");
        // }

        this.id = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;

    }

    public Producto(String nombre, String descripcion, double precio, int stock) {


        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacio.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio del producto debe ser un valor válido y no puede ser nulo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;

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

    public void setNombre(String nombre) {
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

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    

}
