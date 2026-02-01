package com.inventario.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.inventario.bbdd.ConexionBBDD;

import com.inventario.model.DetalleVenta;
import com.inventario.repository.VentaRepository;

public class VentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public void realizarVenta(int idCliente, List<DetalleVenta> detalles) throws SQLException {
        Connection con = null;
        try {
            con = ConexionBBDD.obtenerConexion();
            con.setAutoCommit(false);

            int idVenta = ventaRepository.insertarCabeceraVenta(idCliente);
            double total = ventaRepository.procesarDetalles(con, idVenta, detalles);
            ventaRepository.cobrarCliente(con, idCliente, total);

            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Error during rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    throw new SQLException("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public List<String> listarResumenVentas() throws SQLException {
        return ventaRepository.obtenerResumenVentas();
    }

    public List<String> listarDetallesVenta(int idVenta) throws SQLException {
        return ventaRepository.obtenerDetallesVenta(idVenta);
    }

    public double obtenerTotalVenta(int idVenta) throws SQLException {
        return ventaRepository.calcularTotalVenta(idVenta);
    }
}
