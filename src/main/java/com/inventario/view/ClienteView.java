package com.inventario.view;

import java.util.List;
import java.util.Scanner;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.model.Cliente;
import com.inventario.util.Util;

/**
 * Clase que representa la vista de los clientes
 */
public class ClienteView {

    private final Scanner scanner;

    public ClienteView(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Muestra el menú de gestión de clientes
     * 
     * @return la opción seleccionada
     */
    public int mostrarMenu() {
        System.out.println("\n====== GESTIÓN DE CLIENTES ======");
        System.out.println("1. Insertar cliente");
        System.out.println("2. Actualizar cliente");
        System.out.println("3. Eliminar cliente");
        System.out.println("4. Listar clientes");
        System.out.println("5. Buscar cliente por ID");
        System.out.println("6. Agregar dinero a cliente");
        System.out.println("7. Restar dinero a cliente");
        System.out.println("0. Volver al menú principal");
        System.out.println("=================================");
        System.out.print("Selecciona una opción: ");
        return Util.pedirNumeroConRango(scanner, "", 0, 7);
    }

    /**
     * Muestra la lista de clientes
     * 
     * @param clientes la lista de clientes
     */
    public void mostrarClientes(List<Cliente> clientes) {
        System.out.println("\n--- LISTADO DE CLIENTES ---");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.printf("%-5s %-20s %-25s %-15s %-10s%n", "ID", "Nombre", "Email", "Teléfono", "Dinero");
            System.out.println("---------------------------------------------------------------------------------");
            for (Cliente c : clientes) {
                System.out.printf("%-5d %-20s %-25s %-15s %-10.2f%n",
                        c.getId(),
                        c.getNombre(),
                        c.getEmail(),
                        c.getTelefono(),
                        c.getDinero());
            }
        }
    }

    /**
     * Muestra un cliente
     * 
     * @param c el cliente a mostrar
     */
    public void mostrarCliente(Cliente c) {
        System.out.println("\nCliente encontrado:");
        System.out.println("ID: " + c.getId());
        System.out.println("Nombre: " + c.getNombre());
        System.out.println("Email: " + c.getEmail());
        System.out.println("Teléfono: " + c.getTelefono());
        System.out.println("Dinero: " + c.getDinero());
    }

    /**
     * Muestra un mensaje
     * 
     * @param mensaje el mensaje a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Muestra un error
     * 
     * @param error el error a mostrar
     */
    public void mostrarError(String error) {
        System.err.println(error);
    }

    /**
     * Pide el ID de un cliente
     * 
     * @return el ID del cliente
     */
    public int pedirIdCliente() {
        return Util.pedirNumeroMinimo(scanner, "Introduce el ID del cliente:", 1);
    }

    /**
     * Pide los datos de un cliente
     * 
     * @return el cliente
     * @throws DatoInvalidoException si los datos son inválidos
     */
    public Cliente pedirDatosCliente() throws DatoInvalidoException {
        System.out.println("\n--- Insertar nuevo cliente ---");
        String nombre = Util.pedirTexto(scanner, "Introduce el nombre del cliente:");
        String email = Util.pedirTexto(scanner, "Introduce el email del cliente:");
        String telefono = Util.pedirTexto(scanner, "Introduce el teléfono del cliente:");
        double dinero = Util.pedirDecimalMinimo(scanner, "Introduce el dinero del cliente:", 0);

        return new Cliente(nombre, email, telefono, dinero);
    }

    /**
     * Pide la cantidad de dinero
     * 
     * @param mensaje el mensaje a mostrar
     * @return la cantidad de dinero
     */
    public double pedirCantidad(String mensaje) {
        return Util.pedirDecimalMinimo(scanner, mensaje, 0.01);
    }

    /**
     * Pide la opción de actualización
     * 
     * @return la opción de actualización
     */
    public int pedirOpcionActualizar() {
        System.out.println("\n¿Qué campo deseas actualizar?");
        System.out.println("1. Nombre");
        System.out.println("2. Email");
        System.out.println("3. Teléfono");
        System.out.println("0. Volver");
        System.out.print("Opción: ");
        return Util.pedirNumeroConRango(scanner, "", 0, 3);
    }

    /**
     * Pide el nuevo nombre
     * 
     * @return el nuevo nombre
     */
    public String pedirNuevoNombre() {
        return Util.pedirTexto(scanner, "Nuevo nombre:");
    }

    /**
     * Pide el nuevo email
     * 
     * @return el nuevo email
     */
    public String pedirNuevoEmail() {
        return Util.pedirTexto(scanner, "Nuevo email:");
    }

    /**
     * Pide el nuevo teléfono
     * 
     * @return el nuevo teléfono
     */
    public String pedirNuevoTelefono() {
        return Util.pedirTexto(scanner, "Nuevo teléfono:");
    }

    /**
     * Pide la confirmación de una acción
     * 
     * @param mensaje el mensaje a mostrar
     * @return la confirmación
     */
    public boolean pedirConfirmacion(String mensaje) {
        return Util.pedirSiNOBoolean(scanner, mensaje);
    }
}
