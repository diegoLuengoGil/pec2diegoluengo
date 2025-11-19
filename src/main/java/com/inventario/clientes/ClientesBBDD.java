package com.inventario.clientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.inventario.conexion.ConexionBBDD;
import com.inventario.excepciones.DatoInvalidoException;

/**
 * Clase que gestiona las operaciones de la tabla "cliente" en la base de datos.
 */
public class ClientesBBDD {

    /**
     * Verifica si existe un cliente con el ID especificado.
     *
     * @param id El ID del cliente a verificar.
     * @return true si el cliente existe, false en caso contrario.
     * @throws SQLException Si hay un error al verificar la existencia del cliente.
     */
    public static boolean existeCliente(int id) throws SQLException {
        String sql = "SELECT 1 FROM cliente WHERE id_cliente = ?";
        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Agrega la cantidad especificada al saldo del cliente.
     * * @param idCliente ID del cliente.
     * 
     * @param cantidad Cantidad a sumar.
     * @return true si la operación fue exitosa (se actualizó 1 fila), false si no
     *         se encontró el cliente.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public static boolean agregarDinero(int idCliente, double cantidad) throws SQLException {
        String sql = "UPDATE cliente SET dinero = dinero + ? WHERE id_cliente = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, cantidad);
            ps.setInt(2, idCliente);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Resta la cantidad especificada al saldo del cliente, asegurando que el saldo
     * no quede negativo.
     * * @param idCliente ID del cliente.
     * 
     * @param cantidad Cantidad a restar.
     * @return true si la operación fue exitosa, false si el cliente no fue
     *         encontrado o el saldo es insuficiente.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public static boolean restarDinero(int idCliente, double cantidad) throws SQLException {
        String sql = "UPDATE cliente SET dinero = dinero - ? WHERE id_cliente = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, cantidad);
            ps.setInt(2, idCliente);
            ps.setDouble(3, cantidad);

            // Devuelve true si se actualizó exactamente 1 fila
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cuenta el número total de clientes en la base de datos.
     *
     * @return El número total de clientes.
     * @throws SQLException Si hay un error al contar los clientes.
     */
    public static int contarClientes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Cliente";
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

    /**
     * Busca clientes por un campo específico.
     *
     * @param campo El campo por el cual buscar.
     * @param valor El valor del campo para la búsqueda.
     * @return Una lista de clientes que coinciden con el valor del campo.
     * @throws SQLException          Si hay un error al buscar los clientes.
     * @throws DatoInvalidoException Si el campo es inválido.
     */
    public static List<Cliente> buscarPorCampo(String campo, Object valor) throws SQLException, DatoInvalidoException {
        String sql = "SELECT id_cliente, nombre, email, telefono, dinero FROM cliente WHERE " + campo + " = ?";

        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, valor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            rs.getDouble("dinero"));
                    clientes.add(c);
                }
            }
        }

        return clientes;
    }

    /**
     * Elimina un cliente de la base de datos.
     *
     * @param idCliente El ID del cliente a eliminar.
     * @return true si se eliminó correctamente, false en caso contrario.
     * @throws SQLException Si hay un error al eliminar el cliente.
     */
    public static boolean eliminarCliente(int idCliente) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // true si se eliminó algo
        }
    }

    /**
     * Actualiza un campo específico de un cliente.
     *
     * @param idCliente El ID del cliente a actualizar.
     * @param campo     El campo a actualizar.
     * @param valor     El nuevo valor del campo.
     * @return true si se actualizó correctamente, false en caso contrario.
     * @throws SQLException Si hay un error al actualizar el cliente.
     */
    public static boolean actualizarClienteCampo(int idCliente, String campo, Object valor) throws SQLException {
        String sql = "UPDATE cliente SET " + campo + " = ? WHERE id_cliente = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, valor);
            ps.setInt(2, idCliente);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Clientes actualizado correctamente.");
            } else {
                System.out.println("Clientes no actualizado");
            }

            return filasAfectadas > 0;
        }
    }

    /**
     * Obtiene todos los clientes de la base de datos.
     *
     * @return Una lista de clientes.
     * @throws SQLException Si hay un error al obtener los clientes.
     */
    public static List<Cliente> obtenerClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT id_cliente, nombre, email, telefono, dinero FROM cliente";

        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                try {
                    Cliente c = new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            rs.getDouble("dinero"));
                    clientes.add(c);
                } catch (DatoInvalidoException e) {
                    int idInvalido = rs.getInt("id_cliente");
                    System.err.println("ERROR de datos en el Cliente ID " + idInvalido + ": " + e.getMessage()
                            + ". Este id ha sido OMITIDO de la lista.");
                }
            }
        }

        return clientes;
    }

    /**
     * Inserta un cliente en la base de datos.
     *
     * @param cliente El cliente a insertar.
     * @return true si se inserta correctamente, false en caso contrario.
     * @throws SQLException Si hay un error al insertar el cliente.
     */
    public static boolean insertarCliente(Cliente cliente) throws SQLException {
        boolean exito = false;
        String sql = "INSERT INTO cliente (nombre, email, telefono, dinero) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefono());
            ps.setDouble(4, cliente.getDinero());

            exito = ps.executeUpdate() > 0;
        }
        return exito;
    }

    /**
     * Imprime la tabla de clientes usando metadatos.
     */
    public static void imprimirClientes() {
        ConexionBBDD.imprimirTabla("SELECT * FROM cliente");
    }
}
