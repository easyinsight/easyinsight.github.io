package com.easyinsight.datafeeds.infusionsoft;

/**
 * User: jamesboe
 * Date: 9/12/14
 * Time: 5:36 PM
 */
public class InfusionsoftUser {
    private String id;
    private String email;

    public InfusionsoftUser() {
    }

    public InfusionsoftUser(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
