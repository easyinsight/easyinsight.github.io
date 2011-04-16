package com.easyinsight.users;

import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 4/13/11
 * Time: 3:23 PM
 */
public class SuggestedUser {
    private String firstName;
    private String lastName;
    private String userName;
    private String emailAddress;

    public SuggestedUser() {
    }

    public SuggestedUser(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public void fillInInfo(EIConnection conn) throws SQLException {
        boolean validUserName = false;
        PreparedStatement stmt = conn.prepareStatement("SELECT USER_ID FROM USER WHERE USERNAME = ?");
        String baseName;
        if (this.userName == null) {
            baseName = firstName.toLowerCase().charAt(0) + lastName.toLowerCase();
        } else {
            baseName = this.userName;
        }
        String userName = baseName;
        int i = 1;
        do {
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userName = baseName + i++;
            } else {
                validUserName = true;
            }
        } while (!validUserName);
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
