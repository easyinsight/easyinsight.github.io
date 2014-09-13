package com.easyinsight.datafeeds.batchbook2;

import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 7/31/12
 * Time: 3:15 PM
 */
public class Company extends Entity {
    private String name;

    public Company(String about, String id, List<Stuff> emails, List<Stuff> phones, List<Stuff> websites, List<Address> addresses,
                   List<String> tags, String name, List<CustomFieldValue> customFieldValues, Date createdAt, Date updatedAt) {
        super(about, id, emails, phones, websites, addresses, tags, customFieldValues, createdAt, updatedAt);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
