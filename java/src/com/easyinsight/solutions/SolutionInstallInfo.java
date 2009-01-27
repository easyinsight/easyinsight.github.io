package com.easyinsight.solutions;

/**
 * User: James Boe
 * Date: Jan 11, 2009
 * Time: 11:44:05 PM
 */
public class SolutionInstallInfo {
    private long previousID;
    private long newID;

    public SolutionInstallInfo() {
    }

    public SolutionInstallInfo(long previousID, long newID) {
        this.previousID = previousID;
        this.newID = newID;
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
