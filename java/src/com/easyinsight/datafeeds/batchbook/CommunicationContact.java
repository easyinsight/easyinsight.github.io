package com.easyinsight.datafeeds.batchbook;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 10:00 AM
 */
public class CommunicationContact {
    private String id;
    private String role;

    public CommunicationContact(String id, String role) {
        this.id = id;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}
