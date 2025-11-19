package com.inventario.informes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.inventario.conexion.ConexionBBDD;

/**
 * Clase que gestiona los informes y las consultas a la base de datos de los
 * informes.
 */
public class InformesBBDD {
    /**
     * Muestra el contenido de cualquier tabla usando metadatos.
     * ESTE MÉTODO CUMPLE EL REQUISITO OBLIGATORIO DE ResultSetMetaData.
     *
     * @param nombreTabla El nombre de la tabla a consultar (cliente, producto,
     *                    venta, detalle_venta).
     */
    public static void informeGenericoTabla(String nombreTabla) {
        String sql = "SELECT * FROM " + nombreTabla;

        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int numeroColumnas = metaData.getColumnCount();

            System.out.println("\n--- INFORME DE TABLA: " + nombreTabla.toUpperCase() + " ---");

            for (int i = 1; i <= numeroColumnas; i++) {
                System.out.printf("%-25s", metaData.getColumnLabel(i));
            }
            System.out.println();
            System.out.println("=".repeat(numeroColumnas * 25));

            boolean hayFilas = false;
            while (rs.next()) {
                hayFilas = true;
                for (int i = 1; i <= numeroColumnas; i++) {
                    Object valor = rs.getObject(i);
                    System.out.printf("%-25s", (valor != null ? valor.toString() : "NULL"));
                }
                System.out.println();
            }

            if (!hayFilas) {
                System.out.println("La tabla está vacía.");
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al generar informe de " + nombreTabla + ": " + e.getMessage());
        }
    }
}
