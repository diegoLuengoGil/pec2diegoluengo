package com.inventario.repository;

import java.util.ArrayList;
import java.util.List;

import com.inventario.model.Cliente;
import com.inventario.model.DetalleVenta;
import com.inventario.model.Producto;
import com.inventario.model.Venta;
import com.inventario.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import com.inventario.excepciones.SaldoInsuficienteException;

/**
 * Clase que representa el repositorio de ventas
 */
public class VentaRepository {

    /**
     * Obtiene un resumen de las ventas
     * 
     * @return la lista de ventas
     */
    public List<String> obtenerResumenVentas() {
        List<String> resumen = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // JPQL para traer ventas y clientes (join fetch opcional para optimizar)
            List<Venta> ventas = em
                    .createQuery("SELECT v FROM Venta v JOIN FETCH v.cliente ORDER BY v.id DESC", Venta.class)
                    .getResultList();

            for (Venta v : ventas) {
                resumen.add(String.format("%-5d | %-10d | %s",
                        v.getId(),
                        v.getCliente().getId(),
                        v.getCliente().getNombre()));
            }
        } finally {
            em.close();
        }
        return resumen;
    }

    /**
     * Obtiene los detalles de una venta
     * 
     * @param idVenta el ID de la venta
     * @return la lista de detalles de la venta
     */
    public List<String> obtenerDetallesVenta(int idVenta) {
        List<String> detalles = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Venta venta = em.find(Venta.class, idVenta);
            if (venta != null) {
                for (DetalleVenta d : venta.getDetalles()) {
                    detalles.add(String.format("%-25s | %-10d | %-10.2f | %.2f",
                            d.getProducto().getNombre(),
                            d.getCantidad(),
                            d.getPrecioUnitario(),
                            (d.getCantidad() * d.getPrecioUnitario())));
                }
            }
        } finally {
            em.close();
        }
        return detalles;
    }

    /**
     * Calcula el total de una venta
     * 
     * @param idVenta el ID de la venta
     * @return el total de la venta
     */
    public double calcularTotalVenta(int idVenta) {
        // En JPA podemos calcularlo en memoria o con una consulta.
        // Consulta para sumar:
        double total = 0.0;
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Double res = em.createQuery(
                    "SELECT SUM(d.cantidad * d.precioUnitario) FROM DetalleVenta d WHERE d.venta.id = :idVenta",
                    Double.class)
                    .setParameter("idVenta", idVenta)
                    .getSingleResult();
            if (res != null) {
                total = res;
            }
        } finally {
            em.close();
        }
        return total;
    }

    /**
     * Realiza una venta completa
     * 
     * @param idCliente   el ID del cliente
     * @param detallesDTO la lista de detalles de la venta
     */
    public void realizarVentaCompleta(int idCliente, List<DetalleVenta> detallesDTO)
            throws SaldoInsuficienteException {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 1. Obtener Cliente
            Cliente cliente = em.find(Cliente.class, idCliente);
            if (cliente == null) {
                System.out.println("Cliente no encontrado.");
            } else {
                // 2. Crear Venta
                Venta venta = new Venta(cliente);
                // No persistimos venta aún, la guardaremos en cascada o al final.
                // Pero necesitamos la instancia para asociarla.
                em.persist(venta); // Persistimos para tener ID si fuera necesario o gestionado.

                double totalVenta = 0.0;

                // 3. Procesar Detalles (Stock y Total)
                for (DetalleVenta dTemp : detallesDTO) {
                    // El objeto DetalleVenta viene "suelto" del controlador/vista (solo con IDs).
                    // dTemp.getProducto() es dummy con ID.
                    Producto producto = em.find(Producto.class, dTemp.getProducto().getId());

                    if (producto == null) {
                        throw new Exception("Producto no encontrado: ID " + dTemp.getProducto().getId());
                    }

                    if (producto.getStock() < dTemp.getCantidad()) {
                        throw new Exception("Stock insuficiente para: " + producto.getNombre());
                    }

                    // Actualizar Stock
                    producto.setStock(producto.getStock() - dTemp.getCantidad());
                    // em.merge(producto); // Innecesario si está gestionado (find lo deja
                    // gestionado)

                    // Crear el Detalle real asociado
                    DetalleVenta detalleReal = new DetalleVenta(venta, producto, dTemp.getCantidad(),
                            producto.getPrecio());
                    venta.addDetalle(detalleReal); // Helper method que sincroniza

                    // Si no usamos cascade ALL, tendríamos que: em.persist(detalleReal);

                    totalVenta += (dTemp.getCantidad() * producto.getPrecio());
                }

                // 4. Cobrar al Cliente
                if (cliente.getDinero() < totalVenta) {
                    throw new SaldoInsuficienteException(
                            String.format("Saldo insuficiente. Total: %.2f, Saldo: %.2f", totalVenta,
                                    cliente.getDinero()));
                }
                cliente.setDinero(cliente.getDinero() - totalVenta);

                // 5. Commit
                tx.commit();
            }

        } catch (SaldoInsuficienteException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e; // Re-lanzamos la específica
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
    }
}
