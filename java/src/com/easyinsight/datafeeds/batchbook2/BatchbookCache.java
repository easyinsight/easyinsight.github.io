package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.datafeeds.FeedDefinition;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
                String id = (person.get("id")).toString();
                String about = (String) person.get("about");
                String firstName = (String) person.get("first_name");
                String lastName = (String) person.get("last_name");

                List emails = (List) person.get("emails");
                List<Stuff> emailStuff = new ArrayList<Stuff>();
                for (Object emailObject : emails) {
                    Map email = (Map) emailObject;
                    Stuff stuff = new Stuff();
                    stuff.setPart1((String) email.get("address"));
                    stuff.setPart2((String) email.get("label"));
                    emailStuff.add(stuff);
                }

                List phones = (List) person.get("phones");
                List<Stuff> phoneStuff = new ArrayList<Stuff>();
                for (Object phoneObject : phones) {
                    Map phone = (Map) phoneObject;
                    Stuff stuff = new Stuff();
                    stuff.setPart1((String) phone.get("number"));
                    stuff.setPart2((String) phone.get("label"));
                    phoneStuff.add(stuff);
                }

                List websites = (List) person.get("websites");
                List<Stuff> websiteStuff = new ArrayList<Stuff>();
                for (Object websiteObject : websites) {
                    Map website = (Map) websiteObject;
                    Stuff stuff = new Stuff();
                    stuff.setPart1((String) website.get("address"));
                    stuff.setPart2((String) website.get("label"));
                    websiteStuff.add(stuff);
                }

                List addresses = (List) person.get("addresses");
                List<Address> addressList = new ArrayList<Address>();
                for (Object addressObject : addresses) {
                    Map address = (Map) addressObject;
                    addressList.add(new Address((String) address.get("address_1"), (String) address.get("address_2"),
                            (String) address.get("city"), (String) address.get("state"), (String) address.get("postal_code"),
                            (String) address.get("country"), (String) address.get("label")));
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

                List<Stuff> companyStuff = new ArrayList<Stuff>();
                List companies = (List) person.get("company_affiliations");

                if (companies != null) {
                    for (Object companyObject : companies) {
                        Map company = (Map) companyObject;
                        Stuff stuff = new Stuff();
                        stuff.setPart1(company.get("company_id").toString());
                        stuff.setPart2((String) company.get("primary").toString());
                        stuff.setPart3((String) company.get("job_title"));
                        companyStuff.add(stuff);
                    }
                }
                peopleList.add(new Person(about, id, emailStuff, phoneStuff, websiteStuff, addressList, tagList, firstName, lastName, companyStuff, customFieldValues));
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
                String id = (person.get("id")).toString();
                String about = (String) person.get("about");
                String name = (String) person.get("name");
                if ("Delta Metrics".equals(name)){
                    System.out.println(".");
                }

                List emails = (List) person.get("emails");
                List<Stuff> emailStuff = new ArrayList<Stuff>();
                for (Object emailObject : emails) {
                    Map email = (Map) emailObject;
                    Stuff stuff = new Stuff();
                    stuff.setPart1((String) email.get("address"));
                    stuff.setPart2((String) email.get("label"));
                    emailStuff.add(stuff);
                }

                List phones = (List) person.get("phones");
                List<Stuff> phoneStuff = new ArrayList<Stuff>();
                for (Object phoneObject : phones) {
                    Map phone = (Map) phoneObject;
                    Stuff stuff = new Stuff();
                    stuff.setPart1((String) phone.get("number"));
                    stuff.setPart2((String) phone.get("label"));
                    phoneStuff.add(stuff);
                }

                List websites = (List) person.get("websites");
                List<Stuff> websiteStuff = new ArrayList<Stuff>();
                for (Object websiteObject : websites) {
                    Map website = (Map) websiteObject;
                    Stuff stuff = new Stuff();
                    stuff.setPart1((String) website.get("address"));
                    stuff.setPart2((String) website.get("label"));
                    websiteStuff.add(stuff);
                }

                List addresses = (List) person.get("addresses");
                List<Address> addressList = new ArrayList<Address>();
                for (Object addressObject : addresses) {
                    Map address = (Map) addressObject;
                    addressList.add(new Address((String) address.get("address_1"), (String) address.get("address_2"),
                            (String) address.get("city"), (String) address.get("state"), (String) address.get("postal_code"),
                            (String) address.get("country"), (String) address.get("label")));
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

                companyList.add(new Company(about, id, emailStuff, phoneStuff, websiteStuff, addressList, tagList, name, customFieldValues));
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
