package com.easyinsight.stores;

import com.easyinsight.users.User;
import com.easyinsight.database.Database;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.logging.LogClass;

import java.util.Date;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: May 27, 2008
 * Time: 10:58:56 PM
 */
public class PurchaseManager {

    private UserUploadService userUploadService = new UserUploadService();

    public long buy(long feedID, User buyer, Merchant merchant, Date purchaseDate) {
        Purchase purchase = new Purchase(feedID, buyer, merchant, purchaseDate);
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            // create the purchase record...            
            session.save(purchase);
            // execute an actual payment...
            Payment payment = createPayment(purchase);
            session.save(payment);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }        
        return 0;
    }

    public void cancelSubscription(long purchaseID) {

    }

    public Payment createPayment(Purchase purchase) {
        return new Payment(purchase);
    }
}
