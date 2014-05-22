package com.easyinsight.datafeeds.solve360;

import com.easyinsight.core.Key;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 1/31/14
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Company {
    private Integer companyId;
    private String categories;
    private String name;
    private String shippingAddress;
    private String billingAddress;
    private String mainAddress;
    private String website;
    private String companyPhone;
    private String faxNumber;
    private String responsibleParty;
    private String company;
    private Map<Key, Object> customFieldValues;

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Map<Key, Object> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(Map<Key, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setMainAddress(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setResponsibleParty(String responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    public String getResponsibleParty() {
        return responsibleParty;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }
}
