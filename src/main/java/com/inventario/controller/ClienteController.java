package com.inventario.controller;

import java.util.List;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.model.Cliente;
import com.inventario.service.ClienteService;
import com.inventario.view.ClienteView;

/**
 * Controlador de clientes.
 */
public class ClienteController {

    /**
     * Servicio de clientes.
     */
    private ClienteService clienteService;

    /**
     * Vista de clientes.
     */
    private ClienteView clienteView;

    /**
     * Constructor de la clase ClienteController.
     */
    public ClienteController(ClienteService clienteService, ClienteView clienteView) {
        this.clienteService = clienteService;
        this.clienteView = clienteView;
    }

    /**
     * Inicia el controlador.
     */
    public void iniciar() {
        int opcion;
        do {
            opcion = clienteView.mostrarMenu();
            switch (opcion) {
                case 1 -> insertarCliente();
                case 2 -> actualizarCliente();
                case 3 -> eliminarCliente();
                case 4 -> listarClientes();
                case 5 -> buscarClientePorId();
                case 6 -> agregarDinero();
                case 7 -> restarDinero();
                case 0 -> clienteView.mostrarMensaje("Volviendo al menú principal...");
                default -> clienteView.mostrarMensaje("Opción no válida.");
            }
        } while (opcion != 0);
    }

    /**
     * Lista todos los clientes.
     */
    private void listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        clienteView.mostrarClientes(clientes);
    }

    /**
     * Inserta un nuevo cliente.
     */
    private void insertarCliente() {
        try {
            Cliente c = clienteView.pedirDatosCliente();
            clienteService.insertarCliente(c);
            clienteView.mostrarMensaje("Cliente insertado correctamente.");
        } catch (DatoInvalidoException e) {
            clienteView.mostrarError("Datos invalidos: " + e.getMessage());
        }
    }

    /**
     * Busca un cliente por su ID.
     */
    private void buscarClientePorId() {
        int id = clienteView.pedirIdCliente();

        Cliente c = clienteService.buscarClientePorId(id);
        if (c != null) {
            clienteView.mostrarCliente(c);
        } else {
            clienteView.mostrarMensaje("No se encontró ningún cliente con ese ID.");
        }

    }

    /**
     * Elimina un cliente.
     */
    private void eliminarCliente() {

        List<Cliente> clientes = clienteService.listarClientes();
        if (clientes.isEmpty()) {
            clienteView.mostrarMensaje("No hay clientes registrados.");
        } else {
            clienteView.mostrarClientes(clientes);

            int id = clienteView.pedirIdCliente();
            boolean confirmar = clienteView
                    .pedirConfirmacion("¿Estás seguro de que deseas eliminar el cliente con ID " + id + "?");

            if (confirmar) {
                if (clienteService.eliminarCliente(id)) {
                    clienteView.mostrarMensaje("Cliente eliminado correctamente.");
                } else {
                    clienteView.mostrarMensaje("No se encontró ningún cliente con ese ID.");
                }
            } else {
                clienteView.mostrarMensaje("Operación cancelada.");
            }
        }

    }

    /**
     * Actualiza un cliente.
     */
    private void actualizarCliente() {

        List<Cliente> clientes = clienteService.listarClientes();
        if (clientes.isEmpty()) {
            clienteView.mostrarMensaje("No hay clientes registrados.");
        } else {
            clienteView.mostrarClientes(clientes);

            int id = clienteView.pedirIdCliente();
            int opcion;

            do {
                opcion = clienteView.pedirOpcionActualizar();

                boolean exito = false;
                switch (opcion) {
                    case 1 ->
                        exito = clienteService.actualizarCliente(id, "nombre", clienteView.pedirNuevoNombre());
                    case 2 ->
                        exito = clienteService.actualizarCliente(id, "email", clienteView.pedirNuevoEmail());
                    case 3 -> exito = clienteService.actualizarCliente(id, "telefono",
                            clienteView.pedirNuevoTelefono());
                    case 0 -> clienteView.mostrarMensaje("Volviendo al menú anterior...");
                }
                if (opcion != 0 && exito)
                    clienteView.mostrarMensaje("Cliente actualizado correctamente.");

            } while (opcion != 0);
        }

    }

    /**
     * Agrega dinero a un cliente.
     */
    private void agregarDinero() {

        List<Cliente> clientes = clienteService.listarClientes();
        if (clientes.isEmpty()) {
            clienteView.mostrarMensaje("No hay clientes registrados.");
        } else {
            clienteView.mostrarClientes(clientes);

            int id = clienteView.pedirIdCliente();
            double cantidad = clienteView.pedirCantidad("Cantidad a agregar:");

            if (clienteService.agregarDinero(id, cantidad)) {
                clienteView.mostrarMensaje("Dinero agregado correctamente.");
            } else {
                clienteView.mostrarMensaje("No se pudo agregar el dinero.");
            }
        }

    }

    /**
     * Resta dinero a un cliente.
     */
    private void restarDinero() {

        List<Cliente> clientes = clienteService.listarClientes();
        if (clientes.isEmpty()) {
            clienteView.mostrarMensaje("No hay clientes registrados.");
        } else {
            clienteView.mostrarClientes(clientes);

            int id = clienteView.pedirIdCliente();
            double cantidad = clienteView.pedirCantidad("Cantidad a restar:");

            if (clienteService.restarDinero(id, cantidad)) {
                clienteView.mostrarMensaje("Dinero restado correctamente.");
            } else {
                clienteView.mostrarMensaje("No se pudo restar el dinero (ID incorrecto o saldo insuficiente).");
            }
        }

    }
}
