package secondstep;


import keystone.KeyAdditionalPart;
import keystone.KeyCar;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CarDao {
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
    public static void main(String[] args) {
       /* Session session = null;
        Transaction transaction = null;
        List<ShockAbsorber> absorbers = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(ShockAbsorber.class);
           // criteria.setProjection(Projections.distinct(Projections.property("partNo")));
            absorbers=criteria.list();
          *//*  transaction = session.getTransaction();
            transaction.begin();*//*

            *//*Car car = buildTestCar();
            int id = (Integer)session.save(car);
            List<ShockAbsorber> absorbers = car.getAbsorbers();
            for (ShockAbsorber absorber: absorbers){
                absorber.setCarID(id);
                session.persist(absorber);
            }*//*

            //transaction.commit();
        } catch (Exception e) {
            *//*if (transaction != null) {
                transaction.rollback();
            }*//*
            e.printStackTrace();
//        } finally {
//            if (session != null) {
//                session.close();
//            }
        }

        HibernateUtil.shutdown();
        for (ShockAbsorber absorber: absorbers){
            System.out.println(absorber.getDetailsUrl());
        }*/
        List<ShockAbsorber> abs = new ArrayList<>();
        abs=getAbsorbers();

        for (ShockAbsorber absorber : abs){
            if  (absorber.getPartNo().equals("24-253147")){
                System.out.println(absorber.getOtherNotes());
            }
        }
        /*Set<String> absPartNo = new TreeSet<>();
        for (ShockAbsorber absorber: abs){
            absPartNo.add(absorber.getPartNo());
        }

        try(FileWriter fw = new FileWriter("F:\\My Java Projects\\plaxinparse\\src\\main\\java\\keystone\\log\\toparse", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
           for (String partNo: absPartNo){
               out.println(partNo);
           }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

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

            int id = (Integer)session.save(car);
            List<ShockAbsorber> absorbers = car.getAbsorbers();
            for (ShockAbsorber absorber: absorbers){
                absorber.setCarID(id);
                session.persist(absorber);
            }
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
}
