package com.inventario.repository;

import java.util.Collections;
import java.util.List;

import com.inventario.model.Producto;
import com.inventario.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

/**
 * Clase que representa el repositorio de productos
 */
public class ProductoRepository {

    /**
     * Obtiene el conteo de productos
     * 
     * @return el conteo de productos
     */
    public int contarProductos() {
        int count = 0;

        try (EntityManager em = JPAUtil.getEntityManager();) {
            count = em.createQuery("SELECT COUNT(p) FROM Producto p", Long.class).getSingleResult().intValue();
        }
        return count;
    }

    /**
     * Busca productos por campo
     * 
     * @param campo el campo a buscar
     * @param valor el valor a buscar
     * @return la lista de productos encontrados
     */
    public List<Producto> buscarPorCampo(String campo, Object valor) {
        List<Producto> productos = Collections.emptyList();

        try (EntityManager em = JPAUtil.getEntityManager();) {
            String campoEntidad = campo;
            if ("id_producto".equals(campo))
                campoEntidad = "id";

            // Verificamos campos válidos para evitar errores de JPQL
            if (!isValidField(campoEntidad)) {
                // Si el campo no es válido, devolvemos lista vacía
                // O podríamos lanzar excepción.
                return productos;
            }

            String jpql = "SELECT p FROM Producto p WHERE p." + campoEntidad + " = :valor";
            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setParameter("valor", valor);
            productos = query.getResultList();
        }
        return productos;
    }

    /**
     * Verifica si un campo es válido
     * 
     * @param field el campo a verificar
     * @return true si el campo es válido, false en caso contrario
     */
    private boolean isValidField(String field) {
        return List.of("id", "nombre", "descripcion", "precio", "stock").contains(field);
    }

    /**
     * Actualiza un producto por campo
     * 
     * @param idProducto el ID del producto
     * @param campo      el campo a actualizar
     * @param valor      el valor a actualizar
     * @return true si se actualizó el producto, false en caso contrario
     */
    public boolean actualizarProductoCampo(int idProducto, String campo, Object valor) {
        boolean actualizado = false;
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Producto p = em.find(Producto.class, idProducto);
            if (p != null) {
                boolean campoValido = true;
                switch (campo) {
                    case "nombre" -> p.setNombre((String) valor);
                    case "descripcion" -> p.setDescripcion((String) valor);
                    case "precio" -> p.setPrecio(Double.parseDouble(valor.toString()));
                    case "stock" -> p.setStock(Integer.parseInt(valor.toString()));
                    default -> campoValido = false;
                }

                if (campoValido) {
                    tx.commit();
                    actualizado = true;
                } else {
                    tx.rollback();
                }
            } else {
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return actualizado;
    }

    /**
     * Elimina un producto
     * 
     * @param idProducto el ID del producto
     * @return true si se eliminó el producto, false en caso contrario
     */
    public boolean eliminarProducto(int idProducto) {
        boolean eliminado = false;
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Producto p = em.find(Producto.class, idProducto);
            if (p != null) {
                em.remove(p);
                tx.commit();
                eliminado = true;
            } else {
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
        } finally {
            em.close();
        }
        return eliminado;
    }

    /**
     * Obtiene todos los productos
     * 
     * @return la lista de productos
     */
    public List<Producto> obtenerProductos() {
        List<Producto> productos;

        try (EntityManager em = JPAUtil.getEntityManager();) {
            productos = em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
        }
        return productos;
    }

    /**
     * Inserta un producto
     * 
     * @param producto el producto a insertar
     * @return true si se insertó el producto, false en caso contrario
     */
    public boolean insertarProducto(Producto producto) {
        boolean insertado = false;
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(producto);
            tx.commit();
            insertado = true;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return insertado;
    }
}
