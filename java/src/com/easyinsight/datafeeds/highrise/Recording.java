package com.easyinsight.datafeeds.highrise;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 3/24/11
 * Time: 11:46 AM
 */
public class Recording {
    private String body;
    private Date createdAt;
    private Date updatedAt;
    private String contactID;
    private String companyID;
    private String subjectID;
    private String id;
    private String author;

    public Recording(String body, Date createdAt, Date updatedAt, String subjectID, String id, String author) {
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.subjectID = subjectID;
        this.id = id;
        this.author = author;
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

    public String getBody() {
        return body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }
}
