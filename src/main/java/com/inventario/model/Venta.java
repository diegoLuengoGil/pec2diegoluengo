package com.inventario.model;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

/**
 * Clase que representa una venta.
 */
@Entity
@Table(name = "venta")
public class Venta {
    /** Identificador de la venta */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    // 🔹 MÉTODOS DE CONVENIENCIA (MUY IMPORTANTE)
    public void addDetalle(DetalleVenta d) {
        detalles.add(d);
        d.setVenta(this);
    }

    public void removeDetalle(DetalleVenta d) {
        detalles.remove(d);
        d.setVenta(null);
    }

    /**
     * Constructor vacío para JPA.
     */
    public Venta() {
    }

    /**
     * Constructor de la clase.
     * 
     * @param id      Id de la venta en la base de datos.
     * @param cliente Cliente de la venta.
     */
    public Venta(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
    }

    /**
     * Constructor de la clase.
     * 
     * @param cliente Cliente de la venta.
     */
    public Venta(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Obtiene el identificador de la venta.
     * 
     * @return El identificador de la venta.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador de la venta.
     * 
     * @param id El identificador de la venta.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el cliente.
     * 
     * @return El cliente.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el cliente.
     * 
     * @param cliente El cliente.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Compatibility method for JDBC code using idCliente.
     * 
     * @return ID of the client.
     */
    public int getIdCliente() {
        return cliente != null ? cliente.getId() : 0;
    }

    /**
     * Compatibility method not recommended for JPA usage.
     * 
     * @param idCliente
     */
    public void setIdCliente(int idCliente) {
        // Cannot set ID on entity directly if expecting to set relationship.
        // This is just a placeholder to avoid compilation errors if any.
    }
}
