package com.inventario.bbdd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class GestionBBDD {

    /**
     * Imprime el contenido de una tabla o consulta de forma genérica.
     *
     * @param sql La consulta SQL a ejecutar.
     */
    public static void imprimirTabla(String sql) {
        try (Connection con = ConexionBBDD.obtenerConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int numeroColumnas = metaData.getColumnCount();

            // Imprimir encabezados
            for (int i = 1; i <= numeroColumnas; i++) {
                System.out.printf("%-25s", metaData.getColumnLabel(i));
            }
            System.out.println();
            System.out.println("=".repeat(numeroColumnas * 25));

            // Imprimir filas
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
                System.out.println("No se encontraron resultados.");
            }

        } catch (SQLException e) {
            System.err.println("Error al imprimir la tabla: " + e.getMessage());
        }
    }
}
