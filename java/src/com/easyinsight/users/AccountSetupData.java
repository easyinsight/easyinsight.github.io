package com.easyinsight.users;

import com.easyinsight.preferences.Persona;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: May 22, 2010
 * Time: 6:07:39 PM
 */
public class AccountSetupData {

    public static final int BIZ_USER = 0;
    public static final int DEVELOPER = 1;
    public static final int BI_GURU = 2;

    private int myPersona;
    private List<Persona> personas = new ArrayList<Persona>();
    private List<UserPersonaObject> users = new ArrayList<UserPersonaObject>();
    private int dateFormat;

    public int getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(int dateFormat) {
        this.dateFormat = dateFormat;
    }

    public List<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
    }

    public int getMyPersona() {
        return myPersona;
    }

    public void setMyPersona(int myPersona) {
        this.myPersona = myPersona;
    }

    public List<UserPersonaObject> getUsers() {
        return users;
    }

    public void setUsers(List<UserPersonaObject> users) {
        this.users = users;
    }
}
