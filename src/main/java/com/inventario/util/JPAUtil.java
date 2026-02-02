package com.inventario.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase que representa la utilidad de JPA
 */
public class JPAUtil {
    /**
     * Factoría de entity manager
     */
    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    /**
     * Crea una factoría de entity manager
     * 
     * @return la factoría de entity manager
     */
    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            return Persistence.createEntityManagerFactory("inventarioPU");
        } catch (Exception ex) {
            System.err.println("Initial EntityManagerFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Obtiene un entity manager
     * 
     * @return el entity manager
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Cierra la factoría de entity manager
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
