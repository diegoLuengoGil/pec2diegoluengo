package com.inventario.productos;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.util.Util;

public class GestionDeProductos {
    private static void buscarProductoPorId(Scanner scanner) {
        System.out.println("\n--- BUSCAR PRODUCTO POR ID ---");

        int id = Util.pedirNumeroMinimo(scanner, "Introduce el ID del producto:", 1);

        try {
            List<Producto> resultados = ProductosBBDD.buscarPorCampo("id_producto", id);

            if (resultados.isEmpty()) {
                System.out.println("No se encontró ningún producto con ese ID.");

            } else {

                Producto p = resultados.get(0);

                System.out.println("\nProducto encontrado:");
                System.out.println("ID: " + p.getId());
                System.out.println("Nombre: " + p.getNombre());
                System.out.println("Descripción: " + p.getDescripcion());
                System.out.println("Precio: " + p.getPrecio());
                System.out.println("Stock: " + p.getStock());
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar el producto: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void actualizarProducto(Scanner scanner) {
        System.out.println("\n--- ACTUALIZAR PRODUCTO ---");

        try {
            listarProductos(); // Mostrar productos primero

            int id = Util.pedirNumeroMinimo(scanner, "Introduce el ID del producto a actualizar:", 1);

            int opcion;
            do {
                System.out.println("\n¿Qué campo deseas actualizar?");
                System.out.println("1. Nombre");
                System.out.println("2. Descripción");
                System.out.println("3. Precio de venta");
                System.out.println("4. Stock");
                System.out.println("0. Salir");
                System.out.print("Opción: ");

                opcion = Util.pedirNumeroMinimo(scanner, "", 1);

                switch (opcion) {
                    case 1 -> {
                        String nuevoNombre = Util.pedirTexto(scanner, "Nuevo nombre:");
                        ProductosBBDD.actualizarProductoCampo(id, "nombre", nuevoNombre);
                    }
                    case 2 -> {
                        String nuevaDesc = Util.pedirTexto(scanner, "Nueva descripción:");
                        ProductosBBDD.actualizarProductoCampo(id, "descripcion", nuevaDesc);
                    }
                    case 3 -> {
                        double nuevoPrecio = Util.pedirDecimalMinimo(scanner, "Nuevo precio:", 0);
                        ProductosBBDD.actualizarProductoCampo(id, "precio", nuevoPrecio);
                    }
                    case 4 -> {
                        int nuevoStock = Util.pedirNumeroMinimo(scanner, "Nuevo stock:", 0);
                        ProductosBBDD.actualizarProductoCampo(id, "stock", nuevoStock);
                    }
                    case 0 -> System.out.println("Volviendo al menú anterior...");
                    default -> System.out.println("Opción no válida.");
                }

            } while (opcion != 0);

        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void eliminarProducto(Scanner scanner) {
        System.out.println("\n--- ELIMINAR PRODUCTO ---");

        try {
            // Listamos productos para que el usuario vea los IDs
            listarProductos();

            int id = Util.pedirNumero(scanner, "Introduce el ID del producto a eliminar:");

            boolean eliminado = ProductosBBDD.eliminarProducto(id);

            if (eliminado) {
                System.out.println("Producto eliminado correctamente.");
            } else {
                System.out.println("No se encontró ningún producto con ese ID.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar el producto en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void listarProductos() {
        System.out.println("\n--- LISTADO DE PRODUCTOS ---");

        try {
            List<Producto> productos = ProductosBBDD.obtenerProductos();

            if (productos.isEmpty()) {
                System.out.println("No hay productos en la base de datos.");
            } else {

                System.out.printf("%-5s %-20s %-30s %-10s %-5s%n", "ID", "Nombre", "Descripción", "Precio", "Stock");
                System.out.println("---------------------------------------------------------------------");

                for (Producto producto : productos) {
                    System.out.printf("%-5d %-20s %-30s %-10.2f %-5d%n",
                            producto.getId(), producto.getNombre(), producto.getDescripcion(), producto.getPrecio(),
                            producto.getStock());
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar el producto con la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void insertarProducto(Scanner scanner) {
        System.out.println("\n--- Insertar nuevo producto ---");

        String nombre = Util.pedirTexto(scanner, "Introduce el nombre del producto:");

        String descripcion = Util.pedirTexto(scanner, "Introduce la descripción del producto:");

        double precio = Util.pedirDecimalMinimo(scanner, "Introduce el precio del producto:", 0);

        int stock = Util.pedirNumeroMinimo(scanner, "Introduce el stock inicial del producto:", 0);

        try {
            Producto nuevoProducto = new Producto(nombre, descripcion, precio, stock);
            ProductosBBDD.insertarProducto(nuevoProducto);
            System.out.println("Producto insertado correctamente: " + nuevoProducto.getNombre());

        }catch (SQLException e) {
            System.err.println("Error al insertar el producto en la base de datos: " + e.getMessage());
        } catch (DatoInvalidoException e) {
            System.err.println("Error de dato inválido: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
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
            System.out.println("0. Volver al menú principal");
            System.out.println("===================================");
            System.out.print("Selecciona una opción: ");

            opcion = Util.pedirNumeroConRango(scanner, null, 0, 5);

            switch (opcion) {
                case 1 -> insertarProducto(scanner);
                case 2 -> actualizarProducto(scanner);
                case 3 -> eliminarProducto(scanner);
                case 4 -> listarProductos();
                case 5 -> buscarProductoPorId(scanner);
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }
}
