package com.inventario.view;

import java.util.Scanner;

import com.inventario.util.Util;

/**
 * Clase que representa la vista principal
 */
public class MainView {

    private final Scanner scanner;

    public MainView(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Muestra el menú principal
     * 
     * @return la opción seleccionada
     */
    public int mostrarMenuPrincipal() {
        System.out.println("\n========== MENÚ PRINCIPAL ==========");
        System.out.println("1. Gestión de productos");
        System.out.println("2. Gestión de clientes");
        System.out.println("3. Registrar venta");
        System.out.println("4. Consultar ventas");
        System.out.println("0. Salir");
        System.out.println("=====================================");
        System.out.print("Selecciona una opción: ");
        return Util.pedirNumeroConRango(scanner, "", 0, 4);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}
