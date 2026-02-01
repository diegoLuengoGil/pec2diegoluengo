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
import com.inventario.model.Cliente;

public class ClienteRepository {

    public boolean existeCliente(int id) throws SQLException {
        boolean existe = false;
        String sql = "SELECT 1 FROM cliente WHERE id_cliente = ?";
        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                existe = rs.next();
            }
        }
        return existe;
    }

    public boolean agregarDinero(int idCliente, double cantidad) throws SQLException {
        boolean exito = false;
        String sql = "UPDATE cliente SET dinero = dinero + ? WHERE id_cliente = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, cantidad);
            ps.setInt(2, idCliente);

            exito = ps.executeUpdate() > 0;
        }
        return exito;
    }

    public boolean restarDinero(int idCliente, double cantidad) throws SQLException {
        boolean exito = false;
        String sql = "UPDATE cliente SET dinero = dinero - ? WHERE id_cliente = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, cantidad);
            ps.setInt(2, idCliente);
            // ps.setDouble(3, cantidad); // Was present in legacy code???
            // Legacy code: ps.setDouble(3, cantidad); ??
            // SQL was: UPDATE cliente SET dinero = dinero - ? WHERE id_cliente = ?
            // Wait, legacy code had 3 parameters set but only 2 placeholders?
            // Legacy Line 77-79:
            // ps.setDouble(1, cantidad);
            // ps.setInt(2, idCliente);
            // ps.setDouble(3, cantidad);
            // This suggests the legacy logic or SQL might have been different or
            // buggy/redundant.
            // "UPDATE cliente SET dinero = dinero - ? WHERE id_cliente = ?" has 2 params.
            // I will stick to 2 params.

            exito = ps.executeUpdate() > 0;
        }
        return exito;
    }

    public int contarClientes() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Cliente";
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

    public List<Cliente> buscarPorCampo(String campo, Object valor) throws SQLException, DatoInvalidoException {
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

    public boolean eliminarCliente(int idCliente) throws SQLException {
        boolean eliminado = false;
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            eliminado = ps.executeUpdate() > 0;
        }
        return eliminado;
    }

    public boolean actualizarClienteCampo(int idCliente, String campo, Object valor) throws SQLException {
        boolean actualizado = false;
        String sql = "UPDATE cliente SET " + campo + " = ? WHERE id_cliente = ?";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, valor);
            ps.setInt(2, idCliente);

            actualizado = ps.executeUpdate() > 0;
        }
        return actualizado;
    }

    public List<Cliente> obtenerClientes() throws SQLException {
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
                    System.err.println("Data error for Client ID " + rs.getInt("id_cliente") + ": " + e.getMessage());
                }
            }
        }
        return clientes;
    }

    public boolean insertarCliente(Cliente cliente) throws SQLException {
        boolean insertado = false;
        String sql = "INSERT INTO cliente (nombre, email, telefono, dinero) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefono());
            ps.setDouble(4, cliente.getDinero());

            insertado = ps.executeUpdate() > 0;
        }
        return insertado;
    }
}
