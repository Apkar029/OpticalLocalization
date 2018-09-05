/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ap
 */
public class EntityManagerUtil {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY;

    static {
        try {
            ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("EventLocalizationSwingPU");
            System.out.println("EntityManagerFactory created");

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            System.err.println("Please check database connection");
            System.err.println("Exiting");
//            System.exit(1);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void closeEntityFactory() {
        ENTITY_MANAGER_FACTORY.close();
    }
}
