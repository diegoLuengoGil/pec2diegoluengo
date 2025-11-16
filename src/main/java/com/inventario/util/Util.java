package com.inventario.util;

import java.util.Scanner;

public class Util {

    public static int pedirSiNO(Scanner scanner, String mensaje) {
        int opcion;
        do {
            System.out.println(mensaje + " (1: Sí, 2: No):");
            opcion = pedirNumero(scanner, "");
            if (opcion != 1 && opcion != 2) {
                System.out.println("Opción inválida. Por favor, ingresa 1 para Sí o 2 para No.");
            }
        } while (opcion != 1 && opcion != 2);
        return opcion;
    }

    public static boolean pedirSiNOBoolean(Scanner scanner, String mensaje) {
        int opcion;
        boolean esSi = false;
        do {
            System.out.println(mensaje + " (1: Sí, 2: No):");
            opcion = pedirNumero(scanner, "");
            if (opcion != 1 && opcion != 2) {
                System.out.println("Opción inválida. Por favor, ingresa 1 para Sí o 2 para No.");
            }
        } while (opcion != 1 && opcion != 2);

        if (opcion == 1) {
            esSi = true;
        }
        return esSi;
    }

    public static int pedirNumero(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida. " + mensaje);
            scanner.next(); // Limpiar la entrada invalida
        }
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        return numero;
    }

    public static int pedirNumeroConRango(Scanner scanner, String mensaje, int min, int max) {
        int numero;
        do {
            numero = pedirNumero(scanner, mensaje + " (entre " + min + " y " + max + "):");
            if (numero < min || numero > max) {
                System.out.println("Número fuera de rango. Inténtalo de nuevo.");
            }
        } while (numero < min || numero > max);
        return numero;
    }

    public static int pedirNumeroMinimo(Scanner scanner, String mensaje, int min) {
        int numero;
        do {
            numero = pedirNumero(scanner, mensaje + " (mínimo " + min + "):");
            if (numero < min) {
                System.out.println("Número menor que el mínimo. Inténtalo de nuevo.");
            }
        } while (numero < min);
        return numero;
    }

    public static int pedirNumeroMaximo(Scanner scanner, String mensaje, int max) {
        int numero;
        do {
            numero = pedirNumero(scanner, mensaje + " (máximo " + max + "):");
            if (numero > max) {
                System.out.println("Número mayor que el máximo. Inténtalo de nuevo.");
            }
        } while (numero > max);
        return numero;
    }

    public static String pedirTexto(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        String texto = scanner.nextLine();
        while (texto.isEmpty()) {
            System.out.println("Entrada vacía. Inténtalo de nuevo.");
            texto = scanner.nextLine();
        }
        return texto;
    }

    public static double pedirDecimal(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        while (!scanner.hasNextDouble()) {
            System.out.println("Entrada invalida. " + mensaje);
            scanner.next(); // Limpiar la entrada invalida
        }
        double numero = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer

        return numero;
    }

    public static double pedirDecimalMinimo(Scanner scanner, String mensaje, double min) {
        double numero;
        do {
            numero = pedirDecimal(scanner, mensaje + " (mínimo " + min + "):");
            if (numero < min) {
                System.out.println("Número menor que el mínimo. Inténtalo de nuevo.");
            }
        } while (numero < min);
        return numero;
    }

    public static double pedirDecimalMaximo(Scanner scanner, String mensaje, double max) {
        double numero;
        do {
            numero = pedirDecimal(scanner, mensaje + " (máximo " + max + "):");
            if (numero > max) {
                System.out.println("Número mayor que el máximo. Inténtalo de nuevo.");
            }
        } while (numero > max);
        return numero;
    }

    public static double pedirDecimalConRango(Scanner scanner, String mensaje, double min, double max) {
        double numero;
        do {
            numero = pedirDecimal(scanner, mensaje + " (entre " + min + " y " + max + "):");
            if (numero < min || numero > max) {
                System.out.println("Número fuera de rango. Inténtalo de nuevo.");
            }
        } while (numero < min || numero > max);
        return numero;
    }
}
