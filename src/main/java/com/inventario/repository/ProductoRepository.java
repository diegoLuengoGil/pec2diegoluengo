package com.inventario.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.inventario.bbdd.ConexionBBDD;
import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.model.Producto;

/**
 * Repository for Producto data access operations.
 */
public class ProductoRepository {

    public int contarProductos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM producto";
        int count = 0;

        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        return count;
    }

    public List<Producto> buscarPorCampo(String campo, Object valor) throws SQLException, DatoInvalidoException {
        // Validation of field name to prevent SQL Injection could be here, strict
        // whitelist
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock FROM producto WHERE " + campo + " = ?";

        List<Producto> productos = new ArrayList<>();

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, valor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"));
                    productos.add(p);
                }
            }
        }
        return productos;
    }

    public boolean actualizarProductoCampo(int idProducto, String campo, Object valor) throws SQLException {
        boolean actualizado = false;
        String sql = "UPDATE producto SET " + campo + " = ? WHERE id_producto = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, valor);
            ps.setInt(2, idProducto);

            int filasAfectadas = ps.executeUpdate();
            actualizado = filasAfectadas > 0;
        }
        return actualizado;
    }

    public boolean eliminarProducto(int idProducto) throws SQLException {
        boolean eliminado = false;
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            eliminado = ps.executeUpdate() > 0;
        }
        return eliminado;
    }

    public List<Producto> obtenerProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock FROM producto";

        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                try {
                    Producto p = new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"));

                    productos.add(p);
                } catch (DatoInvalidoException e) {
                    System.err.println("Database inconsistency for Product ID " + rs.getInt("id_producto") + ": "
                            + e.getMessage());
                }
            }
        }
        return productos;
    }

    public boolean insertarProducto(Producto producto) throws SQLException {
        boolean insertado = false;
        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());

            insertado = ps.executeUpdate() > 0;
        }
        return insertado;
    }
}
