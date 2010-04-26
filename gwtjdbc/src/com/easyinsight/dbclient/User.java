package com.easyinsight.dbclient;

import org.hibernate.annotations.*;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Apr 12, 2010
 * Time: 5:27:26 PM
 */
@Entity
public class User implements Serializable {

    @Type(type="encrypted_string")
    private String password;
    private String userName;
    //@Id @GeneratedValue(generator="hibseq")
    @GenericGenerator(name="hibseq", strategy = "seqhilo")
    private Long userID;

    private boolean admin;
    private String eiName;
    @Type(type="encrypted_string")
    private String eiPassword;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEiPassword() {
        return eiPassword;
    }

    public void setEiPassword(String eiPassword) {
        this.eiPassword = eiPassword;
    }
}
