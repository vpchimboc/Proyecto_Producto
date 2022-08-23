/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoproducto;

import java.sql.Connection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author 59399
 */
public class ManagerFactory {

    private EntityManagerFactory emf = null;

    public EntityManagerFactory getEntityManagerFactory() {
        return emf = Persistence.createEntityManagerFactory("ProyectoProductoJDBCPU");
    }

    public static final Connection getConnection(final EntityManager entityManager) {
        entityManager.getTransaction().begin();
        Connection connection = entityManager.unwrap(java.sql.Connection.class);
        return connection;

    }
}
