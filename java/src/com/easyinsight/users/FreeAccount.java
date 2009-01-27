package com.easyinsight.users;

import javax.persistence.DiscriminatorValue;

/**
 * User: James Boe
 * Date: Jun 23, 2008
 * Time: 6:49:27 PM
 */
public class FreeAccount extends AccountType {
    public boolean isPrivateSharingAllowed() {
        return false;
    }

    public long getSpaceAllowed() {
        return 1000000;
    }

    public int getAccountType() {
        return AccountType.FREE;
    }
}
