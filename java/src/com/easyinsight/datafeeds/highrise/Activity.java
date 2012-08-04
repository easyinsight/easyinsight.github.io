package com.easyinsight.datafeeds.highrise;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 6/28/12
 * Time: 9:58 AM
 */
public class Activity {
    private String body;
    private Date createdAt;
    private Date updatedAt;
    private String contactID;
    private String companyID;
    private String dealID;
    private String caseID;
    private String id;
    private String author;
    private String activityType;

    public Activity(String body, Date createdAt, Date updatedAt, String contactID, String companyID, String dealID, String caseID, String activityType, String id, String author) {
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.contactID = contactID;
        this.companyID = companyID;
        this.dealID = dealID;
        this.caseID = caseID;
        this.activityType = activityType;
        this.id = id;
        this.author = author;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getDealID() {
        return dealID;
    }

    public void setDealID(String dealID) {
        this.dealID = dealID;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
