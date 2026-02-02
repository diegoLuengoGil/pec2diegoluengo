package com.inventario.service;

import java.util.List;

import com.inventario.model.Producto;
import com.inventario.repository.ProductoRepository;

/**
 * Clase que representa el servicio de productos
 */
public class ProductoService {

    /**
     * Repositorio de productos
     */
    private final ProductoRepository productoRepository;

    /**
     * Constructor
     * 
     * @param productoRepository el repositorio de productos
     */
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Lista todos los productos
     * 
     * @return la lista de productos
     */
    public List<Producto> listarProductos() {
        return productoRepository.obtenerProductos();
    }

    /**
     * Busca un producto por su ID
     * 
     * @param id el ID del producto
     * @return el producto encontrado
     */
    public Producto buscarProductoPorId(int id) {

        List<Producto> productos = productoRepository.buscarPorCampo("id_producto", id);
        Producto producto = null;
        if (!productos.isEmpty()) {
            producto = productos.get(0);
        }

        return producto;

    }

    /**
     * Inserta un producto
     * 
     * @param producto el producto a insertar
     */
    public void insertarProducto(Producto producto) {
        productoRepository.insertarProducto(producto);
    }

    /**
     * Actualiza un producto
     * 
     * @param id    el ID del producto
     * @param campo el campo a actualizar
     * @param valor el valor a actualizar
     * @return true si se actualizó el producto, false en caso contrario
     */
    public boolean actualizarProducto(int id, String campo, Object valor) {
        return productoRepository.actualizarProductoCampo(id, campo, valor);
    }

    /**
     * Elimina un producto
     * 
     * @param id el ID del producto
     * @return true si se eliminó el producto, false en caso contrario
     */
    public boolean eliminarProducto(int id) {
        return productoRepository.eliminarProducto(id);
    }
}
