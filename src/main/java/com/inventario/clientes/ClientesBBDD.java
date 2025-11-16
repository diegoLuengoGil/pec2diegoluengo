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

public class ClientesBBDD {

    public static List<Cliente> buscarPorCampo(String campo, Object valor) throws SQLException, DatoInvalidoException {
        String sql = "SELECT id_cliente, nombre, email, telefono FROM cliente WHERE " + campo + " = ?";

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
                            rs.getString("telefono"));
                    clientes.add(c);
                }
            }
        }

        return clientes;
    }

    public static boolean eliminarCliente(int idCliente) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // true si se eliminó algo
        }
    }

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

    public static List<Cliente> obtenerClientes() throws SQLException, DatoInvalidoException {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT id_cliente, nombre, email, telefono FROM cliente";

        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("telefono"));
                clientes.add(c);
            }
        }

        return clientes;
    }

    public static boolean insertarCliente(Cliente cliente) throws SQLException {
        boolean exito = false;
        String sql = "INSERT INTO cliente (nombre, email, telefono) VALUES (?, ?, ?)";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefono());

            exito = ps.executeUpdate() > 0;
        }
        return exito;
    }
}
