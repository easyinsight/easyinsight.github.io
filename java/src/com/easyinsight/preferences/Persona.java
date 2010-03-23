package com.easyinsight.preferences;

/**
 * User: jamesboe
 * Date: Mar 20, 2010
 * Time: 12:01:17 PM
 */
public class Persona {
    private String name;
    private long personaID;
    private UISettings uiSettings;

    public UISettings getUiSettings() {
        return uiSettings;
    }

    public void setUiSettings(UISettings uiSettings) {
        this.uiSettings = uiSettings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPersonaID() {
        return personaID;
    }

    public void setPersonaID(long personaID) {
        this.personaID = personaID;
    }
}
