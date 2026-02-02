package com.inventario.model;

import com.inventario.excepciones.DatoInvalidoException;
import jakarta.persistence.*;

/**
 * Clase que representa un detalle de venta.
 */
@Entity
@Table(name = "DetalleVenta")
public class DetalleVenta {
    /**
     * ID del detalle de venta.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private int id;
    /**
     * Venta asociada.
     */
    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;
    /**
     * Producto asociado.
     */
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
    /**
     * Cantidad del producto.
     */
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    /**
     * Precio unitario del producto.
     */
    @Column(name = "precio_unitario", nullable = false)
    private double precioUnitario;

    /**
     * Constructor vacío para JPA.
     */
    public DetalleVenta() {
    }

    /**
     * Obtiene el ID de la venta.
     * 
     * @return el ID de la venta
     */
    public int getIdVenta() {
        return venta != null ? venta.getId() : 0;
    }

    /**
     * Obtiene el ID del producto.
     * 
     * @return el ID del producto
     */
    public int getIdProducto() {
        return producto != null ? producto.getId() : 0;
    }

    /**
     * Establece el ID de la venta.
     * 
     * @param idVenta el ID de la venta
     */
    public void setIdVenta(int idVenta) {
        if (this.venta == null)
            this.venta = new Venta();
        this.venta.setId(idVenta);
    }

    /**
     * Establece el ID del producto.
     * 
     * @param idProducto el ID del producto
     */
    public void setIdProducto(int idProducto) {
        if (this.producto == null)
            this.producto = new Producto();
        this.producto.setId(idProducto);
    }

    /*
     * Aqui empieza el modelo original
     */

    /**
     * Constructor para crear un nuevo detalle de venta con objetos.
     * 
     * @param id             ID del detalle.
     * @param venta          Venta asociada.
     * @param producto       Producto asociado.
     * @param cantidad       Cantidad.
     * @param precioUnitario Precio unitario.
     * @throws DatoInvalidoException
     */
    public DetalleVenta(int id, Venta venta, Producto producto, int cantidad, double precioUnitario)
            throws DatoInvalidoException {
        validarCantidad(cantidad);
        this.id = id;
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Constructor para crear un nuevo detalle de venta sin ID.
     * 
     * @param venta          Venta asociada.
     * @param producto       Producto asociado.
     * @param cantidad       Cantidad.
     * @param precioUnitario Precio unitario.
     * @throws DatoInvalidoException
     */
    public DetalleVenta(Venta venta, Producto producto, int cantidad, double precioUnitario)
            throws DatoInvalidoException {
        validarCantidad(cantidad);
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Constructor de compatibilidad (usado en VentaController).
     * El objeto Venta será null y el Producto debe ser pasado correctamente (aunque
     * aqui simulamos con ID si tenemos Producto o no).
     * Como VentaController pasa IDs y precio, aqui tenemos un problema.
     * VentaController hace: new DetalleVenta(0, 0, p.getId(), cant, p.getPrecio())
     * Pero p.getId() es un int. No podemos asignar int a Producto.
     * Necesitamos un constructor que acepte (int, int, int, int, double) y cree un
     * Producto dummy?
     * No, Hibernate fallará al persistir.
     * Pero esto es para JDBC por ahora. Para JDBC solo necesitamos getIdProducto().
     * Crearemos un objeto Producto dummy con ese ID para que getIdProducto()
     * funcione.
     */
    public DetalleVenta(int id, int idVenta, int idProducto, int cantidad, double precioUnitario)
            throws DatoInvalidoException {
        validarCantidad(cantidad);
        this.id = id;
        // Dummy Venta and Producto for ID holding
        this.venta = new Venta();
        this.venta.setId(idVenta);

        this.producto = new Producto();
        this.producto.setId(idProducto);

        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Valida que la cantidad sea mayor que cero.
     * 
     * @param cantidad Cantidad del producto.
     * @throws DatoInvalidoException Si la cantidad es inválida.
     */
    private void validarCantidad(int cantidad) throws DatoInvalidoException {
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor que cero.");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}