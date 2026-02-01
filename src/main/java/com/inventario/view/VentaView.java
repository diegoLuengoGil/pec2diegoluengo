package com.inventario.view;

import java.util.List;
import java.util.Scanner;

import com.inventario.util.Util;

public class VentaView {

    private final Scanner scanner;

    public VentaView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int mostrarMenuConsultas() {
        System.out.println("\n====== CONSULTAS DE VENTAS ======");
        System.out.println("1. Listar todas las ventas (Resumen)");
        System.out.println("2. Consultar detalles de una venta por ID");
        System.out.println("0. Volver al menú principal");
        System.out.println("=================================");
        System.out.print("Selecciona una opción: ");
        return Util.pedirNumeroConRango(scanner, "", 0, 2);
    }

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

    public int pedirIdVenta() {
        return Util.pedirNumeroMinimo(scanner, "Introduce el ID de la venta:", 1);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println(error);
    }

    // Creating sale interaction
    public int pedirIdCliente() {
        return Util.pedirNumeroMinimo(scanner, "ID Cliente:", 1);
    }

    public int pedirIdProducto() {
        return Util.pedirNumeroMinimo(scanner, "ID Producto:", 1);
    }

    public int pedirCantidad(int max) {
        return Util.pedirNumeroConRango(scanner, "Cantidad (Max " + max + "):", 1, max);
    }

    public int pedirSiNo(String mensaje) {
        return Util.pedirSiNO(scanner, mensaje);
    }
}
