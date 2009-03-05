package com.easyinsight.users;

/**
 * User: James Boe
 * Date: Mar 4, 2009
 * Time: 2:39:48 PM
 */
public class ConsultantTO {
    private long consultantID;
    private UserTransferObject userTransferObject;
    private int state;

    public long getConsultantID() {
        return consultantID;
    }

    public void setConsultantID(long consultantID) {
        this.consultantID = consultantID;
    }

    public UserTransferObject getUserTransferObject() {
        return userTransferObject;
    }

    public void setUserTransferObject(UserTransferObject userTransferObject) {
        this.userTransferObject = userTransferObject;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
