package com.inventario.controller;

import java.sql.SQLException;
import java.util.List;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.model.Cliente;
import com.inventario.service.ClienteService;
import com.inventario.view.ClienteView;

public class ClienteController {

    private ClienteService clienteService;
    private ClienteView clienteView;

    public ClienteController(ClienteService clienteService, ClienteView clienteView) {
        this.clienteService = clienteService;
        this.clienteView = clienteView;
    }

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

    private void listarClientes() {
        try {
            List<Cliente> clientes = clienteService.listarClientes();
            clienteView.mostrarClientes(clientes);
        } catch (SQLException e) {
            clienteView.mostrarError("Error al listar clientes: " + e.getMessage());
        }
    }

    private void insertarCliente() {
        try {
            Cliente c = clienteView.pedirDatosCliente();
            clienteService.insertarCliente(c);
            clienteView.mostrarMensaje("Cliente insertado correctamente.");
        } catch (Exception e) {
            clienteView.mostrarError("Error al insertar el cliente: " + e.getMessage());
        }
    }

    private void buscarClientePorId() {
        int id = clienteView.pedirIdCliente();
        try {
            Cliente c = clienteService.buscarClientePorId(id);
            if (c != null) {
                clienteView.mostrarCliente(c);
            } else {
                clienteView.mostrarMensaje("No se encontró ningún cliente con ese ID.");
            }
        } catch (SQLException e) {
            clienteView.mostrarError("Error al buscar el cliente: " + e.getMessage());
        } catch (DatoInvalidoException e) {
            clienteView.mostrarError("Datos del cliente inválidos: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        try {
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
        } catch (SQLException e) {
            clienteView.mostrarError("Error al eliminar el cliente: " + e.getMessage());
        }
    }

    private void actualizarCliente() {
        try {
            List<Cliente> clientes = clienteService.listarClientes();
            if (clientes.isEmpty()) {
                clienteView.mostrarMensaje("No hay clientes registrados.");
            } else {
                clienteView.mostrarClientes(clientes);

                int id = clienteView.pedirIdCliente();
                int opcion;

                do {
                    opcion = clienteView.pedirOpcionActualizar();
                    try {
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
                    } catch (SQLException e) {
                        clienteView.mostrarError("Error al actualizar: " + e.getMessage());
                    }
                } while (opcion != 0);
            }

        } catch (SQLException e) {
            clienteView.mostrarError("Error de BD: " + e.getMessage());
        }
    }

    private void agregarDinero() {
        try {
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
        } catch (SQLException e) {
            clienteView.mostrarError("Error al agregar dinero: " + e.getMessage());
        }
    }

    private void restarDinero() {
        try {
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
        } catch (SQLException e) {
            clienteView.mostrarError("Error al restar dinero: " + e.getMessage());
        }
    }
}
