package com.inventario.ventas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.inventario.clientes.Cliente;
import com.inventario.clientes.ClientesBBDD;
import com.inventario.clientes.GestionDeClientes;
import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.productos.GestionDeProductos;
import com.inventario.productos.Producto;
import com.inventario.productos.ProductosBBDD;
import com.inventario.util.Util;

public class GestionVentas {

    private static boolean insertarClienteDesdeVentas(Scanner scanner) throws SQLException, DatoInvalidoException {
        System.out.println("\n--- CREAR CLIENTE PARA LA VENTA ---");

        String nombre = Util.pedirTexto(scanner, "Nombre del cliente:");
        String email = Util.pedirTexto(scanner, "Email del cliente:");
        String telefono = Util.pedirTexto(scanner, "Teléfono del cliente:");
        double dinero = Util.pedirDecimalMinimo(scanner, "Dinero del cliente:", 0);

        Cliente nuevo = new Cliente(nombre, email, telefono, dinero);
        ClientesBBDD.insertarCliente(nuevo);
        System.out.println("Cliente creado correctamente.");
        return true;
    }

    /**
     * Lógica principal para registrar la venta:
     * 1. Selecciona Cliente.
     * 2. Crea la lista de Detalles (List<DetalleVenta>).
     * 3. Llama a VentasBBDD para la transacción.
     */
    private static void guardarVenta(Scanner scanner) {
        System.out.println("\n--- REGISTRANDO VENTA ---");

        // Aquí es donde "nace" la lista de detalles
        List<DetalleVenta> detalles = new ArrayList<>();
        int idCliente = -1;

        try {
            // --- 1. SELECCIONAR CLIENTE ---
            System.out.println("\n--- Clientes Disponibles ---");
            // Asumo que tienes un método listarClientes() en ClientesBBDD
            List<Cliente> clientes = ClientesBBDD.obtenerClientes();

            GestionDeClientes.listarClientes();

            boolean clienteValido = false;
            do {
                idCliente = Util.pedirNumeroMinimo(scanner, "Introduce el ID del cliente:", 1);
                // Validar que el cliente existe
                clienteValido = clientes.get(idCliente - 1).getId() == idCliente;
                if (!clienteValido) {
                    System.err.println("ID de cliente no válido. Inténtalo de nuevo.");
                }
            } while (!clienteValido);

            System.out.println("Cliente ID " + idCliente + " seleccionado.");

            System.out.println("\n--- Añadir Productos a la Venta ---");
            int opcion = 1;

            do {
                GestionDeProductos.listarProductos();

                int idProducto = Util.pedirNumeroMinimo(scanner, "Introduce el ID del producto:", 1);

                Producto p = ProductosBBDD.buscarPorCampo("id_producto", idProducto).get(0);

                if (p == null) {
                    System.err.println("Producto no encontrado.");
                } else if (p.getStock() == 0) {
                    System.err.println("Producto agotado.");
                } else {
                    // Pedir cantidad validando stock
                    int cantidad = Util.pedirNumeroConRango(scanner, "Cantidad (Max: " + p.getStock() + "):", 1,
                            p.getStock());

                    // Crear el objeto DetalleVenta
                    // Usamos 0 como idVenta temporal
                    DetalleVenta detalle = new DetalleVenta(0, p.getId(), cantidad, p.getPrecio());
                    detalles.add(detalle);
                    System.out.println("Producto añadido: " + p.getNombre() + " (x" + cantidad + ")");
                }

                opcion = Util.pedirSiNO(scanner, "¿Añadir otro producto?");
            } while (opcion == 1);

            // --- 3. EJECUTAR TRANSACCIÓN ---
            if (detalles.isEmpty()) {
                System.out.println("Venta cancelada (no hay productos).");
            } else {
                System.out.println("\nProcesando transacción...");
                boolean exito = VentasBBDD.registrarVentaTransaccional(idCliente, detalles);

                if (exito) {
                    System.out.println("Venta registrada con éxito.");
                } else {
                    System.err.println("La venta falló y fue revertida (Rollback).");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
            System.err.println("La transacción ha sido revertida.");
        } catch (DatoInvalidoException e) {
            System.err.println("Error de datos: " + e.getMessage());
        }
    }

    public static void crearVenta(Scanner scanner) {
        System.out.println("\n--- CREAR VENTA ---");
        boolean preparado = false;

        try {
            int cantidadProductos = ProductosBBDD.contarProductos();
            int cantidadClientes = ClientesBBDD.contarClientes();

            if (cantidadProductos > 0) {
                if (cantidadClientes == 0) {
                    System.out.println("No hay clientes registrados.");
                    int opcion = Util.pedirSiNO(scanner, "¿Deseas crear un cliente ahora?");
                    if (opcion == 1) {
                        boolean creado = false;
                        do {
                            try {

                                creado = insertarClienteDesdeVentas(scanner);

                            } catch (SQLException e) {
                                System.err.println("Error tras insertar el cliente: " + e.getMessage());
                            } catch (DatoInvalidoException e) {
                                System.err.println("Error en los datos del cliente insertado: " + e.getMessage());
                            } catch (Exception e) {
                                System.err.println("Error inesperado: " + e.getMessage());
                            }
                        } while (!creado);
                        preparado = true;

                    } else {
                        System.out.println("No es posible registrar una venta sin clientes. Volviendo al menú.");
                    }
                } else {
                    preparado = true;
                }
            } else {
                System.out.println("No hay productos registrados.");
            }
        } catch (SQLException e) {
            System.err.println(
                    "Error al comprobar la existencia de productos o clientes en la base de datos: " + e.getMessage());
        }

        if (preparado) {
            guardarVenta(scanner);
        }
    }

    /**
     * Orquesta la consulta de detalles y el cálculo del total (usando la Función).
     */
    private static void consultarDetallesConTotal(Scanner scanner) throws SQLException {
        System.out.println("\n--- DETALLES DE VENTA ---");
        int idVenta = Util.pedirNumeroMinimo(scanner, "Introduce el ID de la venta:", 1);

        // 1. Imprime la lista de detalles (sin clase auxiliar)
        boolean existenDetalles = VentasBBDD.imprimirDetallesVenta(idVenta);

        // 2. Si se imprimieron detalles, llamamos a la Función para el total
        if (existenDetalles) {
            double total = VentasBBDD.calcularTotalVentaConFuncion(idVenta);
            System.out.println("---------------------------------------------------------------");
            System.out.printf("TOTAL VENTA: %.2f\n", total);
        }
    }

    public static void menuConsultasVentas(Scanner scanner) {
        int opcion;

        do {
            System.out.println("\n====== CONSULTAS DE VENTAS ======");
            System.out.println("1. Listar todas las ventas (Resumen)");
            System.out.println("2. Consultar detalles de una venta por ID");
            System.out.println("0. Volver al menú principal");
            System.out.println("=================================");
            System.out.print("Selecciona una opción: ");

            opcion = Util.pedirNumeroConRango(scanner, null, 0, 2);

            try {
                switch (opcion) {
                    case 1 -> VentasBBDD.imprimirResumenVentas();
                    case 2 -> consultarDetallesConTotal(scanner);
                    case 0 -> System.out.println("Volviendo al menú principal...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.err.println("Error de Base de Datos: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
            }

        } while (opcion != 0);
    }

}
