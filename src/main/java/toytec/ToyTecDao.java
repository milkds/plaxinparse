package toytec;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import secondstep.HibernateUtil;
import secondstep.ShockAbsorber;

import java.util.ArrayList;
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

    public static List<ToyItem> getAllItems(Session session){
        List<ToyItem> toyItems = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(ToyItem.class);
            toyItems=criteria.list();
        } catch (Exception e) {

        }
        return toyItems;
    }

    public static void updateItem(ToyItem item, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.update(item);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("couldn't save car to db "+ item.getItemName());
            e.printStackTrace();
            System.exit(0);
        }
    }
}
