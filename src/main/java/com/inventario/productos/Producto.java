package com.inventario.productos;

import java.math.BigDecimal;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    public Producto(int idProducto, String nombre, String descripcion, BigDecimal precio, int stock) {

        // Validación de restricciones de la BD (stock >= 0 y no nulos)
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo.");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio del producto debe ser un valor válido y no puede ser nulo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        this.id = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;

    }

}
