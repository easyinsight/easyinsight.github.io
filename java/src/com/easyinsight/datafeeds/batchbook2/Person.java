package com.easyinsight.datafeeds.batchbook2;

import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 7/31/12
 * Time: 3:15 PM
 */
public class Person extends Entity {
    private String firstName;
    private String lastName;
    private List<Stuff> companies;

    public Person(String about, String id, List<Stuff> emails, List<Stuff> phones, List<Stuff> websites, List<Address> addresses,
                  List<String> tags, String firstName, String lastName, List<Stuff> companies, List<CustomFieldValue> customFieldValues,
                  Date createdAt, Date updatedAt) {
        super(about, id, emails, phones, websites, addresses, tags, customFieldValues, createdAt, updatedAt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.companies = companies;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Stuff> getCompanies() {
        return companies;
    }
}
