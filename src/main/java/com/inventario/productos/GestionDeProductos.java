package com.inventario.productos;

import java.util.Scanner;

import com.inventario.util.Util;

public class GestionDeProductos {

    private static void insertarProducto(Scanner scanner) {
        System.out.println("\n--- Insertar nuevo producto ---");

        String nombre = Util.pedirTexto(scanner, "Introduce el nombre del producto:");

        String descripcion = Util.pedirTexto(scanner, "Introduce la descripción del producto:");

        double precio = Util.pedirDecimalMinimo(scanner, descripcion, 0);

        int stock = Util.pedirNumeroMinimo(scanner, "Introduce el stock inicial del producto:", 0);

        try {
            Producto nuevoProducto = new Producto(nombre, descripcion, precio, stock);
            // Aquí iría la lógica para guardar el producto en la base de datos
            System.out.println("Producto insertado correctamente: " + nuevoProducto.getNombre());
        } catch (IllegalArgumentException e) {
            System.err.println("Error al crear el producto: " + e.getMessage());
        }
    }

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
}
