package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: 9/12/14
 * Time: 11:57 AM
 */
public class Consultant {
    private String consultantName;
    private String account;

    public Consultant() {
    }

    public Consultant(String consultantName, String account) {
        this.consultantName = consultantName;
        this.account = account;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
