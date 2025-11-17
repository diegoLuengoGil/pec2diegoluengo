package com.inventario.ventas;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
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
     */
    private static int insertarCabeceraVenta(Connection con, int idCliente) throws SQLException {
        int idVenta = -1;
        String sql = "INSERT INTO venta (id_cliente) VALUES (?)";

        try (PreparedStatement ps = con.prepareStatement(sql,
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
            throw new SQLException("Error al obtener el ID de la venta generada.");
        }
        System.out.println("Venta ID " + idVenta + " registrada.");
        return idVenta;
    }

    /**
     * Actualiza el stock de un producto específico en la BBDD.
     */
    private static void actualizarStockProducto(Connection con, DetalleVenta detalle) throws SQLException {
        String sql = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";

        try (PreparedStatement psStock = con.prepareStatement(sql)) {
            psStock.setInt(1, detalle.getCantidad());
            psStock.setInt(2, detalle.getIdProducto());
            psStock.executeUpdate();
            System.out.println("   [Stock OK] Stock de Producto ID " + detalle.getIdProducto() + " actualizado.");
        }
    }

    /**
     * Procesa la lista de detalles, llama al SP y actualiza el stock.
     */
    private static double procesarDetalles(Connection con, int idVenta, List<DetalleVenta> detalles)
            throws SQLException {

        double totalVenta = 0.0;
        String sqlSP = "{CALL RegistrarDetalleVenta(?, ?, ?, ?, ?)}";

        try (CallableStatement csDetalle = con.prepareCall(sqlSP)) {

            for (DetalleVenta detalle : detalles) {
                double subtotalDetalle = detalle.getCantidad() * detalle.getPrecioUnitario();

                // 1. Llamar al SP (valida stock e inserta detalle)
                csDetalle.setInt(1, idVenta);
                csDetalle.setInt(2, detalle.getIdProducto());
                csDetalle.setInt(3, detalle.getCantidad());
                csDetalle.setDouble(4, detalle.getPrecioUnitario());
                csDetalle.registerOutParameter(5, Types.INTEGER);
                csDetalle.execute();
                int estadoSP = csDetalle.getInt(5);

                if (estadoSP == 1) {
                    // 2. Actualizar Stock (en Java)
                    actualizarStockProducto(con, detalle);
                    totalVenta += subtotalDetalle;
                    System.out.println("[Detalle OK] Producto ID " + detalle.getIdProducto() + " insertado.");
                } else {
                    String mensajeError = switch (estadoSP) {
                        case -1 -> "Stock insuficiente";
                        case -2 -> "Producto no encontrado";
                        default -> "Error desconocido (Código: " + estadoSP + ")";
                    };

                    System.err.printf("[Detalle FALLO] Producto ID %d: %s.\n", detalle.getIdProducto(),
                            mensajeError);

                    // Lanzar la excepción para forzar el Rollback (parcial o total)
                    throw new SQLException("Error en la línea de venta: " + mensajeError);
                }
            }
        }
        return totalVenta;
    }

    /**
     * Actualiza el dinero del cliente con el total de la venta.
     */
    private static void actualizarDineroCliente(Connection con, int idCliente, double totalVenta)
            throws SQLException {

        String sql = "UPDATE cliente SET dinero = dinero - ? WHERE id_cliente = ?";

        try (PreparedStatement psCliente = con.prepareStatement(sql)) {
            psCliente.setDouble(1, totalVenta);
            psCliente.setInt(2, idCliente);

            if (psCliente.executeUpdate() < 1) {
                // Comprobamos si el fallo es por dinero insuficiente
                // (Asumiendo que tienes el CHECK (dinero >= 0) en la BBDD)
                throw new SQLException("Error al actualizar el dinero del cliente ID " + idCliente
                        + ". Saldo insuficiente o cliente no encontrado.");
            }
            System.out.printf("-> Dinero del cliente ID %d actualizado. Total: -%.2f.\n", idCliente, totalVenta);
        }
    }

    // ----------------------------------------------------------------------
    // MÉTODO PRINCIPAL DE LA TRANSACCIÓN (ORQUESTACIÓN)
    // ----------------------------------------------------------------------

    /**
     * Registra la venta completa en una transacción.
     * Esta es la estructura correcta para manejar transacciones manualmente.
     */
    public static boolean registrarVentaTransaccional(int idCliente, List<DetalleVenta> detalles) throws SQLException {
        Connection con = null; // Debe declararse fuera del try para ser accesible en catch/finally
        Savepoint puntoSeguro = null;
        boolean exito = false;
        int idVenta = -1;

        try {
            con = ConexionBBDD.obtenerConexion();
            con.setAutoCommit(false); // 1. Iniciar la transacción

            // 2. Insertar Cabecera (puede fallar)
            idVenta = insertarCabeceraVenta(con, idCliente);

            // 3. Establecer Savepoint
            puntoSeguro = con.setSavepoint("AntesDeActualizaciones");
            System.out.println("Savepoint 'AntesDeActualizaciones' establecido.");

            // 4. Procesar Detalles y Stock (puede fallar)
            double totalVenta = procesarDetalles(con, idVenta, detalles);

            // 5. Actualizar Dinero Cliente (puede fallar)
            actualizarDineroCliente(con, idCliente, totalVenta);

            // 6. Commit Final
            con.commit();
            exito = true;
            System.out.println("\nVenta registrada con éxito. COMMIT realizado.");

        } catch (SQLException e) {

            // 7. Gestión del Rollback
            if (con != null) {
                if (puntoSeguro != null) {
                    // Fallo después de la Cabecera -> Rollback Parcial
                    con.rollback(puntoSeguro);
                    con.commit(); // Asegurar que la cabecera se mantenga
                    System.err.println(
                            "\nFALLO: Transacción revertida al Savepoint. Cabecera Venta ID " + idVenta
                                    + " mantenida.");
                    // Devolvemos true porque la demostración del rollback parcial fue exitosa
                    exito = true;
                } else {
                    // Fallo antes del Savepoint (Cabecera o Conexión) -> Rollback Total
                    con.rollback();
                    System.err.println("\nFALLO: Transacción revertida completamente.");
                    exito = false;
                }
            }
            // Propagar la excepción para que sea manejada por la capa de gestión
            throw e;

        } finally {
            if (con != null) {
                // Restaurar el autoCommit y cerrar la conexión
                con.setAutoCommit(true);
                con.close();
            }
        }
        return exito;
    }
}