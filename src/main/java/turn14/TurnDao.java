package turn14;

import org.hibernate.Session;
import org.hibernate.Transaction;
import secondstep.HibernateUtil;
import secondstep.ShockAbsorber;

import java.util.List;

public class TurnDao {

    public static void saveParts(List<TurnPart> parts){
        Session session = HibernateUtil.getSessionFactory().openSession();
        for (TurnPart part: parts){
            savePart(part,session);
        }
        HibernateUtil.shutdown();
    }

    private static void savePart(TurnPart part, Session session){
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();

            int id = (Integer)session.save(part);
            List<TurnCar> cars = part.getCars();
            for (TurnCar car: cars){
                car.setPartID(id);
                session.persist(car);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("couldn't save part to db "+part.getPartNo());
            e.printStackTrace();
            System.exit(0);
        }
    }
}
