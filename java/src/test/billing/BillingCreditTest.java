package test.billing;

import com.easyinsight.users.Account;
import com.easyinsight.users.AccountActivity;
import com.easyinsight.users.AccountCreditCardBillingInfo;
import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 8/20/12
 * Time: 8:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class BillingCreditTest extends TestCase {
    private Date chargeDate;
    private Date curDate;

//    @Override
//    public void setUp() throws Exception {
//        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.DAY_OF_MONTH, 10);
//        chargeDate = c.getTime();
//        Calendar c2 = Calendar.getInstance();
//        c2.set(Calendar.DAY_OF_MONTH, 11);
//        curDate = c2.getTime();
//    }
//
//    public void testSingleMonthNormalBilling() {
//        Account a = new Account();
//
//        AccountCreditCardBillingInfo billingInfo = new AccountCreditCardBillingInfo();
//        billingInfo.setAmount("25.00");
//        billingInfo.setResponseCode("100");
//        billingInfo.setTransactionTime(chargeDate);
//        a.getBillingInfo().add(billingInfo);
//
//        AccountActivity activity = new AccountActivity();
//        activity.setAccountType(Account.BASIC);
//        activity.setTransactionDate(chargeDate);
//        activity.setPricingModel(0);
//        Calendar c = Calendar.getInstance();
//        c.setTime(chargeDate);
//        activity.setBillingDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
//
//        a.getActivityInfo().add(activity);
//        assertEquals(0.0, a.calculateCredit());
//    }
//
//    public void testYearlyBilling() {
//        Account a = new Account();
//
//        AccountCreditCardBillingInfo billingInfo = new AccountCreditCardBillingInfo();
//        billingInfo.setAmount("275.00");
//        billingInfo.setResponseCode("100");
//        billingInfo.setTransactionTime(chargeDate);
//        a.getBillingInfo().add(billingInfo);
//
//        AccountActivity activity = new AccountActivity();
//        activity.setAccountType(Account.BASIC);
//        activity.setTransactionDate(chargeDate);
//        activity.setPricingModel(0);
//        Calendar c = Calendar.getInstance();
//        c.setTime(chargeDate);
//        activity.setBillingDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
//
//        a.getActivityInfo().add(activity);
//        assertEquals(0.0, a.calculateCredit());
//    }
//
//
//    public void testBasicAmount() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.BASIC);
//        a.setPricingModel(0);
//        assertEquals(25.0, a.totalAmount());
//    }
//    public void testPlusAmount() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.PLUS);
//        a.setPricingModel(0);
//        assertEquals(75.0, a.totalAmount());
//    }
//
//    public void testProfessionalAmount() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.PROFESSIONAL);
//        a.setPricingModel(0);
//        assertEquals(200.0, a.totalAmount());
//    }
//
//    public void testNewBasicAmount() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.BASIC);
//        a.setPricingModel(1);
//        a.setNumDesigners(1);
//        assertEquals(25.0, a.totalAmount());
//    }
//    public void testNewBasicScaledUsers() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.BASIC);
//        a.setPricingModel(1);
//        a.setNumDesigners(10);
//        assertEquals(115.0, a.totalAmount());
//    }
//    public void testNewPlusAmount() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.PLUS);
//        a.setPricingModel(1);
//        a.setNumDesigners(1);
//        assertEquals(75.0, a.totalAmount());
//    }
//    public void testNewPlusScaledUsers() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.PLUS);
//        a.setPricingModel(1);
//        a.setNumDesigners(5);
//        assertEquals(175.0, a.totalAmount());
//    }
//    public void testNewProfessionalAmount() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.PROFESSIONAL);
//        a.setPricingModel(1);
//        a.setNumDesigners(1);
//        assertEquals(200.0, a.totalAmount());
//    }
//
//    public void testNewProfessionalScaledUsers() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.PROFESSIONAL);
//        a.setPricingModel(1);
//        a.setNumDesigners(5);
//        assertEquals(400.0, a.totalAmount());
//    }
//
//    public void testNewProfessionalDataTiers() {
//        AccountActivity a = new AccountActivity();
//        a.setAccountType(Account.PROFESSIONAL);
//        a.setPricingModel(1);
//        a.setNumDesigners(5);
//        a.setDataTier(Account.PROFESSIONAL_MAX_4);
//        assertEquals(850.0, a.totalAmount());
//    }
}
