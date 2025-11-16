package com.inventario.productos;

import java.sql.Connection;

import conexion.ConexionBBDD;

public class ProductosBBDD {
    public static boolean insertarProducto(Producto producto) {

        String sql = "INSERT INTO producto (nombre, descripcion, precio_venta, stock) VALUES (?, ?, ?, ?)";

        boolean exito = false;

        try (Connection con = ConexionBBDD.obtenerConexion();
             var ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                exito = true;
            }
        } catch (Exception e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
        }
        return exito; // Retorna true si la inserción fue exitosa
    }
}
