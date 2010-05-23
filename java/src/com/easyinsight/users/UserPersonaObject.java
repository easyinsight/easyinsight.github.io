package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: May 22, 2010
 * Time: 6:10:50 PM
 */
public class UserPersonaObject extends UserTransferObject {
    private int persona;
    private boolean badEmail;
    private boolean badUserName;

    public boolean isBadUserName() {
        return badUserName;
    }

    public void setBadUserName(boolean badUserName) {
        this.badUserName = badUserName;
    }

    public boolean isBadEmail() {
        return badEmail;
    }

    public void setBadEmail(boolean badEmail) {
        this.badEmail = badEmail;
    }

    public int getPersona() {
        return persona;
    }

    public void setPersona(int persona) {
        this.persona = persona;
    }
}
