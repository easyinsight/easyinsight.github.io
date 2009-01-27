package com.easyinsight.users;

/**
 * User: James Boe
 * Date: Jun 24, 2008
 * Time: 12:23:04 PM
 */
public abstract class AccountType {

    public static final int FREE = 1;
    public static final int INDIVIDUAL = 2;
    public static final int COMMERCIAL = 3;

    public abstract boolean isPrivateSharingAllowed();
    public abstract int getAccountType();
}
