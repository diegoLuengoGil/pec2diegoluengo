package com.inventario.view;

import java.util.List;
import java.util.Scanner;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.model.Producto;
import com.inventario.util.Util;

/**
 * Clase que representa la vista de los productos
 */
public class ProductoView {

    /**
     * Scanner para la entrada de datos
     */
    private final Scanner scanner;

    /**
     * Constructor de la clase ProductoView
     * 
     * @param scanner Scanner para la entrada de datos
     */
    public ProductoView(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Muestra el menú de gestión de productos
     * 
     * @return la opción seleccionada
     */
    public int mostrarMenu() {
        System.out.println("\n====== GESTIÓN DE PRODUCTOS ======");
        System.out.println("1. Insertar producto");
        System.out.println("2. Actualizar producto");
        System.out.println("3. Eliminar producto");
        System.out.println("4. Listar productos");
        System.out.println("5. Buscar producto por ID");
        System.out.println("0. Volver al menú principal");
        System.out.println("===================================");
        System.out.print("Selecciona una opción: ");
        return Util.pedirNumeroConRango(scanner, "", 0, 5);
    }

    /**
     * Muestra la lista de productos
     * 
     * @param productos lista de productos
     */
    public void mostrarProductos(List<Producto> productos) {
        System.out.println("\n--- LISTADO DE PRODUCTOS ---");
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            // Using a simple table format or just listing
            System.out.printf("%-5s %-20s %-30s %-10s %-5s%n", "ID", "Nombre", "Descripción", "Precio", "Stock");
            System.out.println("---------------------------------------------------------------------------");
            for (Producto p : productos) {
                System.out.printf("%-5d %-20s %-30s %-10.2f %-5d%n",
                        p.getId(),
                        limitString(p.getNombre(), 20),
                        limitString(p.getDescripcion(), 30),
                        p.getPrecio(),
                        p.getStock());
            }
        }
    }

    /**
     * Muestra un producto
     * 
     * @param p el producto a mostrar
     */
    public void mostrarProducto(Producto p) {
        System.out.println("\nProducto encontrado:");
        System.out.println("ID: " + p.getId());
        System.out.println("Nombre: " + p.getNombre());
        System.out.println("Descripción: " + p.getDescripcion());
        System.out.println("Precio: " + p.getPrecio());
        System.out.println("Stock: " + p.getStock());
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
     * Pide el ID de un producto
     * 
     * @return el ID del producto
     */
    public int pedirIdProducto() {
        return Util.pedirNumeroMinimo(scanner, "Introduce el ID del producto:", 1);
    }

    /**
     * Limita una cadena de texto
     * 
     * @param str la cadena de texto
     * @param max el máximo de caracteres
     * @return la cadena limitada
     */
    private String limitString(String str, int max) {
        if (str == null)
            return "";
        if (str.length() > max)
            return str.substring(0, max - 3) + "...";
        return str;
    }

    /**
     * Pide los datos de un producto
     * 
     * @return el producto
     * @throws DatoInvalidoException si los datos son inválidos
     */
    public Producto pedirDatosProducto() throws DatoInvalidoException {
        System.out.println("\n--- Insertar nuevo producto ---");
        String nombre = Util.pedirTexto(scanner, "Introduce el nombre del producto:");
        String descripcion = Util.pedirTexto(scanner, "Introduce la descripción del producto:");
        double precio = Util.pedirDecimalMinimo(scanner, "Introduce el precio del producto:", 0.00);
        int stock = Util.pedirNumeroMinimo(scanner, "Introduce el stock inicial del producto:", 0);

        return new Producto(nombre, descripcion, precio, stock);
    }

    /**
     * Pide la opción de actualización
     * 
     * @return la opción de actualización
     */
    public int pedirOpcionActualizar() {
        System.out.println("\n¿Qué campo deseas actualizar?");
        System.out.println("1. Nombre");
        System.out.println("2. Descripción");
        System.out.println("3. Precio de venta");
        System.out.println("4. Stock");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
        return Util.pedirNumeroMinimo(scanner, "", 0);
    }

    /**
     * Pide el nuevo nombre
     * 
     * @return el nuevo nombre
     */
    public String pedirNuevoNombre() {
        return Util.pedirTexto(scanner, "Nuevo nombre:");
    }

    /**
     * Pide la nueva descripción
     * 
     * @return la nueva descripción
     */
    public String pedirNuevaDescripcion() {
        return Util.pedirTexto(scanner, "Nueva descripción:");
    }

    /**
     * Pide el nuevo precio
     * 
     * @return el nuevo precio
     */
    public double pedirNuevoPrecio() {
        return Util.pedirDecimalMinimo(scanner, "Nuevo precio:", 0);
    }

    /**
     * Pide el nuevo stock
     * 
     * @return el nuevo stock
     */
    public int pedirNuevoStock() {
        return Util.pedirNumeroMinimo(scanner, "Nuevo stock:", 0);
    }
}
