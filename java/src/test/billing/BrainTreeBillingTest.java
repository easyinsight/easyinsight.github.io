package test.billing;

import junit.framework.TestCase;
import com.easyinsight.billing.BrainTreeBillingSystem;

import java.util.Map;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 29, 2009
 * Time: 10:11:22 PM
 */
public class BrainTreeBillingTest extends TestCase {
    private BrainTreeBillingSystem billingSystem;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        billingSystem = new BrainTreeBillingSystem();
        billingSystem.setUsername("testapi");
        billingSystem.setPassword("password1");
    }

    public void testBillTenDollars() {
        Map<String, String> params = billingSystem.billAccount(52, 10.00);
        assertEquals("1", params.get("response"));
        assertTrue(Arrays.asList("X", "Y", "D", "M", "W", "Z", "P", "L").contains(params.get("avsresponse")));
    }
}
