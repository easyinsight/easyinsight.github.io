package com.easyinsight.datafeeds.constantcontact;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 1/5/12
 * Time: 10:36 AM
 */
public class Campaign {
    private String name;
    private String id;
    private String status;
    private Date date;
    private String url;
    private int camapignNumber;

    public Campaign(String name, String id, String status, Date date, String url) {
        this.name = name;
        this.id = id;
        this.status = status;
        this.date = date;
        this.url = url;
    }

    public int getCamapignNumber() {
        return camapignNumber;
    }

    public void setCamapignNumber(int camapignNumber) {
        this.camapignNumber = camapignNumber;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
