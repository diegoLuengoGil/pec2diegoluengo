package com.inventario.informes;

import com.inventario.util.Util;
import java.util.Scanner;

public class GestionInformes {

    /**
     * Menú para seleccionar la tabla a inspeccionar.
     *
     * @param scanner Scanner para la entrada del usuario.
     */
    public static void menuInformesAvanzados(Scanner scanner) {
        int opcion;

        do {
            System.out.println("\n====== 5. INFORMES AVANZADOS ======");
            System.out.println("1. Informe de Clientes");
            System.out.println("2. Informe de Productos");
            System.out.println("3. Informe de Ventas");
            System.out.println("4. Informe de Detalle de Venta");
            System.out.println("0. Volver al menú principal");
            System.out.println("======================================================");
            System.out.print("Selecciona una opción: ");

            // Asume que Util.pedirNumeroConRango existe y funciona
            opcion = Util.pedirNumeroConRango(scanner, null, 0, 4);

            switch (opcion) {
                case 1 -> InformesBBDD.informeGenericoTabla("cliente");
                case 2 -> InformesBBDD.informeGenericoTabla("producto");
                case 3 -> InformesBBDD.informeGenericoTabla("venta");
                case 4 -> InformesBBDD.informeGenericoTabla("detalleVenta");
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }
}