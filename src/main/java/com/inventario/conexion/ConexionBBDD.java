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
}
