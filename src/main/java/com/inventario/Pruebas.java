package com.inventario;

import java.sql.Connection;

import conexion.ConexionBD;

public class Pruebas {
    public static void main(String[] args) {

        ConexionBD.iniciarConexion();

        try (Connection con = ConexionBD.obtenerConexion()) {
            System.out.println("FUnciona");
        } catch (Exception e) {
            System.err.println("Error al obtener la conexión: " + e.getMessage());
        }

    }
}
