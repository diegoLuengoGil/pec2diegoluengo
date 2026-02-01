package com.inventario.view;

import java.util.List;
import java.util.Scanner;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.model.Cliente;
import com.inventario.util.Util;

public class ClienteView {

    private final Scanner scanner;

    public ClienteView(Scanner scanner) {
        this.scanner = scanner;
    }

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
                        limitString(c.getNombre(), 20),
                        limitString(c.getEmail(), 25),
                        c.getTelefono(),
                        c.getDinero());
            }
        }
    }

    private String limitString(String str, int max) {
        if (str == null)
            return "";
        if (str.length() > max)
            return str.substring(0, max - 3) + "...";
        return str;
    }

    public void mostrarCliente(Cliente c) {
        System.out.println("\nCliente encontrado:");
        System.out.println("ID: " + c.getId());
        System.out.println("Nombre: " + c.getNombre());
        System.out.println("Email: " + c.getEmail());
        System.out.println("Teléfono: " + c.getTelefono());
        System.out.println("Dinero: " + c.getDinero());
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println(error);
    }

    public int pedirIdCliente() {
        return Util.pedirNumeroMinimo(scanner, "Introduce el ID del cliente:", 1);
    }

    public Cliente pedirDatosCliente() throws DatoInvalidoException {
        System.out.println("\n--- Insertar nuevo cliente ---");
        String nombre = Util.pedirTexto(scanner, "Introduce el nombre del cliente:");
        String email = Util.pedirTexto(scanner, "Introduce el email del cliente:");
        String telefono = Util.pedirTexto(scanner, "Introduce el teléfono del cliente:");
        double dinero = Util.pedirDecimalMinimo(scanner, "Introduce el dinero del cliente:", 0);

        return new Cliente(nombre, email, telefono, dinero);
    }

    public double pedirCantidad(String mensaje) {
        return Util.pedirDecimalMinimo(scanner, mensaje, 0.01);
    }

    public int pedirOpcionActualizar() {
        System.out.println("\n¿Qué campo deseas actualizar?");
        System.out.println("1. Nombre");
        System.out.println("2. Email");
        System.out.println("3. Teléfono");
        System.out.println("0. Volver");
        System.out.print("Opción: ");
        return Util.pedirNumeroConRango(scanner, "", 0, 3);
    }

    public String pedirNuevoNombre() {
        return Util.pedirTexto(scanner, "Nuevo nombre:");
    }

    public String pedirNuevoEmail() {
        return Util.pedirTexto(scanner, "Nuevo email:");
    }

    public String pedirNuevoTelefono() {
        return Util.pedirTexto(scanner, "Nuevo teléfono:");
    }

    public boolean pedirConfirmacion(String mensaje) {
        return Util.pedirSiNOBoolean(scanner, mensaje);
    }
}
