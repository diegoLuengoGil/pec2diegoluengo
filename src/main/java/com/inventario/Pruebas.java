package com.inventario;

import java.sql.Connection;

import conexion.ConexionBBDD;

public class Pruebas {
    public static void main(String[] args) {

        ConexionBBDD.iniciarConexion();

        try (Connection con = ConexionBBDD.obtenerConexion()) {
            System.out.println("FUnciona");
        } catch (Exception e) {
            System.err.println("Error al obtener la conexión: " + e.getMessage());
        }

    }
}
