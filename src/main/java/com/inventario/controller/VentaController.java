package com.inventario.controller;

import java.util.ArrayList;
import java.util.List;

import com.inventario.excepciones.DatoInvalidoException;

import com.inventario.model.Cliente;
import com.inventario.model.DetalleVenta;
import com.inventario.model.Producto;
import com.inventario.service.ClienteService;
import com.inventario.service.ProductoService;
import com.inventario.service.VentaService;
import com.inventario.view.ClienteView;
import com.inventario.view.ProductoView;
import com.inventario.view.VentaView;

/**
 * Clase que gestiona las operaciones de ventas.
 */
public class VentaController {

    private VentaService ventaService;
    private VentaView ventaView;
    private ProductoService productoService;
    private ProductoView productoView;
    private ClienteService clienteService;
    private ClienteView clienteView;

    /**
     * Constructor de la clase VentaController.
     * 
     * @param ventaService    el servicio de ventas
     * @param ventaView       la vista de ventas
     * @param productoService el servicio de productos
     * @param productoView    la vista de productos
     * @param clienteService  el servicio de clientes
     * @param clienteView     la vista de clientes
     */
    public VentaController(VentaService ventaService, VentaView ventaView,
            ProductoService productoService, ProductoView productoView,
            ClienteService clienteService, ClienteView clienteView) {
        this.ventaService = ventaService;
        this.ventaView = ventaView;
        this.productoService = productoService;
        this.productoView = productoView;
        this.clienteService = clienteService;
        this.clienteView = clienteView;
    }

    /**
     * Muestra el menú de consultas y ejecuta la opción seleccionada.
     */
    public void menuConsultas() {
        int opcion;
        do {
            opcion = ventaView.mostrarMenuConsultas();
            switch (opcion) {
                case 1 -> listarResumenVentas();
                case 2 -> consultarDetallesVenta();
                case 0 -> ventaView.mostrarMensaje("Volviendo al menú principal...");
            }
        } while (opcion != 0);
    }

    /**
     * Lista un resumen de todas las ventas.
     */
    private void listarResumenVentas() {
        List<String> ventas = ventaService.listarResumenVentas();
        ventaView.mostrarResumenVentas(ventas);
    }

    /**
     * Consulta los detalles de una venta específica.
     */
    private void consultarDetallesVenta() {
        int id = ventaView.pedirIdVenta();

        List<String> detalles = ventaService.listarDetallesVenta(id);
        double total = ventaService.obtenerTotalVenta(id);
        // Verify if exists? If empty, maybe not found or just empty.
        if (!detalles.isEmpty()) {
            ventaView.mostrarDetallesVenta(id, detalles, total);
        } else {
            ventaView.mostrarMensaje("No se encontraron detalles para la venta ID " + id);
        }

    }

    /**
     * Crea una nueva venta.
     */
    public void crearVenta() {
        ventaView.mostrarMensaje("\n--- CREAR VENTA ---");

        List<Producto> productos = productoService.listarProductos();
        List<Cliente> clientes = clienteService.listarClientes();

        if (productos.isEmpty()) {
            ventaView.mostrarMensaje("No hay productos registrados.");
        } else if (clientes.isEmpty()) {
            ventaView.mostrarMensaje("No hay clientes registrados. Crea un cliente primero.");
        } else {
            int idCliente = -1;
            boolean clienteValido = false;
            do {
                clienteView.mostrarClientes(clientes);
                idCliente = ventaView.pedirIdCliente();

                if (clienteService.buscarClientePorId(idCliente) != null) {
                    clienteValido = true;
                } else {
                    ventaView.mostrarMensaje("Cliente no encontrado. Inténtalo de nuevo.");
                }

            } while (!clienteValido);

            // Seleccionar Productos
            List<DetalleVenta> detalles = new ArrayList<>();
            double totalEstimado = 0.0;
            int opcion;

            do {
                productoView.mostrarProductos(productos);
                int idProd = ventaView.pedirIdProducto();

                try {
                    Producto p = productoService.buscarProductoPorId(idProd);
                    if (p != null) {
                        if (p.getStock() > 0) {
                            int cant = ventaView.pedirCantidad(p.getStock());
                            detalles.add(new DetalleVenta(0, 0, p.getId(), cant, p.getPrecio()));
                            totalEstimado += (p.getPrecio() * cant);
                            ventaView.mostrarMensaje("Añadido: " + p.getNombre() + " x" + cant);
                        } else {
                            ventaView.mostrarMensaje("Stock agotado para este producto.");
                        }
                    } else {
                        ventaView.mostrarMensaje("Producto no encontrado.");
                    }
                } catch (DatoInvalidoException e) {
                    ventaView.mostrarMensaje("Error en datos del producto: " + e.getMessage());
                }

                opcion = ventaView.pedirSiNo("¿Añadir otro producto?");
            } while (opcion == 1);

            if (detalles.isEmpty()) {
                ventaView.mostrarMensaje("No se han seleccionado productos. Cancelando venta.");
            } else {
                // Confirmación
                ventaView.mostrarMensaje(String.format("\n>> TOTAL A PAGAR: %.2f €", totalEstimado));
                if (ventaView.pedirSiNo("¿Confirmar venta y realizar cobro?") == 1) {

                    ventaService.realizarVenta(idCliente, detalles);
                    ventaView.mostrarMensaje(
                            "¡Venta realizada con éxito! Inventario actualizado y saldo descontado.");

                } else {
                    ventaView.mostrarMensaje("Venta cancelada por el usuario.");
                }
            }
        }

    }
}
