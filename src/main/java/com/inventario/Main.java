package com.inventario;

import java.util.Scanner;

import com.inventario.util.Util;

public class Main {

    public static void menuProductos(Scanner scanner) {
        int opcion;

        do {
            System.out.println("\n====== GESTIÓN DE PRODUCTOS ======");
            System.out.println("1. Insertar producto");
            System.out.println("2. Actualizar producto");
            System.out.println("3. Eliminar producto");
            System.out.println("4. Listar productos");
            System.out.println("5. Buscar producto por ID");
            System.out.println("6. Listar productos con poco stock");
            System.out.println("0. Volver al menú principal");
            System.out.println("===================================");
            System.out.print("Selecciona una opción: ");

            opcion = Util.pedirNumeroConRango(scanner, null, 0, 6);

            switch (opcion) {
                case 1 -> insertarProducto(scanner);
                case 2 -> actualizarProducto(scanner); 
                case 3 -> eliminarProducto(scanner);
                case 4 -> listarProductos();
                case 5 -> buscarProductoPorId(scanner); 
                case 6 -> listarProductosPocoStock();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

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
                case 1 -> menuProductos(scanner);
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