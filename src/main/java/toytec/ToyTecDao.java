package toytec;

import org.hibernate.Session;
import org.hibernate.Transaction;
import secondstep.ShockAbsorber;

import java.util.List;

public class ToyTecDao {
    public static void saveToyItem(ToyItem toyItem, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();

            int id = (Integer)session.save(toyItem);
            List<ToyOption> options = toyItem.getOptions();
            for (ToyOption option: options){
                option.setItemID(id);
                session.persist(option);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("couldn't save car to db "+ toyItem.getItemName());
            e.printStackTrace();
            System.exit(0);
        }
    }
}
