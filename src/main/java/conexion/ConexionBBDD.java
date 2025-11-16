package conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ConexionBBDD {

    private static String url;
    private static String user;
    private static String password;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ConexionBBDD.url = url;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        ConexionBBDD.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        ConexionBBDD.password = password;
    }

    public static void iniciarConexion() {
        // La ruta corregida según la estructura del proyecto
        final String RUTADELARCHIVO = "src/main/resources/config.properties";
        Properties prop = new Properties();

        try (InputStream input = ConexionBBDD.class.getClassLoader().getResourceAsStream("config.properties")) {

            // Cargar las propiedades desde el archivo
            prop.load(input);

            // Usar los setters para asignar los atributos estáticos
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

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
