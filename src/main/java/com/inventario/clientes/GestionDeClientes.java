package com.inventario.clientes;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.util.Util;

public class GestionDeClientes {

    private static void buscarClientePorId(Scanner scanner) {
        System.out.println("\n--- BUSCAR CLIENTE POR ID ---");

        int id = Util.pedirNumeroMinimo(scanner, "Introduce el ID del cliente:", 1);

        try {
            List<Cliente> resultados = ClientesBBDD.buscarPorCampo("id_cliente", id);

            if (resultados.isEmpty()) {
                System.out.println("No se encontró ningún cliente con ese ID.");
                return;
            }

            Cliente c = resultados.get(0);

            System.out.println("\nCliente encontrado:");
            System.out.println("ID: " + c.getId());
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("Email: " + c.getEmail());
            System.out.println("Teléfono: " + c.getTelefono());

        } catch (SQLException e) {
            System.err.println("Error al buscar el cliente en la base de datos: " + e.getMessage());
        } catch (DatoInvalidoException e){
            System.err.println("Error de dato inválido: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void eliminarCliente(Scanner scanner) {
        System.out.println("\n--- ELIMINAR CLIENTE ---");

        try {
            listarClientes(); // Mostrar clientes actuales

            int id = Util.pedirNumeroMinimo(scanner,
                    "Introduce el ID del cliente a eliminar:", 1);

            // Confirmación opcional (recomendable)
            boolean confirmar = Util.pedirSiNOBoolean(scanner,
                    "¿Estás seguro de que deseas eliminar el cliente con ID " + id + "?");

            if (!confirmar) {
                System.out.println("Operación cancelada.");
            } else {
                boolean eliminado = ClientesBBDD.eliminarCliente(id);

                if (eliminado) {
                    System.out.println("Cliente eliminado correctamente.");
                } else {
                    System.out.println("No se encontró ningún cliente con ese ID.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void actualizarCliente(Scanner scanner) {
        System.out.println("\n--- ACTUALIZAR CLIENTE ---");

        try {
            listarClientes(); // Mostrar listado antes de actualizar

            int id = Util.pedirNumeroMinimo(scanner, "Introduce el ID del cliente a actualizar:", 1);

            int opcion;

            do {
                System.out.println("\n¿Qué campo deseas actualizar?");
                System.out.println("1. Nombre");
                System.out.println("2. Email");
                System.out.println("3. Teléfono");
                System.out.println("0. Volver");
                System.out.print("Opción: ");

                opcion = Util.pedirNumeroConRango(scanner, "", 0, 3);

                switch (opcion) {
                    case 1 -> {
                        String nuevoNombre = Util.pedirTexto(scanner, "Nuevo nombre:");
                        ClientesBBDD.actualizarClienteCampo(id, "nombre", nuevoNombre);
                    }
                    case 2 -> {
                        String nuevoEmail = Util.pedirTexto(scanner, "Nuevo email:");
                        // Validar email (tu clase Cliente ya tiene validación)
                        ClientesBBDD.actualizarClienteCampo(id, "email", nuevoEmail);
                    }
                    case 3 -> {
                        String nuevoTelefono = Util.pedirTexto(scanner, "Nuevo teléfono:");
                        ClientesBBDD.actualizarClienteCampo(id, "telefono", nuevoTelefono);
                        System.out.println("Teléfono actualizado correctamente.");
                    }
                    case 0 -> System.out.println("Volviendo al menú anterior...");
                }

            } while (opcion != 0);

        } catch (SQLException e) {
            System.err.println("Error al actualizar el cliente: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void listarClientes() {
        System.out.println("\n--- LISTADO DE CLIENTES ---");

        try {
            List<Cliente> clientes = ClientesBBDD.obtenerClientes();

            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados.");
                return;
            }

            System.out.printf("%-5s %-20s %-30s %-15s%n", "ID", "Nombre", "Email", "Teléfono");
            System.out.println("------------------------------------------------------------------");

            for (Cliente cliente : clientes) {
                System.out.printf("%-5d %-20s %-30s %-15s%n",
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getEmail(),
                        cliente.getTelefono());
            }

        } catch (SQLException e) {
            System.err.println("Error al listar los clientes: " + e.getMessage());
        } catch (DatoInvalidoException e) {
            System.err.println("Error de dato inválido: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void insertarCliente(Scanner scanner) {
        System.out.println("\n--- INSERTAR CLIENTE ---");

        String nombre = Util.pedirTexto(scanner, "Introduce el nombre del cliente:");
        String email = Util.pedirTexto(scanner, "Introduce el email del cliente:");
        String telefono = Util.pedirTexto(scanner, "Introduce el teléfono del cliente:");

        try {
            // Crear el objeto cliente con validación interna
            Cliente nuevoCliente = new Cliente(nombre, email, telefono);

            // Insertar en la base de datos
            ClientesBBDD.insertarCliente(nuevoCliente);

            System.out.println("Cliente insertado correctamente.");

        } catch (DatoInvalidoException e) {
            System.err.println("Error en los datos del cliente: " + e.getMessage());

        } catch (SQLException e) {
            System.err.println("Error al insertar cliente en la base de datos: " + e.getMessage());

        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }

    public static void menuClientes(Scanner scanner) {
        int opcion;

        do {
            System.out.println("\n====== GESTIÓN DE CLIENTES ======");
            System.out.println("1. Insertar cliente");
            System.out.println("2. Actualizar cliente");
            System.out.println("3. Eliminar cliente");
            System.out.println("4. Listar clientes");
            System.out.println("5. Buscar cliente por ID");
            System.out.println("0. Volver al menú principal");
            System.out.println("=================================");
            System.out.print("Selecciona una opción: ");

            opcion = Util.pedirNumeroConRango(scanner, null, 0, 5);

            switch (opcion) {
                case 1 -> insertarCliente(scanner);
                case 2 -> actualizarCliente(scanner);
                case 3 -> eliminarCliente(scanner);
                case 4 -> listarClientes();
                case 5 -> buscarClientePorId(scanner);
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }
}
