package com.inventario.repository;

import java.util.Collections;
import java.util.List;

import com.inventario.model.Cliente;
import com.inventario.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

/**
 * Clase que representa el repositorio de clientes
 */
public class ClienteRepository {

    /**
     * Verifica si un cliente existe
     * 
     * @param id el ID del cliente
     * @return true si el cliente existe, false en caso contrario
     */
    public boolean existeCliente(int id) {
        // En JPA, simplemente buscamos por ID.
        boolean existe;

        try (EntityManager em = JPAUtil.getEntityManager();) {
            Cliente c = em.find(Cliente.class, id);
            existe = (c != null);
        }
        return existe;
    }

    /**
     * Agrega dinero a un cliente
     * 
     * @param idCliente el ID del cliente
     * @param cantidad  la cantidad de dinero a agregar
     * @return true si se agregó el dinero, false en caso contrario
     */
    public boolean agregarDinero(int idCliente, double cantidad) {
        boolean exito = false;
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente c = em.find(Cliente.class, idCliente);
            if (c != null) {
                // JPA trackea cambios automáticamente en entidades gestionadas
                c.setDinero(c.getDinero() + cantidad);
                tx.commit();
                exito = true;
            } else {
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
        return exito;
    }

    /**
     * Resta dinero a un cliente
     * 
     * @param idCliente el ID del cliente
     * @param cantidad  la cantidad de dinero a restar
     * @return true si se restó el dinero, false en caso contrario
     */
    public boolean restarDinero(int idCliente, double cantidad) {
        boolean exito = false;
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente c = em.find(Cliente.class, idCliente);
            if (c != null) {
                c.setDinero(c.getDinero() - cantidad);
                tx.commit();
                exito = true;
            } else {
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
        return exito;
    }

    /**
     * Obtiene el conteo de clientes
     * 
     * @return el conteo de clientes
     */
    public int contarClientes() {
        int count = 0;

        try (EntityManager em = JPAUtil.getEntityManager();) {
            count = em.createQuery("SELECT COUNT(c) FROM Cliente c", Long.class).getSingleResult().intValue();
        }
        return count;
    }

    /**
     * Busca clientes por campo
     * 
     * @param campo el campo a buscar
     * @param valor el valor a buscar
     * @return la lista de clientes encontrados
     */
    public List<Cliente> buscarPorCampo(String campo, Object valor) {
        List<Cliente> clientes = Collections.emptyList();

        try (EntityManager em = JPAUtil.getEntityManager();) {
            String campoEntidad = campo;
            if ("id_cliente".equals(campo))
                campoEntidad = "id";

            String jpql = "SELECT c FROM Cliente c WHERE c." + campoEntidad + " = :valor";
            TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
            query.setParameter("valor", valor);
            clientes = query.getResultList();
        }
        return clientes;
    }

    /**
     * Elimina un cliente
     * 
     * @param idCliente el ID del cliente
     * @return true si se eliminó el cliente, false en caso contrario
     */
    public boolean eliminarCliente(int idCliente) {
        boolean eliminado = false;
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente c = em.find(Cliente.class, idCliente);
            if (c != null) {
                em.remove(c);
                tx.commit();
                eliminado = true;
            } else {
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
        return eliminado;
    }

    /**
     * Actualiza un cliente por campo
     * 
     * @param idCliente el ID del cliente
     * @param campo     el campo a actualizar
     * @param valor     el valor a actualizar
     * @return true si se actualizó el cliente, false en caso contrario
     */
    public boolean actualizarClienteCampo(int idCliente, String campo, Object valor) {
        boolean actualizado = false;
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente c = em.find(Cliente.class, idCliente);
            if (c != null) {
                // Reflexión básica o switch para asignar valor.
                // Como es una migración directa, usaremos switch para los campos conocidos.
                // id_cliente, nombre, email, telefono, dinero

                switch (campo) {
                    case "nombre" -> {
                        c.setNombre((String) valor);
                    }
                    case "email" -> {
                        c.setEmail((String) valor);
                    }
                    case "telefono" -> {
                        c.setTelefono((String) valor);
                    }
                    case "dinero" -> {
                        c.setDinero(Double.parseDouble(valor.toString()));
                    }
                    default -> {
                        // Campo no soportado o error
                        throw new IllegalArgumentException("Campo no soportado: " + campo);
                    }
                }

                tx.commit();
                actualizado = true;
            } else {
                tx.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
        return actualizado;
    }

    /**
     * Obtiene todos los clientes
     * 
     * @return la lista de clientes
     */
    public List<Cliente> obtenerClientes() {
        List<Cliente> clientes;

        try (EntityManager em = JPAUtil.getEntityManager();) {
            clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        }
        return clientes;
    }

    /**
     * Inserta un cliente
     * 
     * @param cliente el cliente a insertar
     * @return true si se insertó el cliente, false en caso contrario
     */
    public boolean insertarCliente(Cliente cliente) {
        boolean insertado = false;
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Para insertar un nuevo objeto, usamos persist.
            // Si el cliente ya tiene ID, merge podría ser mejor, pero asumimos insert
            // nuevo.
            // Como Cliente constructor puede setear ID 0.
            em.persist(cliente);
            tx.commit();
            insertado = true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return insertado;
    }
}
