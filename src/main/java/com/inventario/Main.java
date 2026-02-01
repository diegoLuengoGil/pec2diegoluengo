package com.inventario;

import com.inventario.controller.MainController;

/**
 * Clase principal del programa
 * 
 * @author Diego Luengo Gil
 */
public class Main {

    public static void main(String[] args) {
        MainController mainController = new MainController();
        mainController.iniciar();
    }
}