package com.easyinsight.datafeeds.highrise;

import java.util.Date;
import java.util.Map;

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
    private String state;
    private String country;
    private String city;
    private String street;
    private String officePhone;
    private String homePhone;
    private String mobilePhone;
    private String faxPhone;
    private String officeEmail;
    private String homeEmail;
    private String otherEmail;
    private Map<String, String> customFields;

    public HighriseCompany(String companyName, String companyID, String tags, String owner, Date createdAt, Date updatedAt, String zipCode, String background,
                           String country, String state, String city, String street, String officePhone, String homePhone, String mobilePhone, String faxPhone,
                           String officeEmail, String homeEmail, String otherEmail, Map<String, String> customFields) {
        this.companyName = companyName;
        this.companyID = companyID;
        this.tags = tags;
        this.owner = owner;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.zipCode = zipCode;
        this.background = background;
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.homeEmail = homeEmail;
        this.otherEmail = otherEmail;
        this.homePhone = homePhone;
        this.mobilePhone = mobilePhone;
        this.faxPhone = faxPhone;
        this.customFields = customFields;
    }

    public String getStreet() {
        return street;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getFaxPhone() {
        return faxPhone;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public String getHomeEmail() {
        return homeEmail;
    }

    public String getOtherEmail() {
        return otherEmail;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
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
