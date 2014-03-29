package com.easyinsight.datafeeds.insightly;

/**
 * User: jamesboe
 * Date: 3/25/14
 * Time: 8:16 AM
 */
public class InsightlyLink {
    private String role;
    private String linkedID;

    public InsightlyLink(String role, String linkedID) {
        this.role = role;
        this.linkedID = linkedID;
    }

    public String getRole() {
        return role;
    }

    public String getLinkedID() {
        return linkedID;
    }
}
