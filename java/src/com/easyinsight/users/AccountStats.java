package com.easyinsight.users;

/**
 * User: James Boe
 * Date: Mar 27, 2009
 * Time: 12:34:56 PM
 */
public class AccountStats {
    private long usedSpace;
    private long maxSpace;

    public long getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(long usedSpace) {
        this.usedSpace = usedSpace;
    }

    public long getMaxSpace() {
        return maxSpace;
    }

    public void setMaxSpace(long maxSpace) {
        this.maxSpace = maxSpace;
    }
}
