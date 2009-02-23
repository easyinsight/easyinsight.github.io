package com.easyinsight.solutions;

/**
 * User: James Boe
 * Date: Jan 11, 2009
 * Time: 11:44:05 PM
 */
public class SolutionInstallInfo {

    public static final int DATA_SOURCE = 1;
    public static final int INSIGHT = 2;

    private long previousID;
    private long newID;
    private int type;

    public SolutionInstallInfo() {
    }

    public SolutionInstallInfo(long previousID, long newID, int type) {
        this.previousID = previousID;
        this.newID = newID;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPreviousID() {
        return previousID;
    }

    public void setPreviousID(long previousID) {
        this.previousID = previousID;
    }

    public long getNewID() {
        return newID;
    }

    public void setNewID(long newID) {
        this.newID = newID;
    }
}
