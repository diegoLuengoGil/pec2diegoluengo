package com.inventario.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.inventario.bbdd.ConexionBBDD;
import com.inventario.model.DetalleVenta;

// DTO helper for ResumenVenta
// Ideally this should be in com.inventario.dto but I'll use a simple inner class or pass Object[] if avoiding new packages?
// Unclean to return Object[]. 
// I will create a VentaResumen class in model or just use Venta if adaptable. 
// Venta.java might be simple.

public class VentaRepository {

    public List<String> obtenerResumenVentas() throws SQLException {
        // Returning formatted strings for simplicity to replace "imprimirResumenVentas"
        // quickly.
        // A better approach would be returning a List<VentaDTO>
        String sql = "SELECT v.id_venta, v.id_cliente, c.nombre "
                + "FROM venta v JOIN cliente c ON v.id_cliente = c.id_cliente "
                + "ORDER BY v.id_venta DESC";
        List<String> resumen = new ArrayList<>();

        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                resumen.add(String.format("%-5d | %-10d | %s",
                        rs.getInt("id_venta"),
                        rs.getInt("id_cliente"),
                        rs.getString("nombre")));
            }
        }
        return resumen;
    }

    public List<String> obtenerDetallesVenta(int idVenta) throws SQLException {
        String sql = "SELECT d.cantidad, d.precio_unitario, p.nombre "
                + "FROM DetalleVenta d JOIN producto p ON d.id_producto = p.id_producto "
                + "WHERE d.id_venta = ?";
        List<String> detalles = new ArrayList<>();

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cantidad = rs.getInt("cantidad");
                    double precio = rs.getDouble("precio_unitario");
                    detalles.add(String.format("%-25s | %-10d | %-10.2f | %.2f",
                            rs.getString("nombre"),
                            cantidad,
                            precio,
                            (cantidad * precio)));
                }
            }
        }
        return detalles;
    }

    public double calcularTotalVenta(int idVenta) throws SQLException {
        double total = 0.0;
        String sql = "{? = CALL CalcularTotalVenta(?)}";

        try (Connection con = ConexionBBDD.obtenerConexion();
                CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.DECIMAL);
            cs.setInt(2, idVenta);
            cs.execute();
            total = cs.getDouble(1);
        }
        return total;
    }

    public int insertarCabeceraVenta(int idCliente) throws SQLException {
        int idVenta = -1;
        String sql = "INSERT INTO venta (id_cliente) VALUES (?)";

        try (PreparedStatement ps = ConexionBBDD.obtenerConexion().prepareStatement(sql,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCliente);
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idVenta = rs.getInt(1);
                    }
                }
            }
        }

        if (idVenta == -1) {
            throw new SQLException("Error generating sale ID.");
        }
        return idVenta;
    }

    public double procesarDetalles(Connection con, int idVenta, List<DetalleVenta> detalles) throws SQLException {
        double totalVenta = 0.0;
        String sqlSP = "{CALL RegistrarDetalleVenta(?, ?, ?, ?, ?)}";

        try (CallableStatement cs = con.prepareCall(sqlSP)) {
            for (DetalleVenta detalle : detalles) {
                double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();

                cs.setInt(1, idVenta);
                cs.setInt(2, detalle.getIdProducto());
                cs.setInt(3, detalle.getCantidad());
                cs.setDouble(4, detalle.getPrecioUnitario());
                cs.registerOutParameter(5, Types.INTEGER);
                cs.execute();

                int estado = cs.getInt(5);

                if (estado == 1) {
                    totalVenta += subtotal;
                } else {
                    String error = (estado == -1) ? "Insufficient stock" : "Product not found";
                    throw new SQLException("Error processing product ID " + detalle.getIdProducto() + ": " + error);
                }
            }
        }
        return totalVenta;
    }

    public void cobrarCliente(Connection con, int idCliente, double cantidad) throws SQLException {
        String sql = "UPDATE cliente SET dinero = dinero - ? WHERE id_cliente = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, cantidad);
            ps.setInt(2, idCliente);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Error updating balance. Verify client or funds.");
            }
        }
    }
}
