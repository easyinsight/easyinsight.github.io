package test.billing;

import com.easyinsight.billing.BillingSystem;
import com.easyinsight.billing.BrainTreeBlueBillingSystem;
import com.easyinsight.users.AccountCreditCardBillingInfo;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 29, 2009
 * Time: 10:11:22 PM
 */
public class BrainTreeBillingTest extends TestCase {
    private BillingSystem billingSystem;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        billingSystem = new BrainTreeBillingSystem("testapi", "password1");
        billingSystem = new BrainTreeBlueBillingSystem();
    }

    public void testBillTenDollars() {
        AccountCreditCardBillingInfo info = billingSystem.billAccount(151, 10.00);
        assertTrue(info.isSuccessful());
        info.toString();
        /*assertEquals("1", params.get("response"));
        assertTrue(Arrays.asList("X", "Y", "D", "M", "W", "Z", "P", "L").contains(params.get("avsresponse")));*/
    }

    public void testDelete() {
        billingSystem.cancelPlan(151);

    }
}
