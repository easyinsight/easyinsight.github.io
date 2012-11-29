package com.easyinsight.billing;

import com.easyinsight.config.ConfigLoader;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/29/12
 * Time: 7:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class BillingSystemFactory {
    private static int curBillingSystem = ConfigLoader.instance().getBillingSystem();
    private static final int BRAINTREE_ORANGE = 1;
    private static final int BRAINTREE_BLUE = 2;

    public static BillingSystem getBillingSystem() {
        if(curBillingSystem == BillingSystemFactory.BRAINTREE_ORANGE)
            return new BrainTreeBillingSystem(ConfigLoader.instance().getBillingUsername(), ConfigLoader.instance().getBillingPassword());
        else
            return new BrainTreeBlueBillingSystem();
    }
}
