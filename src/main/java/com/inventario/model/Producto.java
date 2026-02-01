package com.inventario.model;

import com.inventario.excepciones.DatoInvalidoException;
import jakarta.persistence.*;

/**
 * Clase que representa un producto con sus atributos y métodos.
 */
@Entity
@Table(name = "producto")
public class Producto {
    /**
     * ID del producto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private int id;
    /**
     * Nombre del producto.
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;
    /**
     * Descripción del producto.
     */
    @Column(name = "descripcion")
    private String descripcion;
    /**
     * Precio del producto.
     */
    @Column(name = "precio", nullable = false)
    private double precio;
    /**
     * Stock del producto.
     */
    @Column(name = "stock", nullable = false)
    private int stock;

    /**
     * Constructor vacío para JPA.
     */
    public Producto() {
    }

    /**
     * Constructor que crea un nuevo producto con todos los atributos.
     *
     * @param idProducto  El ID del producto.
     * @param nombre      El nombre del producto.
     * @param descripcion La descripción del producto.
     * @param precio      El precio del producto.
     * @param stock       El stock del producto.
     * @throws DatoInvalidoException Si los datos del producto son inválidos.
     */
    public Producto(int idProducto, String nombre, String descripcion, double precio, int stock)
            throws DatoInvalidoException {

        validarNombre(nombre);
        validarPrecio(precio);
        validarStock(stock);

        this.id = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;

    }

    /**
     * Constructor que crea un nuevo producto con todos los atributos excepto el ID.
     *
     * @param nombre      El nombre del producto.
     * @param descripcion La descripción del producto.
     * @param precio      El precio del producto.
     * @param stock       El stock del producto.
     * @throws DatoInvalidoException Si los datos del producto son inválidos.
     */
    public Producto(String nombre, String descripcion, double precio, int stock) throws DatoInvalidoException {

        validarNombre(nombre);
        validarPrecio(precio);
        validarStock(stock);

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;

    }

    /**
     * Valida el nombre del producto.
     *
     * @param nombre El nombre del producto.
     * @throws DatoInvalidoException Si el nombre del producto es inválido.
     */
    private void validarNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del producto no puede estar vacio.");
        }
    }

    /**
     * Valida el precio del producto.
     *
     * @param precio El precio del producto.
     * @throws DatoInvalidoException Si el precio del producto es inválido.
     */
    private void validarPrecio(double precio) throws DatoInvalidoException {
        if (precio < 0) {
            throw new DatoInvalidoException("El precio del producto debe ser un valor válido y no puede ser nulo.");
        }
    }

    /**
     * Valida el stock del producto.
     *
     * @param stock El stock del producto.
     * @throws DatoInvalidoException Si el stock del producto es inválido.
     */
    private void validarStock(int stock) throws DatoInvalidoException {
        if (stock < 0) {
            throw new DatoInvalidoException("El stock no puede ser negativo.");
        }
    }

    /**
     * Obtiene el ID del producto.
     *
     * @return El ID del producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del producto.
     *
     * @param id El ID del producto.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre El nombre del producto.
     * @throws DatoInvalidoException Si el nombre del producto es inválido.
     */
    public void setNombre(String nombre) throws DatoInvalidoException {
        validarNombre(nombre);
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del producto.
     *
     * @return La descripción del producto.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del producto.
     *
     * @param descripcion La descripción del producto.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return El precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio El precio del producto.
     * @throws DatoInvalidoException Si el precio del producto es inválido.
     */
    public void setPrecio(double precio) throws DatoInvalidoException {
        validarPrecio(precio);
        this.precio = precio;
    }

    /**
     * Obtiene el stock del producto.
     *
     * @return El stock del producto.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece el stock del producto.
     *
     * @param stock El stock del producto.
     * @throws DatoInvalidoException Si el stock del producto es inválido.
     */
    public void setStock(int stock) throws DatoInvalidoException {
        validarStock(stock);
        this.stock = stock;
    }

}
