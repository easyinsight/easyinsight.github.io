package com.easyinsight.users;

import javax.persistence.DiscriminatorValue;

/**
 * User: James Boe
 * Date: Jun 23, 2008
 * Time: 6:49:47 PM
 */
public class CommercialAccount extends AccountType {
    public boolean isPrivateSharingAllowed() {
        return true;
    }

    public long getSpaceAllowed() {
        return 50000000;
    }

    public int getAccountType() {
        return AccountType.COMMERCIAL;
    }
}
