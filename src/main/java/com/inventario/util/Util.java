package com.inventario.util;

import java.util.Scanner;

/**
 * Clase que contiene métodos utilitarios para la entrada de datos.
 */
public class Util {

    /**
     * Pide al usuario que ingrese una opción (1: Sí, 2: No).
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @return 1 si el usuario elige Sí, 2 si elige No.
     */
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

    /**
     * Pide al usuario que ingrese una opción (1: Sí, 2: No) y devuelve un booleano.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @return true si el usuario elige Sí, false si elige No.
     */
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

    /**
     * Pide al usuario que ingrese un número entero.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @return Número entero ingresado por el usuario.
     */
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

    /**
     * Pide al usuario que ingrese un número entero dentro de un rango.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @param min     Valor mínimo del rango.
     * @param max     Valor máximo del rango.
     * @return Número entero dentro del rango.
     */
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

    /**
     * Pide al usuario que ingrese un número entero mayor o igual a un valor mínimo.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @param min     Valor mínimo del rango.
     * @return Número entero mayor o igual al valor mínimo.
     */
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

    /**
     * Pide al usuario que ingrese un número entero menor o igual a un valor máximo.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @param max     Valor máximo del rango.
     * @return Número entero menor o igual al valor máximo.
     */
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

    /**
     * Pide al usuario que ingrese un texto.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @return Texto ingresado por el usuario.
     */
    public static String pedirTexto(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        String texto = scanner.nextLine();
        while (texto.isEmpty()) {
            System.out.println("Entrada vacía. Inténtalo de nuevo.");
            texto = scanner.nextLine();
        }
        return texto;
    }

    /**
     * Pide al usuario que ingrese un número decimal.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @return Número decimal ingresado por el usuario.
     */
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

    /**
     * Pide al usuario que ingrese un número decimal mayor o igual a un valor
     * mínimo.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @param min     Valor mínimo del rango.
     * @return Número decimal mayor o igual al valor mínimo.
     */
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

    /**
     * Pide al usuario que ingrese un número decimal menor o igual a un valor
     * máximo.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @param max     Valor máximo del rango.
     * @return Número decimal menor o igual al valor máximo.
     */
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

    /**
     * Pide al usuario que ingrese un número decimal dentro de un rango.
     * 
     * @param scanner Scanner para leer la entrada del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @param min     Valor mínimo del rango.
     * @param max     Valor máximo del rango.
     * @return Número decimal dentro del rango.
     */
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
