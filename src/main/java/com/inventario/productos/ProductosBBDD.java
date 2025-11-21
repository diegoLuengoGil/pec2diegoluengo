package com.inventario.productos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.inventario.bbdd.ConexionBBDD;
import com.inventario.bbdd.GestionBBDD;
import com.inventario.excepciones.DatoInvalidoException;

/**
 * Clase que gestiona las operaciones de la base de datos relacionadas con los
 * productos.
 */
public class ProductosBBDD {

    /**
     * Cuenta el número total de productos en la base de datos.
     *
     * @return El número total de productos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public static int contarProductos() throws SQLException {
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

    /**
     * Busca productos en la base de datos por un campo específico.
     *
     * @param campo El nombre del campo por el que se buscará.
     * @param valor El valor del campo para la búsqueda.
     * @return Una lista de productos que coinciden con la búsqueda.
     * @throws SQLException          Si ocurre un error al interactuar con la base
     *                               de datos.
     * @throws DatoInvalidoException Si el valor del campo es inválido.
     */
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

    /**
     * Actualiza un campo específico de un producto en la base de datos.
     *
     * @param idProducto El ID del producto a actualizar.
     * @param campo      El nombre del campo a actualizar.
     * @param valor      El nuevo valor para el campo.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
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

    /**
     * Elimina un producto de la base de datos por su ID.
     *
     * @param idProducto El ID del producto a eliminar.
     * @return true si el producto fue eliminado exitosamente, false en caso
     *         contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public static boolean eliminarProducto(int idProducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            int filasAfectadas = ps.executeUpdate();

            System.out.println("Filas afectadas: " + filasAfectadas);

            return filasAfectadas > 0;
        }
    }

    /**
     * Obtiene todos los productos de la base de datos.
     *
     * @return Una lista de todos los productos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
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
                    int idInvalido = rs.getInt("id_producto");
                    System.err.println("ERROR de datos en el Producto ID " + idInvalido + ": " + e.getMessage()
                            + ". Este producto ha sido OMITIDO de la lista.");
                }
            }
        }

        return productos;
    }

    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * @param producto El objeto Producto a insertar.
     * @return {@code true} si el producto fue insertado exitosamente, {@code false}
     *         en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */

    public static boolean insertarProducto(Producto producto) throws SQLException {
        boolean exito = false;
        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());

            exito = ps.executeUpdate() > 0;
        }
        return exito;
    }

    /**
     * Imprime la tabla de productos usando metadatos.
     */
    public static void imprimirProductos() {
        GestionBBDD.imprimirTabla("SELECT * FROM producto");
    }
}
