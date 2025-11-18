package com.inventario.ventas;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import com.inventario.conexion.ConexionBBDD;

public class VentasBBDD {

    // ... (Tus métodos existentes: insertarCabeceraVenta, actualizarStockProducto,
    // etc.)

    /**
     * Imprime un resumen de las ventas (JOIN con Cliente).
     * No devuelve nada (void), evitando clases auxiliares.
     */
    public static void imprimirResumenVentas() throws SQLException {
        String sql = "SELECT v.id_venta, v.id_cliente, c.nombre "
                + "FROM venta v JOIN cliente c ON v.id_cliente = c.id_cliente "
                + "ORDER BY v.id_venta DESC";
        boolean hayResultados = false;

        System.out.println("\n--- LISTADO DE VENTAS (RESUMEN) ---");

        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            System.out.printf("%-5s | %-10s | %s\n", "ID VTA", "ID CLI", "CLIENTE");
            System.out.println("----------------------------------------------");

            while (rs.next()) {
                hayResultados = true;
                System.out.printf("%-5d | %-10d | %s\n",
                        rs.getInt("id_venta"),
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"));
            }

            if (!hayResultados) {
                System.out.println("No hay ventas registradas.");
            }
        }
    }

    /**
     * Imprime los detalles de una venta específica (JOIN con Producto).
     * Devuelve true si encontró detalles, false si no.
     */
    public static boolean imprimirDetallesVenta(int idVenta) throws SQLException {
        String sql = "SELECT d.cantidad, d.precio_unitario, p.nombre "
                + "FROM DetalleVenta d JOIN producto p ON d.id_producto = p.id_producto "
                + "WHERE d.id_venta = ?";
        boolean hayDetalles = false;

        try (Connection con = ConexionBBDD.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    // Si es la primera fila, imprimimos la cabecera
                    if (!hayDetalles) {
                        hayDetalles = true;
                        System.out.printf("%-25s | %-10s | %-10s | %s\n", "PRODUCTO", "CANTIDAD", "PRECIO U.",
                                "SUBTOTAL");
                        System.out.println("---------------------------------------------------------------");
                    }

                    int cantidad = rs.getInt("cantidad");
                    double precio = rs.getDouble("precio_unitario");
                    System.out.printf("%-25s | %-10d | %-10.2f | %.2f\n",
                            rs.getString("nombre"),
                            cantidad,
                            precio,
                            (cantidad * precio)); // Calculamos subtotal al vuelo
                }
            }
        }

        if (!hayDetalles) {
            System.out.println("No se encontraron detalles para la venta ID " + idVenta);
        }
        return hayDetalles; // Devuelve si se imprimió algo
    }

    /**
     * Llama a la Función Almacenada 'CalcularTotalVenta'
     * y devuelve el total calculado.
     */
    public static double calcularTotalVentaConFuncion(int idVenta) throws SQLException {
        double total = 0.0;
        // Sintaxis para llamar a una Función que retorna un valor
        String sql = "{? = CALL CalcularTotalVenta(?)}";

        try (Connection con = ConexionBBDD.obtenerConexion();
                CallableStatement cs = con.prepareCall(sql)) {

            // 1. Registrar el parámetro de SALIDA (el retorno de la función)
            cs.registerOutParameter(1, Types.DECIMAL);

            // 2. Asignar el parámetro de ENTRADA
            cs.setInt(2, idVenta);

            // 3. Ejecutar
            cs.execute();

            // 4. Recuperar el valor de salida
            total = cs.getDouble(1);
        }
        return total;
    }

    /**
     * Inserta la cabecera de la venta y devuelve el ID generado.
     * Nota: Recibe la conexión, no la crea.
     */
    public static int insertarCabeceraVenta(Connection con, int idCliente) throws SQLException {
        int idVenta = -1;
        String sql = "INSERT INTO venta (id_cliente) VALUES (?)";

        try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
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
            throw new SQLException("Error al obtener el ID de la venta generada.");
        }
        return idVenta;
    }

    /**
     * Procesa la lista de detalles llamando al SP y ahora solo calcula el total (el
     * SP actualiza stock).
     * Retorna el total acumulado de la venta.
     */
    public static double procesarDetalles(Connection con, int idVenta, List<DetalleVenta> detalles)
            throws SQLException {
        double totalVenta = 0.0;
        // El SP ahora se encarga de: insertar DetalleVenta + actualizar Stock
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
                    // ¡ELIMINADA LA LLAMADA A actualizarStockProducto!
                    totalVenta += subtotal;
                } else {
                    String error = (estado == -1) ? "Stock insuficiente" : "Producto no encontrado";
                    throw new SQLException("Error en producto ID " + detalle.getIdProducto() + ": " + error);
                }
            }
        }
        return totalVenta;
    }

    /**
     * Cobra al cliente (resta dinero).
     */
    public static void cobrarCliente(Connection con, int idCliente, double monto) throws SQLException {
        String sql = "UPDATE cliente SET dinero = dinero - ? WHERE id_cliente = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, monto);
            ps.setInt(2, idCliente);
            if (ps.executeUpdate() == 0) {
                // Esto saltará si el cliente no existe, pero la restricción de saldo negativo
                // saltará como excepción SQL directa desde la BBDD.
                throw new SQLException("Error al actualizar saldo. Verifique cliente o fondos.");
            }
        }
    }
}