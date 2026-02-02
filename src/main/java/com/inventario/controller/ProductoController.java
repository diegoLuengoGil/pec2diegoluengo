package com.inventario.controller;

import java.util.List;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.model.Producto;
import com.inventario.service.ProductoService;
import com.inventario.view.ProductoView;

/**
 * Clase que gestiona las operaciones de productos.
 */
public class ProductoController {

    private ProductoService productoService;
    private ProductoView productoView;

    /**
     * Constructor de la clase ProductoController.
     * 
     * @param productoService el servicio de productos
     * @param productoView    la vista de productos
     */
    public ProductoController(ProductoService productoService, ProductoView productoView) {
        this.productoService = productoService;
        this.productoView = productoView;
    }

    /**
     * Inicia el controlador.
     */
    public void iniciar() {
        int opcion;
        do {
            opcion = productoView.mostrarMenu();
            switch (opcion) {
                case 1 -> insertarProducto();
                case 2 -> actualizarProducto();
                case 3 -> eliminarProducto();
                case 4 -> listarProductos();
                case 5 -> buscarProductoPorId();
                case 0 -> productoView.mostrarMensaje("Volviendo al menú principal...");
                default -> productoView.mostrarMensaje("Opción no válida.");
            }
        } while (opcion != 0);
    }

    /**
     * Lista todos los productos.
     */
    private void listarProductos() {
        List<Producto> productos = productoService.listarProductos();
        productoView.mostrarProductos(productos);
    }

    /**
     * Busca un producto por su ID.
     */
    private void buscarProductoPorId() {
        int id = productoView.pedirIdProducto();

        Producto p = productoService.buscarProductoPorId(id);
        if (p != null) {
            productoView.mostrarProducto(p);
        } else {
            productoView.mostrarMensaje("No se encontró ningún producto con ese ID.");
        }

    }

    /**
     * Inserta un nuevo producto.
     */
    private void insertarProducto() {
        try {
            Producto p = productoView.pedirDatosProducto();
            productoService.insertarProducto(p);
            productoView.mostrarMensaje("Producto insertado correctamente.");
        } catch (DatoInvalidoException e) {
            productoView.mostrarError("Datos invalidos: " + e.getMessage());
        }
    }

    /**
     * Actualiza un producto.
     */
    private void actualizarProducto() {

        List<Producto> productos = productoService.listarProductos();
        if (productos.isEmpty()) {
            productoView.mostrarMensaje("No hay productos registrados.");

        } else {
            productoView.mostrarProductos(productos);

            int id = productoView.pedirIdProducto();

            // Verify existence? Service logic?
            // Better to check if existence before asking what to update, but legacy code
            // did this way.
            // I'll stick to legacy flow logic which is inside the loop.

            int opcion;
            do {
                opcion = productoView.pedirOpcionActualizar();

                boolean exito = false;
                switch (opcion) {
                    case 1 -> {
                        String nuevoNombre = productoView.pedirNuevoNombre();
                        exito = productoService.actualizarProducto(id, "nombre", nuevoNombre);
                    }
                    case 2 -> {
                        String nuevaDesc = productoView.pedirNuevaDescripcion();
                        exito = productoService.actualizarProducto(id, "descripcion", nuevaDesc);
                    }
                    case 3 -> {
                        double nuevoPrecio = productoView.pedirNuevoPrecio();
                        exito = productoService.actualizarProducto(id, "precio", nuevoPrecio);
                    }
                    case 4 -> {
                        int nuevoStock = productoView.pedirNuevoStock();
                        exito = productoService.actualizarProducto(id, "stock", nuevoStock);
                    }
                    case 0 -> productoView.mostrarMensaje("Volviendo al menú anterior...");
                    default -> productoView.mostrarMensaje("Opción no válida.");
                }
                if (opcion != 0 && exito)
                    productoView.mostrarMensaje("Producto actualizado correctamente.");
                // if (opcion != 0 && !exito) // Could mean ID not found

            } while (opcion != 0);
        }

    }

    /**
     * Elimina un producto.
     */
    private void eliminarProducto() {

        List<Producto> productos = productoService.listarProductos();
        if (productos.isEmpty()) {
            productoView.mostrarMensaje("No hay productos registrados.");

        } else {
            productoView.mostrarProductos(productos);

            int id = productoView.pedirIdProducto();
            boolean eliminado = productoService.eliminarProducto(id);
            if (eliminado) {
                productoView.mostrarMensaje("Producto eliminado correctamente.");
            } else {
                productoView.mostrarMensaje("No se encontró ningún producto con ese ID.");
            }
        }

    }
}
