package com.inventario.controller;

import java.util.Scanner;

import com.inventario.bbdd.ConexionBBDD;
import com.inventario.repository.ClienteRepository;
import com.inventario.repository.ProductoRepository;
import com.inventario.repository.VentaRepository;
import com.inventario.service.ClienteService;
import com.inventario.service.ProductoService;
import com.inventario.service.VentaService;
import com.inventario.view.ClienteView;
import com.inventario.view.MainView;
import com.inventario.view.ProductoView;
import com.inventario.view.VentaView;

public class MainController {

    private MainView mainView;
    private ProductoController productoController;
    private ClienteController clienteController;
    private VentaController ventaController;
    private Scanner scanner;

    public MainController() {
        this.scanner = new Scanner(System.in);
        this.mainView = new MainView(scanner);

        // Initialize Producto Component
        ProductoRepository productoRepository = new ProductoRepository();
        ProductoService productoService = new ProductoService(productoRepository);
        ProductoView productoView = new ProductoView(scanner);
        this.productoController = new ProductoController(productoService, productoView);

        // Initialize Cliente Component
        ClienteRepository clienteRepository = new ClienteRepository();
        ClienteService clienteService = new ClienteService(clienteRepository);
        ClienteView clienteView = new ClienteView(scanner);
        this.clienteController = new ClienteController(clienteService, clienteView);

        // Initialize Venta Component
        VentaRepository ventaRepository = new VentaRepository();
        VentaService ventaService = new VentaService(ventaRepository);
        VentaView ventaView = new VentaView(scanner);
        this.ventaController = new VentaController(ventaService, ventaView, productoService, productoView,
                clienteService, clienteView);
    }

    public void iniciar() {
        ConexionBBDD.iniciarConexion();
        int opcion;

        do {
            opcion = mainView.mostrarMenuPrincipal();

            switch (opcion) {
                case 1 -> productoController.iniciar();
                case 2 -> clienteController.iniciar();
                case 3 -> ventaController.crearVenta();
                case 4 -> ventaController.menuConsultas();
                case 0 -> mainView.mostrarMensaje("\nSaliendo del programa...");
                default -> mainView.mostrarMensaje("Opción no válida.");
            }
        } while (opcion != 0);

        scanner.close();
    }
}
