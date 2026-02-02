package com.inventario.view;

import java.util.List;
import java.util.Scanner;

import com.inventario.util.Util;

/**
 * Clase que representa la vista de las ventas
 */
public class VentaView {

    /**
     * Scanner para la entrada de datos
     */
    private final Scanner scanner;

    /**
     * Constructor de la clase VentaView
     * 
     * @param scanner Scanner para la entrada de datos
     */
    public VentaView(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Muestra el menú de consultas de ventas
     * 
     * @return la opción seleccionada
     */
    public int mostrarMenuConsultas() {
        System.out.println("\n====== CONSULTAS DE VENTAS ======");
        System.out.println("1. Listar todas las ventas (Resumen)");
        System.out.println("2. Consultar detalles de una venta por ID");
        System.out.println("0. Volver al menú principal");
        System.out.println("=================================");
        System.out.print("Selecciona una opción: ");
        return Util.pedirNumeroConRango(scanner, "", 0, 2);
    }

    /**
     * Muestra el resumen de las ventas
     * 
     * @param ventas lista de ventas
     */
    public void mostrarResumenVentas(List<String> ventas) {
        System.out.println("\n--- LISTADO DE VENTAS (RESUMEN) ---");
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
        } else {
            System.out.printf("%-5s | %-10s | %s\n", "ID VTA", "ID CLI", "CLIENTE");
            System.out.println("----------------------------------------------");
            for (String v : ventas) {
                System.out.println(v);
            }
        }
    }

    /**
     * Muestra los detalles de una venta
     * 
     * @param id       ID de la venta
     * @param detalles lista de detalles de la venta
     * @param total    total de la venta
     */
    public void mostrarDetallesVenta(int id, List<String> detalles, double total) {
        System.out.println("\n--- DETALLES DE VENTA ID " + id + " ---");
        if (detalles.isEmpty()) {
            System.out.println("No se encontraron detalles.");
        } else {
            System.out.printf("%-25s | %-10s | %-10s | %s\n", "PRODUCTO", "CANTIDAD", "PRECIO U.", "SUBTOTAL");
            System.out.println("---------------------------------------------------------------");
            for (String d : detalles) {
                System.out.println(d);
            }
            System.out.println("---------------------------------------------------------------");
            System.out.printf("TOTAL VENTA: %.2f\n", total);
        }
    }

    /**
     * Pide el ID de una venta
     * 
     * @return el ID de la venta
     */
    public int pedirIdVenta() {
        return Util.pedirNumeroMinimo(scanner, "Introduce el ID de la venta:", 1);
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
        return Util.pedirNumeroMinimo(scanner, "ID Cliente:", 1);
    }

    /**
     * Pide el ID de un producto
     * 
     * @return el ID del producto
     */
    public int pedirIdProducto() {
        return Util.pedirNumeroMinimo(scanner, "ID Producto:", 1);
    }

    /**
     * Pide la cantidad de un producto
     * 
     * @param max la cantidad máxima
     * @return la cantidad
     */
    public int pedirCantidad(int max) {
        return Util.pedirNumeroConRango(scanner, "Cantidad (Max " + max + "):", 1, max);
    }

    /**
     * Pide una respuesta S/N
     * 
     * @param mensaje el mensaje a mostrar
     * @return la respuesta S/N
     */
    public int pedirSiNo(String mensaje) {
        return Util.pedirSiNO(scanner, mensaje);
    }
}
