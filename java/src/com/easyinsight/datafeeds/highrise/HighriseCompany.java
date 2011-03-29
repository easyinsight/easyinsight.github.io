package com.easyinsight.datafeeds.highrise;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 3/25/11
 * Time: 10:15 AM
 */
public class HighriseCompany {

    private String companyName;
    private String companyID;
    private String tags;
    private String owner;
    private Date createdAt;
    private Date updatedAt;
    private String zipCode;
    private String background;

    public HighriseCompany(String companyName, String companyID, String tags, String owner, Date createdAt, Date updatedAt, String zipCode, String background) {
        this.companyName = companyName;
        this.companyID = companyID;
        this.tags = tags;
        this.owner = owner;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.zipCode = zipCode;
        this.background = background;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getTags() {
        return tags;
    }

    public String getOwner() {
        return owner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getBackground() {
        return background;
    }
}
