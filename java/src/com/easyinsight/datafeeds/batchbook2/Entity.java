package com.easyinsight.datafeeds.batchbook2;

import java.util.List;

/**
 * User: jamesboe
 * Date: 7/31/12
 * Time: 2:56 PM
 */
public class Entity {
    private String about;

    private String id;
    private List<Stuff> emails;
    private List<Stuff> phones;
    private List<Stuff> websites;
    private List<String> tags;
    private List<Address> addresses;
    private List<CustomFieldValue> customFieldValues;

    public Entity(String about, String id, List<Stuff> emails, List<Stuff> phones, List<Stuff> websites, List<Address> addresses,
                  List<String> tags, List<CustomFieldValue> customFieldValues) {
        this.about = about;
        this.id = id;
        this.emails = emails;
        this.phones = phones;
        this.websites = websites;
        this.addresses = addresses;
        this.tags = tags;
        this.customFieldValues = customFieldValues;
    }

    public List<CustomFieldValue> getCustomFieldValues() {
        return customFieldValues;
    }

    public String getAbout() {
        return about;
    }

    public String getId() {
        return id;
    }

    public List<Stuff> getEmails() {
        return emails;
    }

    public List<Stuff> getPhones() {
        return phones;
    }

    public List<Stuff> getWebsites() {
        return websites;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
