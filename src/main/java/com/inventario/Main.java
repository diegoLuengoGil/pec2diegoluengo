package com.inventario;

import java.util.Scanner;

import com.inventario.clientes.GestionDeClientes;
import com.inventario.conexion.ConexionBBDD;
import com.inventario.informes.GestionInformes;
import com.inventario.productos.GestionDeProductos;
import com.inventario.util.Util;
import com.inventario.ventas.GestionVentas;

/**
 * Clase principal del programa
 * 
 * @author Diego Luengo Gil
 */
public class Main {

    /**
     * Muestra el menú principal del programa
     */
    public static void menuPrincipal() {
        ConexionBBDD.iniciarConexion();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {

            System.out.println("\n========== MENÚ PRINCIPAL ==========");
            System.out.println("1. Gestión de productos");
            System.out.println("2. Gestión de clientes");
            System.out.println("3. Registrar venta");
            System.out.println("4. Consultar ventas");
            System.out.println("5. Ver Base de Datos");
            System.out.println("0. Salir");
            System.out.println("=====================================");
            System.out.print("Selecciona una opción ");

            opcion = Util.pedirNumeroConRango(scanner, "", 0, 5);

            switch (opcion) {
                case 1 -> GestionDeProductos.menuProductos(scanner);
                case 2 -> GestionDeClientes.menuClientes(scanner);
                case 3 -> GestionVentas.crearVenta(scanner);
                case 4 -> GestionVentas.menuConsultasVentas(scanner);
                case 5 -> GestionInformes.menuInformesAvanzados(scanner);
                case 0 -> System.out.println("\nSaliendo del programa...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    public static void main(String[] args) {
        menuPrincipal();
    }
}