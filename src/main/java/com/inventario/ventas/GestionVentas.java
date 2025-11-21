package com.inventario.ventas;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.inventario.bbdd.ConexionBBDD;
import com.inventario.clientes.Cliente;
import com.inventario.clientes.ClientesBBDD;
import com.inventario.clientes.GestionDeClientes;
import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.productos.GestionDeProductos;
import com.inventario.productos.Producto;
import com.inventario.productos.ProductosBBDD;
import com.inventario.util.Util;

/** Clase para gestionar las ventas */
public class GestionVentas {

    /**
     * Inserta un nuevo cliente a partir de la venta.
     * 
     * @param scanner Scanner para la entrada del usuario.
     * @return true si el cliente se ha insertado correctamente.
     * @throws SQLException          Si ocurre un error al interactuar con la base
     *                               de datos.
     * @throws DatoInvalidoException Si se introduce un dato inválido.
     */
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
     * Consulta los detalles de una venta y muestra su total.
     * 
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    private static void consultarDetallesConTotal(Scanner scanner) throws SQLException {
        System.out.println("\n--- DETALLES DE VENTA ---");
        int idVenta = Util.pedirNumeroMinimo(scanner, "Introduce el ID de la venta:", 1);

        boolean existenDetalles = VentasBBDD.imprimirDetallesVenta(idVenta);

        if (existenDetalles) {
            double total = VentasBBDD.calcularTotalVenta(idVenta);
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

            opcion = Util.pedirNumeroConRango(scanner, "", 0, 2);

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

    /**
     * Gestiona la creación de cabecera, detalles y cálculo del total (usando la
     * Función).
     * 
     * @param scanner Scanner para la entrada del usuario.
     */
    private static void guardarVenta(Scanner scanner) {
        System.out.println("\n--- INICIANDO SESIÓN DE VENTAS ---");
        boolean finalizado = false;

        try (Connection con = ConexionBBDD.obtenerConexion()) {
            con.setAutoCommit(false);

            boolean continuarVendiendo = true;

            do {
                System.out.println("\n--- NUEVA VENTA ---");

                Savepoint spInicioVenta = con.setSavepoint("InicioVenta");

                try {
                    int idCliente = seleccionarCliente(scanner);
                    List<DetalleVenta> detalles = seleccionarProductos(scanner);

                    if (detalles.isEmpty()) {
                        System.out.println("Venta vacía. Cancelando esta venta...");
                        con.rollback(spInicioVenta);
                    } else {
                        int idVenta = VentasBBDD.insertarCabeceraVenta(con, idCliente);
                        double total = VentasBBDD.procesarDetalles(con, idVenta, detalles);

                        System.out.printf("\n>> TOTAL A PAGAR: %.2f €\n", total);
                        int confirma = Util.pedirSiNO(scanner, "¿El cliente acepta la compra?");

                        if (confirma == 2) {
                            con.rollback(spInicioVenta);
                            System.out.println("Venta rechazada. Deshaciendo cambios de esta venta.");
                        } else {
                            VentasBBDD.cobrarCliente(con, idCliente, total);
                            System.out.println("Venta aceptada y saldo descontado.");

                            int cancelarTodo = Util.pedirSiNO(scanner,
                                    "¿Desea CANCELAR TODO el proceso de ventas acumulado hasta ahora?");

                            if (cancelarTodo == 1) {
                                con.rollback();
                                System.out.println(
                                        "PROCESO CANCELADO. Se han deshecho TODAS las ventas de la sesión.");
                                continuarVendiendo = false;
                            }
                        }
                    }

                    if (continuarVendiendo) {
                        // E. Continuar con otra venta
                        int masVentas = Util.pedirSiNO(scanner, "¿Desea registrar otra venta en este lote?");
                        if (masVentas == 2) {
                            continuarVendiendo = false;
                            finalizado = true;
                        }
                    }

                } catch (SQLException ex) {
                    System.err.println("Error en la venta actual: " + ex.getMessage());
                    System.err.println("Revertiendo esta venta...");
                    con.rollback(spInicioVenta); // Rollback solo de la venta actual fallida

                    int retry = Util.pedirSiNO(scanner, "¿Intentar otra venta?");
                    if (retry == 2)
                        continuarVendiendo = false;
                }
            } while (continuarVendiendo);

            if (!con.getAutoCommit() && finalizado) { // Si seguimos en transacción
                con.commit();
                System.out.println("\n=== SESIÓN FINALIZADA ===");
            }

            con.setAutoCommit(true);

        } catch (SQLException e) {
            System.err.println("Error CRÍTICO de conexión: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Selecciona un cliente existente.
     * 
     * @param scanner Scanner para la entrada del usuario.
     * @return ID del cliente seleccionado.
     * @throws SQLException Si ocurre un error de base de datos.
     */
    private static int seleccionarCliente(Scanner scanner) throws SQLException {
        int idCliente;
        boolean valido = false;

        do {
            GestionDeClientes.listarClientes();
            idCliente = Util.pedirNumeroMinimo(scanner, "ID Cliente:", 1);

            if (ClientesBBDD.existeCliente(idCliente)) {
                valido = true;
            } else {
                System.out.println("El cliente no existe. Inténtalo de nuevo.");
            }
        } while (!valido);

        return idCliente;
    }

    /**
     * Selecciona productos existentes para la venta.
     * 
     * @param scanner Scanner para la entrada del usuario.
     * @return Lista de detalles de venta seleccionados.
     * @throws SQLException          Si ocurre un error de base de datos.
     * @throws DatoInvalidoException Si se introduce un dato inválido.
     */
    private static List<DetalleVenta> seleccionarProductos(Scanner scanner) throws SQLException, DatoInvalidoException {
        List<DetalleVenta> detalles = new ArrayList<>();
        int opcion = 1;
        do {
            GestionDeProductos.listarProductos();
            int idProd = Util.pedirNumeroMinimo(scanner, "ID Producto:", 1);

            List<Producto> prods = ProductosBBDD.buscarPorCampo("id_producto", idProd);

            if (!prods.isEmpty()) {
                Producto p = prods.get(0);
                if (p.getStock() > 0) {
                    int cant = Util.pedirNumeroConRango(scanner, "Cantidad (Max " + p.getStock() + "):", 1,
                            p.getStock());
                    detalles.add(new DetalleVenta(0, p.getId(), cant, p.getPrecio()));
                    System.out.println("Añadido: " + p.getNombre());
                } else {
                    System.out.println("Stock agotado.");
                }
            } else {
                System.out.println("Producto no encontrado.");
            }
            opcion = Util.pedirSiNO(scanner, "¿Añadir otro producto?");
        } while (opcion == 1);
        return detalles;
    }

    /**
     * Crea una nueva venta.
     * 
     * @param scanner Scanner para la entrada del usuario.
     */
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

}
