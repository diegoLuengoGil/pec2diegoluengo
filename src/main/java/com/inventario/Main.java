package com.inventario;

import java.util.Scanner;

import com.inventario.productos.GestionDeProductos;
import com.inventario.util.Util;

public class Main {

    private static void menuClientes() {
        System.out.println("\n[Menú de Clientes]");
    }

    private static void menuVentas() {
        System.out.println("\n[Registrar Venta]");
    }

    private static void menuConsultasVentas() {
        System.out.println("\n[Consultas de Ventas]");
    }

    private static void menuInformesAvanzados() {
        System.out.println("\n[Informes Avanzados]");
    }

    public static void menuPrincipal() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {

            System.out.println("\n========== MENÚ PRINCIPAL ==========");
            System.out.println("1. Gestión de productos");
            System.out.println("2. Gestión de clientes");
            System.out.println("3. Registrar venta (transacción + savepoint)");
            System.out.println("4. Consultar ventas");
            System.out.println("5. Informes avanzados");
            System.out.println("0. Salir");
            System.out.println("=====================================");
            System.out.print("Selecciona una opción: ");

            opcion = Util.pedirNumeroConRango(scanner, null, 0, 5);

            switch (opcion) {
                case 1 -> GestionDeProductos.menuProductos(scanner);
                case 2 -> menuClientes();
                case 3 -> menuVentas();
                case 4 -> menuConsultasVentas();
                case 5 -> menuInformesAvanzados();
                case 0 -> System.out.println("\nSaliendo del programa... 👋");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    public static void main(String[] args) {
        menuPrincipal();
    }
}