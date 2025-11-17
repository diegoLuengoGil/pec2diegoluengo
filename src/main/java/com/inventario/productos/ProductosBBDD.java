package com.inventario.productos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.inventario.conexion.ConexionBBDD;
import com.inventario.excepciones.DatoInvalidoException;

public class ProductosBBDD {

        public static int contarProductos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM producto";
        int count = 0;

        // Asumiendo que ConexionBBDD.obtenerConexion() funciona
        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                count = rs.getInt(1); // El COUNT(*) es la primera columna
            }
        }
        return count;
    }

    public static List<Producto> buscarPorCampo(String campo, Object valor) throws SQLException, DatoInvalidoException {
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock "
                + "FROM producto WHERE " + campo + " = ?";

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

    public static boolean actualizarProductoCampo(int idProducto, String campo, Object valor) throws SQLException {
        String sql = "UPDATE producto SET " + campo + " = ? WHERE id_producto = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, valor);
            ps.setInt(2, idProducto);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Producto actualizado correctamente.");
            } else {
                System.out.println("Producto no actualizado");
            }
            return filasAfectadas > 0;
        }
    }

    public static boolean eliminarProducto(int idProducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            int filasAfectadas = ps.executeUpdate();

            System.out.println("Filas afectadas: " + filasAfectadas);

            return filasAfectadas > 0; // true si se eliminó algún producto
        }
    }

    public static List<Producto> obtenerProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock FROM producto";

        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);) {

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
                    // ¡Añadir gestión de errores aquí!
                    int idInvalido = rs.getInt("id_producto");
                    System.err.println("ERROR de datos en el Producto ID " + idInvalido + ": " + e.getMessage()
                            + ". Este producto ha sido OMITIDO de la lista.");
                    // continue es implícito aquí, el bucle pasa al siguiente rs.next()
                }
            }
        }

        return productos;
    }

    /**
     * Inserta un nuevo producto en la base de datos.
     * 
     * @param producto El producto a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public static boolean insertarProducto(Producto producto) throws SQLException {

        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";

        boolean exito = false;

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                exito = true;
            }
        }
        return exito; // Retorna true si la inserción fue exitosa
    }
}
