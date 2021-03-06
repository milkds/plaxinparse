package secondstep;


import keystone.KeyAdditionalPart;
import keystone.KeyCar;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CarDao {

    public static void main(String[] args) {
        processLifts();

    }

    public static List<Car>getCars(){
        Session session = null;
        Transaction transaction = null;
        List<Car> cars = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Car.class);
            //  criteria.setProjection(Projections.distinct(Projections.property("carFullName")));
            cars=criteria.list();
          /*  transaction = session.getTransaction();
            transaction.begin();*/

            /*Car car = buildTestCar();
            int id = (Integer)session.save(car);
            List<ShockAbsorber> absorbers = car.getAbsorbers();
            for (ShockAbsorber absorber: absorbers){
                absorber.setCarID(id);
                session.persist(absorber);
            }*/

            //transaction.commit();
        } catch (Exception e) {
            /*if (transaction != null) {
                transaction.rollback();
            }*/
            e.printStackTrace();
//        } finally {
//            if (session != null) {
//                session.close();
//            }
        }

        HibernateUtil.shutdown();
        System.out.println(cars.size() + " is qty of cars in database");

        return cars;
    }

    public static List<Car>getCars(Session session){
        List<Car> cars = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Car.class);
            cars=criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(cars.size() + " is qty of cars in database");

        return cars;
    }
    public static List<Car>getCarsOrdered(Session session){
        List<Car> cars = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Car.class);
            criteria.addOrder(Order.asc("make"));
            criteria.addOrder(Order.asc("model"));
            criteria.addOrder(Order.asc("submodel"));
            criteria.addOrder(Order.asc("drive").nulls(NullPrecedence.LAST));
            criteria.addOrder(Order.asc("suspension").nulls(NullPrecedence.LAST));
            criteria.addOrder(Order.asc("modelYear"));
            cars=criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(cars.size() + " is qty of cars in database");

        return cars;
    }

    public static List<CarNoInfo>getCarsNoInfo(Session session){
        List<CarNoInfo> cars = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(CarNoInfo.class);
            cars=criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(cars.size() + " is qty of cars in database");

        return cars;
    }
    public static List<ShockAbsorber>getAbsorbers(){
        Session session;
        List<ShockAbsorber> shockAbsorbers = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(ShockAbsorber.class);
            //criteria.setProjection(Projections.distinct(Projections.property("partNo")));
            shockAbsorbers=criteria.list();
            //session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //HibernateUtil.shutdown();
        System.out.println(shockAbsorbers.size() + " is qty of shocks in database");
        HibernateUtil.shutdown();

        return shockAbsorbers;
    }
    public static List<ShockAbsorber>getAbsorbers(Session session){
        List<ShockAbsorber> shockAbsorbers = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(ShockAbsorber.class);
            shockAbsorbers=criteria.list();
        } catch (Exception e) {

        }
        return shockAbsorbers;
    }
    public static List<ShockAbsorber> getAbsorbersByCarID(Session session, Integer carID){
        List<ShockAbsorber> shockAbsorbers = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(ShockAbsorber.class);
            criteria.add(Restrictions.eq("carID",carID));
            shockAbsorbers=criteria.list();
        } catch (Exception e) {

        }
        return shockAbsorbers;
    }


    public static Car buildTestCar(){
        List<ShockAbsorber> absorbers = new ArrayList<>();
        // codeDump(absorbers);
        ShockAbsorber absorber1 = new ShockAbsorber();
        absorber1.setSeries("avcd");
        absorbers.add(absorber1);
        ShockAbsorber absorber2 = new ShockAbsorber();
        absorber2.setSeries("xyz");
        absorbers.add(absorber2);
        Car car = new Car();
        car.setCarFullName("Chevrolet Silverado");
        car.setAbsorbers(absorbers);

        return car;
    }

    public static void saveCar(Session session, Car car){
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(car);
            /*int id = (Integer)session.save(car);
            List<ShockAbsorber> absorbers = car.getAbsorbers();
            for (ShockAbsorber absorber: absorbers){
                absorber.setCarID(id);
                session.persist(absorber);
            }*/
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("couldn't save car to db "+car.getCarFullName());
            e.printStackTrace();
            System.exit(0);
        }

    }
    public static int saveAdditional(Session session, KeyAdditionalPart part){
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();

           session.save(part);
            List<KeyCar> cars = part.getCars();
            for (KeyCar car: cars){
                session.persist(car);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return 1;
        }

        return 0;
    }
    public static Session getSession(){
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
        } catch (Exception e) {
            System.out.println("no session");
            e.printStackTrace();
            System.exit(0);
        }

        return session;
    }

    public static void updateCar(Session session, String carFullName, int carID){
        Transaction transaction = session.beginTransaction();
        Car car = session.load(Car.class, carID);
        car.setCarFullName(carFullName);
        session.update(car);
        transaction.commit();
    }
    public static void updateCar(Session session, Car car){
        Transaction transaction = session.beginTransaction();
        session.update(car);
        transaction.commit();
    }
    public static void updateCarNoInfo(Session session, CarNoInfo carNoInfo){
        Transaction transaction = session.beginTransaction();
        session.update(carNoInfo);
        transaction.commit();
    }
    public static void updateShock(Session session, ShockAbsorber absorber){
        Transaction transaction = session.beginTransaction();
        session.update(absorber);
        transaction.commit();
    }

    public static void processLifts() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ShockAbsorber> absorbers = getAbsorbers(session);
        for (ShockAbsorber absorber : absorbers) {
        if (absorber.getLiftStart()==null&&absorber.getLiftFinish()==null){
            String notes = absorber.getNotes();

            if (notes!=null&&notes.contains("Lift")){
                System.out.println(notes);
                System.out.println(absorber.getId());
            }
        }

        }
        System.out.println("thats all");
        session.close();
        System.exit(0);
    }

    public static List<Integer> getCarIDsByShock(Session session, ShockAbsorber filterShock) {
        List<Integer> carIDs = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(ShockAbsorber.class);
            criteria.setProjection(Projections.property("carID"));
            criteria.add(Restrictions.eq("partNo",filterShock.getPartNo()));
            carIDs=criteria.list();
        } catch (Exception e) {

        }

        return carIDs;
    }

    public static List<Car> getCarsByMakeModelIDs(Session session, Car filterCar, List<Integer> carIDsByShock) {
        List<Car> cars = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Car.class);
            criteria.add(Restrictions.eq("make",filterCar.getMake()));
            criteria.add(Restrictions.eq("model",filterCar.getModel()));
            criteria.add(Restrictions.in("id",carIDsByShock.toArray()));
            cars=criteria.list();
        } catch (Exception e) {

        }

        return cars;
    }
}


