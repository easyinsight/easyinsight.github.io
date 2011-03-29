package com.easyinsight.datafeeds.highrise;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 3/28/11
 * Time: 4:57 PM
 */
public class HighriseEmail {
    private String authorName;
    private Date sentAt;
    private String contactID;
    private String dealID;
    private String caseID;
    private String companyID;
    private String id;

    public HighriseEmail(String authorName, Date sentAt, String id) {
        this.authorName = authorName;
        this.sentAt = sentAt;
        this.id = id;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public void setDealID(String dealID) {
        this.dealID = dealID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getDealID() {
        return dealID;
    }

    public String getCaseID() {
        return caseID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public String getContactID() {
        return contactID;
    }
}
