package com.easyinsight.client;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 2:07:27 PM
 */
public class GWTUser implements Serializable {
    private String userName;
    private boolean admin;
    private String eiName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getEiName() {
        return eiName;
    }

    public void setEiName(String eiName) {
        this.eiName = eiName;
    }
}
