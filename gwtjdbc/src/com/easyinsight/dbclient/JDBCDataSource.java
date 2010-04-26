package com.easyinsight.dbclient;

import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Type;

/**
 * User: jamesboe
 * Date: Apr 12, 2010
 * Time: 5:29:03 PM
 */
@Entity
public class JDBCDataSource extends DataSource {
    private String connectString;
    private String userName;
    @Type(type="encrypted_string")
    private String password;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
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
}
