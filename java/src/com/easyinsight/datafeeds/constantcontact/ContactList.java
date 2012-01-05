package com.easyinsight.datafeeds.constantcontact;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/4/12
 * Time: 3:23 PM
 */
public class ContactList {
    private String id;
    private String name;
    private String shortName;
    private List<String> users = new ArrayList<String>();

    public ContactList(String id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public List<String> getUsers() {
        return users;
    }
}
