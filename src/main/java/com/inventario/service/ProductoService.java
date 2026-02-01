package com.inventario.service;

import java.sql.SQLException;
import java.util.List;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.model.Producto;
import com.inventario.repository.ProductoRepository;

public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() throws SQLException {
        return productoRepository.obtenerProductos();
    }

    public Producto buscarProductoPorId(int id) throws SQLException, DatoInvalidoException {

        List<Producto> productos = productoRepository.buscarPorCampo("id_producto", id);
        Producto producto = null;
        if (!productos.isEmpty()) {
            producto = productos.get(0);
        }

        return producto;

    }

    public void insertarProducto(Producto producto) throws SQLException {
        productoRepository.insertarProducto(producto);
    }

    public boolean actualizarProducto(int id, String campo, Object valor) throws SQLException {
        return productoRepository.actualizarProductoCampo(id, campo, valor);
    }

    public boolean eliminarProducto(int id) throws SQLException {
        return productoRepository.eliminarProducto(id);
    }
}
