package com.easyinsight.users;

import com.easyinsight.PasswordStorage;
import com.easyinsight.security.SecurityUtil;

/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 9:42:07 PM
 */
public class Credentials {
    public static final int UNKNOWN = 1;
    public static final int SUCCESSFUL = 2;
    public static final int FAILED = 3;

    //private long accountID;
    private String userName;
    private String password;
    private int failureState = UNKNOWN;

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    private boolean encrypted = false;


    public Credentials() {
    }

    public Credentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public int getFailureState() {
        return failureState;
    }

    public void setFailureState(int failureState) {
        this.failureState = failureState;
    }

    /*public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    } */

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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credentials that = (Credentials) o;

        return password.equals(that.password) && userName.equals(that.userName);

    }

    public int hashCode() {
        int result;
        result = userName.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    public Credentials decryptCredentials() throws MalformedCredentialsException {
        Credentials c = new Credentials();
        String s = PasswordStorage.decryptString(getUserName());
        int i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if(i == -1) {
            throw new MalformedCredentialsException();
        }
        c.setUserName(s.substring(0, i));
        s = PasswordStorage.decryptString(getPassword());
        i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if(i == -1)
            throw new MalformedCredentialsException();
        c.setPassword(s.substring(0, i));
        return c;
    }
}
