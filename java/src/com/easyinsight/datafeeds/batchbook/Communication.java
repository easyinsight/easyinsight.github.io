package com.easyinsight.datafeeds.batchbook;

import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 10:00 AM
 */
public class Communication {
    private List<CommunicationContact> contacts;
    private String subject;
    private String tags;
    private String type;
    private Date date;
    private String id;
    private String body;

    public Communication(List<CommunicationContact> contacts, String subject, String tags, String type, Date date, String id, String body) {
        this.contacts = contacts;
        this.subject = subject;
        this.tags = tags;
        this.type = type;
        this.date = date;
        this.id = id;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }

    public String getTags() {
        return tags;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public List<CommunicationContact> getContacts() {
        return contacts;
    }
}
