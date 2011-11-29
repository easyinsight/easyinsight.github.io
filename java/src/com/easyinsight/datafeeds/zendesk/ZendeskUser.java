package com.easyinsight.datafeeds.zendesk;

/**
 * User: jamesboe
 * Date: 11/29/11
 * Time: 10:16 AM
 */
public class ZendeskUser {
    private String name;
    private String id;
    private String organizationID;
    private String role;
    private String email;

    public ZendeskUser(String name, String id, String organizationID, String role, String email) {
        this.name = name;
        this.id = id;
        this.organizationID = organizationID;
        this.role = role;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}
