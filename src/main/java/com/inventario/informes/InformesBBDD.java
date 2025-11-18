package com.inventario.informes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.inventario.conexion.ConexionBBDD;

public class InformesBBDD {
    /**
     * Muestra el contenido de cualquier tabla usando metadatos.
     * ESTE MÉTODO CUMPLE EL REQUISITO OBLIGATORIO DE ResultSetMetaData.
     *
     * @param nombreTabla El nombre de la tabla a consultar (cliente, producto,
     *                    venta, detalle_venta).
     */
    public static void informeGenericoTabla(String nombreTabla) {
        // La consulta es genérica: se seleccionan todas las columnas de la tabla
        String sql = "SELECT * FROM " + nombreTabla;

        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            // 1. OBTENER METADATOS (ResultSetMetaData)
            ResultSetMetaData metaData = rs.getMetaData();
            int numeroColumnas = metaData.getColumnCount();

            System.out.println("\n--- INFORME DE TABLA: " + nombreTabla.toUpperCase() + " ---");

            // 2. IMPRIMIR NOMBRES DE COLUMNAS DINÁMICAMENTE
            for (int i = 1; i <= numeroColumnas; i++) {
                // getColumnLabel(i) devuelve el nombre de la columna para la presentación
                System.out.printf("%-25s", metaData.getColumnLabel(i));
            }
            System.out.println();
            // Imprime una línea separadora basada en el ancho de las columnas
            System.out.println("=".repeat(numeroColumnas * 25));

            // 3. IMPRIMIR DATOS
            boolean hayFilas = false;
            while (rs.next()) {
                hayFilas = true;
                for (int i = 1; i <= numeroColumnas; i++) {
                    // getObject(i) recupera el valor de la columna i de forma genérica
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
            // Si hay un error, el programa no se detiene.
        }
    }
}
