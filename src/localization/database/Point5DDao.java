/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localization.database;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.omg.CORBA.SystemException;

/**
 *
 * @author ap
 */
public class Point5DDao {

    private static final EntityManager ENTITY_MANAGER = EntityManagerUtil.getEntityManager();

    public static void save(Point5D entry) {
        System.out.println(Time.valueOf(LocalTime.now()) + "     [" + Thread.currentThread().getName() + "]" + " save");

        try {
            ENTITY_MANAGER.getTransaction().begin();
            ENTITY_MANAGER.merge(entry);
            ENTITY_MANAGER.getTransaction().commit();
        } catch (Exception e) {
            ENTITY_MANAGER.getTransaction().rollback();
        }
    }

    public void delete(Point5D entry) {
        try {
            ENTITY_MANAGER.getTransaction().begin();
            ENTITY_MANAGER.remove(entry);
            ENTITY_MANAGER.getTransaction().commit();
        } catch (Exception e) {
            ENTITY_MANAGER.getTransaction().rollback();
        }
    }

    public static void clear() {
        try {
            ENTITY_MANAGER.getTransaction().begin();
            Query q3 = ENTITY_MANAGER.createQuery("DELETE FROM Point5D");
            q3.executeUpdate();
            ENTITY_MANAGER.getTransaction().commit();
        } catch (Exception e) {
            ENTITY_MANAGER.getTransaction().rollback();
        }
    }

    public void update(Point5D entry) {
        try {
            ENTITY_MANAGER.getTransaction().begin();
            ENTITY_MANAGER.merge(entry);
            ENTITY_MANAGER.getTransaction().commit();
        } catch (Exception e) {
            ENTITY_MANAGER.getTransaction().rollback();
        }
    }

    public static List<Point5D> selectPointsByView(int img) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Point5D> c = cq.from(Point5D.class);
        cq.select(c);
        cq.where(cb.equal(c.get("view"), img));
//        ParameterExpression<String> p = cb.parameter(String.class, "tkey");
        Query q = entityManager.createQuery(cq);
//    TypedQuery<Entry> q = entityManager.createQuery(cq);
//      entityManager.getTransaction().begin();
//      @SuppressWarnings("unchecked")
        return q.getResultList();
    }

    public List selectAll() {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        CriteriaQuery<Point5D> cq = entityManager.getCriteriaBuilder()
                .createQuery(Point5D.class);
        cq.select(cq.from(Point5D.class));
        TypedQuery<Point5D> q = entityManager.createQuery(cq);
        return q.getResultList();
    }

//    public static List<Bookmark> selectBookmark(String bookmarkName) {
//        EntityManager entityManager = Dao.getEntityManager();
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery cq = cb.createQuery();
//        Root<Bookmark> c = cq.from(Bookmark.class);
//        cq.select(c);
//        cq.where(cb.equal(c.get("tkey"), bookmarkName));
//        Query q = entityManager.createQuery(cq);
//        return q.getResultList();
//    }
}
