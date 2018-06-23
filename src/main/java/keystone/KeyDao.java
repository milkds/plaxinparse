package keystone;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import secondstep.HibernateUtil;
import secondstep.ShockAbsorber;

import java.util.ArrayList;
import java.util.List;

public class KeyDao {

    public static Session getSession(){
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return session;
    }

    public static int saveAdditionalPart(Session session, KeyAdditionalPart part) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(part);
            List<KeyCar> cars = part.getCars();
            for (KeyCar car: cars){
                session.persist(car);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
           return 1;
        }

        return 0;
    }

    public static int saveShock(Session session, KeyShock shock) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            //transaction.begin();
            session.save(shock);
            List<KeyCar> cars = shock.getCars();
            for (KeyCar car: cars){
                session.persist(car);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return 1;
        }

        return 0;
    }
    public static int updateShock(Session session, KeyAdditionalPart shock) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            //transaction.begin();
            session.update(shock);
            transaction.commit();
            System.out.println("updated "+shock.getPartNo());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return 1;
        }

        return 0;
    }
    public static int deleteShock(Session session, KeyShock shock) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            //transaction.begin();
            session.delete(shock);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return 1;
        }

        return 0;
    }

    public static List<KeyShock> getShocks(){
        List<KeyShock> shocks = new ArrayList<>();
        Session session = getSession();
        Criteria criteria = session.createCriteria(KeyShock.class);
        shocks=criteria.list();

        HibernateUtil.shutdown();
        return shocks;
    }
    public static List<KeyCar> getCars(){
        List<KeyCar> cars = new ArrayList<>();
        Session session = getSession();
        Criteria criteria = session.createCriteria(KeyCar.class);
        cars=criteria.list();

        HibernateUtil.shutdown();
        return cars;
    }
    public static List<KeyShock> getShocks(Session session){
        List<KeyShock> shocks = new ArrayList<>();
        Criteria criteria = session.createCriteria(KeyShock.class);
        shocks=criteria.list();
        return shocks;
    }

    public static List<KeyAdditionalPart> getAdditionals(Session session){
        List<KeyAdditionalPart> additionalParts = new ArrayList<>();
        Criteria criteria = session.createCriteria(KeyAdditionalPart.class);
        additionalParts=criteria.list();
        return additionalParts;
    }

    public static int saveCars(Session session, List<KeyCar> cars) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
           for (KeyCar car: cars){
               session.persist(car);
           }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return 1;
        }

        return 0;
    }

    public static List<KeyCar> getCars(String partNo, Session session){
        List<KeyCar> cars = new ArrayList<>();
        Criteria criteria = session.createCriteria(KeyCar.class);
        criteria.add(Restrictions.like("shockPartNo", partNo));
        cars=criteria.list();

        return cars;
    }
    public static List<KeyCar> getCars( Session session){
        List<KeyCar> cars = new ArrayList<>();
        Criteria criteria = session.createCriteria(KeyCar.class);
        cars=criteria.list();

        return cars;
    }

    public static int deleteCar(Session session, KeyCar car) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            //transaction.begin();
            session.delete(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return 1;
        }

        return 0;
    }
}
