package com.inventario.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Clase que gestiona la conexión a la base de datos.
 */
public class ConexionBBDD {

    /**
     * URL de la base de datos.
     */
    private static String url;

    /**
     * Usuario de la base de datos.
     */
    private static String user;

    /**
     * Contraseña de la base de datos.
     */
    private static String password;

    /**
     * Obtiene la URL de la base de datos.
     *
     * @return La URL de la base de datos.
     */
    public static String getUrl() {
        return url;
    }

    /**
     * Establece la URL de la base de datos.
     *
     * @param url La URL de la base de datos.
     */
    public static void setUrl(String url) {
        ConexionBBDD.url = url;
    }

    /**
     * Obtiene el usuario de la base de datos.
     *
     * @return El usuario de la base de datos.
     */
    public static String getUser() {
        return user;
    }

    /**
     * Establece el usuario de la base de datos.
     *
     * @param user El usuario de la base de datos.
     */
    public static void setUser(String user) {
        ConexionBBDD.user = user;
    }

    /**
     * Obtiene la contraseña de la base de datos.
     *
     * @return La contraseña de la base de datos.
     */
    public static String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña de la base de datos.
     *
     * @param password La contraseña de la base de datos.
     */
    public static void setPassword(String password) {
        ConexionBBDD.password = password;
    }

    /**
     * Inicia la conexión a la base de datos.
     */
    public static void iniciarConexion() {
        final String RUTADELARCHIVO = "src/main/resources/config.properties";
        Properties prop = new Properties();

        try (InputStream input = ConexionBBDD.class.getClassLoader().getResourceAsStream("config.properties")) {

            prop.load(input);

            setUrl(prop.getProperty("db.url"));
            setUser(prop.getProperty("db.user"));
            setPassword(prop.getProperty("db.password"));

            if (getUrl() == null || getUser() == null || getPassword() == null) {
                System.out.println("Error: El archivo '" + RUTADELARCHIVO
                        + "' no contiene todas las propiedades requeridas (db.url, db.user, db.password).");
            }

            System.out.println("Configuración cargada correctamente desde '" + RUTADELARCHIVO + "'.");

        } catch (IOException ex) {
            System.err.println("Error al leer el archivo de configuración: " + ex.getMessage());
        }
    }

    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return Una conexión a la base de datos.
     * @throws SQLException Si hay un error al obtener la conexión.
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Imprime el contenido de una tabla o consulta de forma genérica.
     *
     * @param sql La consulta SQL a ejecutar.
     */
    public static void imprimirTabla(String sql) {
        try (Connection con = obtenerConexion();
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
