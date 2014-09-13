package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.datafeeds.FeedDefinition;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 7/31/12
 * Time: 3:35 PM
 */
public class BatchbookCache extends Batchbook2BaseSource {
    private List<Person> people;
    private List<Company> companies;

    public List<Person> getPeople() {
        return people;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void populate(HttpClient client, Batchbook2CompositeSource parentDefinition) throws Exception {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        int count;
        int page = 1;
        List<Person> peopleList = new ArrayList<Person>();
        do {
            count = 0;
            List people;
            if (page == 1) {
                people = (List) runRestRequest("/people.json", client, parentDefinition).get("people");
            } else {
                people = (List) runRestRequest("/people.json?page=" + page, client, parentDefinition).get("people");
            }
            page++;
            for (Object personObject : people) {
                count++;
                Map person = (Map) personObject;
                Date createdAt = getDate(person, "created_at", dateTimeFormat);
                Date updatedAt = getDate(person, "updated_at", dateTimeFormat);
                String id = (person.get("id")).toString();
                String about = getValue(person, "about");
                String firstName = getValue(person, "first_name");
                String lastName = getValue(person, "last_name");

                List emails = (List) person.get("emails");
                List<Stuff> emailStuff = new ArrayList<Stuff>();
                if (emails != null) {
                    for (Object emailObject : emails) {
                        Map email = (Map) emailObject;
                        Stuff stuff = new Stuff();
                        stuff.setPart1(getValue(email, "address"));
                        stuff.setPart2(getValue(email, "label"));
                        emailStuff.add(stuff);
                    }
                }

                List phones = (List) person.get("phones");
                List<Stuff> phoneStuff = new ArrayList<Stuff>();
                if (phones != null) {
                    for (Object phoneObject : phones) {
                        Map phone = (Map) phoneObject;
                        Stuff stuff = new Stuff();
                        stuff.setPart1(getValue(phone, "number"));
                        stuff.setPart2(getValue(phone, "label"));
                        phoneStuff.add(stuff);
                    }
                }

                List websites = (List) person.get("websites");
                List<Stuff> websiteStuff = new ArrayList<Stuff>();
                if (websites != null) {
                    for (Object websiteObject : websites) {
                        Map website = (Map) websiteObject;
                        Stuff stuff = new Stuff();
                        stuff.setPart1(getValue(website, "address"));
                        stuff.setPart2(getValue(website, "label"));
                        websiteStuff.add(stuff);
                    }
                }

                List addresses = (List) person.get("addresses");
                List<Address> addressList = new ArrayList<Address>();
                if (addresses != null) {
                    for (Object addressObject : addresses) {
                        Map address = (Map) addressObject;
                        addressList.add(new Address(getValue(address, "address_1"), getValue(address, "address_2"),
                                getValue(address, "city"), getValue(address, "state"), getValue(address, "postal_code"),
                                getValue(address, "country"), getValue(address, "label")));
                    }
                }

                List tags = (List) person.get("tags");
                List<String> tagList = new ArrayList<String>();
                if (tags != null) {
                    for (Object tagObject : tags) {
                        Map tag = (Map) tagObject;
                        tagList.add(getValue(tag, "name"));
                    }
                }

                List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
                List customFieldSets = (List) person.get("cf_records");
                for (Object customFieldObject : customFieldSets) {
                    Map customField = (Map) customFieldObject;
                    String customFieldSetID = customField.get("custom_field_set_id").toString();
                    CustomFieldValue customFieldValue = new CustomFieldValue(customFieldSetID);
                    customFieldValues.add(customFieldValue);
                    List customFieldSetValues = (List) customField.get("custom_field_values");
                    for (Object customFieldValueObject : customFieldSetValues) {
                        Map customFieldValueMap = (Map) customFieldValueObject;
                        String customFieldID = customFieldValueMap.get("custom_field_definition_id").toString();
                        Object value = customFieldValueMap.get("text_value");
                        if (value == null) {
                            value = customFieldValueMap.get("decimal_value");
                        }
                        if (value == null) {
                            System.out.println("No value found!");
                        } else {
                            customFieldValue.addValue(customFieldID, value.toString());
                        }
                    }
                }

                List<Stuff> companyStuff = new ArrayList<Stuff>();
                List companies = (List) person.get("company_affiliations");

                if (companies != null) {
                    for (Object companyObject : companies) {
                        Map company = (Map) companyObject;
                        Stuff stuff = new Stuff();

                        stuff.setPart1(getValue(company, "company_id"));
                        stuff.setPart2(getValue(company, "primary"));
                        stuff.setPart3(getValue(company, "job_title"));
                        companyStuff.add(stuff);
                    }
                }
                peopleList.add(new Person(about, id, emailStuff, phoneStuff, websiteStuff, addressList, tagList, firstName, lastName, companyStuff, customFieldValues, createdAt,
                        updatedAt));
            }
        } while (count == 30);
        this.people = peopleList;

        page = 1;
        List<Company> companyList = new ArrayList<Company>();
        do {
            count = 0;
            List companies;
            if (page == 1) {
                companies = (List) runRestRequest("/companies.json", client, parentDefinition).get("companies");
            } else {
                companies = (List) runRestRequest("/companies.json?page=" + page, client, parentDefinition).get("companies");
            }
            page++;

            for (Object personObject : companies) {
                count++;
                Map person = (Map) personObject;
                Date createdAt = getDate(person, "created_at", dateTimeFormat);
                Date updatedAt = getDate(person, "updated_at", dateTimeFormat);
                String id = (person.get("id")).toString();
                String about = (String) person.get("about");
                String name = (String) person.get("name");

                List emails = (List) person.get("emails");
                List<Stuff> emailStuff = new ArrayList<Stuff>();
                if (emails != null) {
                    for (Object emailObject : emails) {
                        Map email = (Map) emailObject;
                        Stuff stuff = new Stuff();
                        stuff.setPart1(getValue(email, "address"));
                        stuff.setPart2(getValue(email, "label"));
                        emailStuff.add(stuff);
                    }
                }

                List phones = (List) person.get("phones");
                List<Stuff> phoneStuff = new ArrayList<Stuff>();
                if (phones != null) {
                    for (Object phoneObject : phones) {
                        Map phone = (Map) phoneObject;
                        Stuff stuff = new Stuff();
                        stuff.setPart1((String) phone.get("number"));
                        stuff.setPart2((String) phone.get("label"));
                        phoneStuff.add(stuff);
                    }
                }

                List websites = (List) person.get("websites");
                List<Stuff> websiteStuff = new ArrayList<Stuff>();
                if (websites != null) {
                    for (Object websiteObject : websites) {
                        Map website = (Map) websiteObject;
                        Stuff stuff = new Stuff();
                        stuff.setPart1((String) website.get("address"));
                        stuff.setPart2((String) website.get("label"));
                        websiteStuff.add(stuff);
                    }
                }

                List addresses = (List) person.get("addresses");
                List<Address> addressList = new ArrayList<Address>();
                if (addresses != null) {
                    for (Object addressObject : addresses) {
                        Map address = (Map) addressObject;
                        addressList.add(new Address((String) address.get("address_1"), (String) address.get("address_2"),
                                (String) address.get("city"), (String) address.get("state"), (String) address.get("postal_code"),
                                (String) address.get("country"), (String) address.get("label")));
                    }
                }

                List tags = (List) person.get("tags");
                List<String> tagList = new ArrayList<String>();
                for (Object tagObject : tags) {
                    Map tag = (Map) tagObject;
                    tagList.add((String) tag.get("name"));
                }

                List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
                List customFieldSets = (List) person.get("cf_records");
                for (Object customFieldObject : customFieldSets) {
                    Map customField = (Map) customFieldObject;
                    String customFieldSetID = customField.get("custom_field_set_id").toString();
                    CustomFieldValue customFieldValue = new CustomFieldValue(customFieldSetID);
                    customFieldValues.add(customFieldValue);
                    List customFieldSetValues = (List) customField.get("custom_field_values");
                    for (Object customFieldValueObject : customFieldSetValues) {
                        Map customFieldValueMap = (Map) customFieldValueObject;
                        String customFieldID = customFieldValueMap.get("custom_field_definition_id").toString();
                        Object value = customFieldValueMap.get("text_value");
                        if (value == null) {
                            value = customFieldValueMap.get("decimal_value");
                        }
                        if (value == null) {
                            System.out.println("No value found!");
                        } else {
                            customFieldValue.addValue(customFieldID, value.toString());
                        }
                    }
                }

                companyList.add(new Company(about, id, emailStuff, phoneStuff, websiteStuff, addressList, tagList, name, customFieldValues, createdAt, updatedAt));
            }
        } while (count == 30);
        this.companies = companyList;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }
}
