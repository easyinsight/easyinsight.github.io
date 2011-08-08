package com.easyinsight.preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Mar 20, 2010
 * Time: 12:01:17 PM
 */
public class Persona {
    private String name;
    private long personaID;
    private UISettings uiSettings;
    private List<DataSourceDLS> dataSourceDLS = new ArrayList<DataSourceDLS>();

    public List<DataSourceDLS> getDataSourceDLS() {
        return dataSourceDLS;
    }

    public void setDataSourceDLS(List<DataSourceDLS> dataSourceDLS) {
        this.dataSourceDLS = dataSourceDLS;
    }

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
