package com.easyinsight.stores;

import com.easyinsight.users.*;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.logging.LogClass;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: May 27, 2008
 * Time: 5:01:43 PM
 */
@SuppressWarnings({"UnusedAssignment"})
public class StoreService {

    //private TransactionStorage transactionStorage = new TransactionStorage();

    public void addStore() {

    }

    public void deleteStore() {

    }

    public void updateStore() {
        
    }

    /*public String createURL() {
        try {
            InstallPaymentInstruction obj = new InstallPaymentInstruction();
            String callerID;
            {
            String apiName = "InstallPaymentInstruction";
                String CallerReference = "AmazonFPSSDK - Caller000";
                String PaymentInstruction = "MyRole == 'Caller';";
                String TokenFriendlyName = "AmazonFPSSDK - Caller";
                String TokenType = "Unrestricted";
                String PaymentReason = "paymentReason";
                String httpQuery = obj.getHttpQuery(apiName, CallerReference, PaymentInstruction, TokenFriendlyName, TokenType, PaymentReason);
                Driver installPI = new Driver(httpQuery);
                callerID = installPI.call();
            }

            String recipientID;
            {
                String apiName = "InstallPaymentInstruction";
                String CallerReference = "AmazonFPSSDK - Recipient000";
                String PaymentInstruction = "MyRole == 'Recipient';";
                String TokenFriendlyName = "AmazonFPSSDK - Recipient";
                String TokenType = "Unrestricted";
                String PaymentReason = "paymentReason";
                String httpQuery = obj.getHttpQuery(apiName, CallerReference, PaymentInstruction, TokenFriendlyName, TokenType, PaymentReason);
                Driver installPI = new Driver(httpQuery);
                recipientID = installPI.call();
            }

            return obj.createSenderQuery("10", callerID, recipientID, "SingleUse", "EasyInsight" + System.currentTimeMillis());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }    */

    /*public String amazonBuy(String senderID, String callerReference) {
        try {
            InstallPaymentInstruction obj = new InstallPaymentInstruction();
            String callerID;
            {
            String apiName = "InstallPaymentInstruction";
                String CallerReference = "AmazonFPSSDK - Caller000";
                String PaymentInstruction = "MyRole == 'Caller';";
                String TokenFriendlyName = "AmazonFPSSDK - Caller";
                String TokenType = "Unrestricted";
                String PaymentReason = "paymentReason";
                String httpQuery = obj.getHttpQuery(apiName, CallerReference, PaymentInstruction, TokenFriendlyName, TokenType, PaymentReason);
                Driver installPI = new Driver(httpQuery);
                callerID = installPI.call();
            }

            String recipientID;
            {
                String apiName = "InstallPaymentInstruction";
                String CallerReference = "AmazonFPSSDK - Recipient000";
                String PaymentInstruction = "MyRole == 'Recipient';";
                String TokenFriendlyName = "AmazonFPSSDK - Recipient";
                String TokenType = "Unrestricted";
                String PaymentReason = "paymentReason";
                String httpQuery = obj.getHttpQuery(apiName, CallerReference, PaymentInstruction, TokenFriendlyName, TokenType, PaymentReason);
                Driver installPI = new Driver(httpQuery);
                recipientID = installPI.call();
            }
            LogClass.debug("got " + senderID);
            //String response = new Driver(obj.payRequest(recipientID, callerID, senderID, "10", callerReference), true).call();
            Transaction transaction = Pay.pay(recipientID, callerID, senderID, "10", callerReference);  */
            /*BuyResponse buyResponse;
            if (transaction.isComplete()) {
                if (transaction.isFailed()) {

                } else {

                }
                buyResponse = new BuyResponse(Transaction.COMPLETE);
            } else {
                
            }*/
       /*     transactionStorage.addTransaction(transaction);
            //LogClass.debug(response);
            //return response;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return "";
    }       */

    public Collection<Merchant> getMerchants() {
        Session session = Database.instance().createSession();
        //User creator = new UserService().retrieveUser();
        List<Merchant> merchantResults = new ArrayList<Merchant>();
        /*try {
            session.beginTransaction();
            List merchantObjs = session.createQuery("from Merchant as merchant left join " +
                    "merchant.users as userBinding where userBinding.accountID = ?").
                    setLong(0, creator.getAccountID().getAccountID()).list();
            for (Object obj : merchantObjs) {
                Object[] elements = (Object[]) obj;
                Merchant merchant = (Merchant) elements[0];
                merchantResults.add(merchant);
            }
            session.getTransaction().commit();
            for (Merchant merchant : merchantResults) {
                for (UserMerchantBinding user : merchant.getUsers()) {
                    user.setAccountID(null);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }*/
        return merchantResults;
    }

    public long createMerchant(Merchant merchant) {
        /*Collection<UserMerchantBinding> bindings = new ArrayList<UserMerchantBinding>();
        User creator = new UserService().retrieveUser();
        bindings.add(new UserMerchantBinding(creator.getAccountID(), merchant, UserMerchantBindingTypes.OWNER));
        merchant.setUsers(bindings);
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            session.save(merchant);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return merchant.getMerchantID();*/
        return 0;
    }

    public void buy(long feedID, int numberLicenses) {
        /*try {
            UserService userService = new UserService();
            User user = userService.retrieveUser();
            Account account = userService.getAccount(user.getAccountID().getAccountID());

            for (int i = 0; i < numberLicenses; i++) {
                SubscriptionLicense subscriptionLicense = new SubscriptionLicense();
                subscriptionLicense.setAccount(account);
                subscriptionLicense.setFeedID(feedID);

                account.addLicense(subscriptionLicense);
                if (i == 0) {
                    if (account.getUsers().size() == 1) {
                        user.getLicenses().add(subscriptionLicense);
                        subscriptionLicense.setUser(user);
                        new UserUploadService().subscribe(feedID);
                    }
                }
            }
            //userService.updateAccount(account);
            FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(feedID);
            //CommercialUploadPolicy commercialUploadPolicy = (CommercialUploadPolicy) feedDefinition.getUploadPolicy();
            //Merchant merchant = getMerchant(commercialUploadPolicy.getMerchantID());
            //new PurchaseManager().buy(feedID, user, merchant, new Date());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }*/
        throw new UnsupportedOperationException();
    }

    private Merchant getMerchant(long merchantID) {
        Merchant merchant;
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Merchant where merchantID = ?").setLong(0, merchantID).list();
            merchant = (Merchant) results.get(0);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return merchant;
    }
}
