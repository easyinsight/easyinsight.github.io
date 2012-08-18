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

        List people = (List) runRestRequest("/people.json", client, parentDefinition).get("people");
        List<Person> peopleList = new ArrayList<Person>();
        for (Object personObject : people) {
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

            for (Object companyObject : companies) {
                Map company = (Map) companyObject;
                Stuff stuff = new Stuff();
                stuff.setPart1(company.get("company_id").toString());
                stuff.setPart2((company.get("current")).toString());
                stuff.setPart3((String) company.get("job_title"));
                companyStuff.add(stuff);
            }
            peopleList.add(new Person(about, id, emailStuff, phoneStuff, websiteStuff, addressList, tagList, firstName, lastName, companyStuff, customFieldValues));
        }
        this.people = peopleList;

        List companies = (List) runRestRequest("/companies.json", client, parentDefinition).get("companies");
        List<Company> companyList = new ArrayList<Company>();
        for (Object personObject : companies) {
            Map person = (Map) personObject;
            String id = (person.get("id")).toString();
            String about = (String) person.get("about");
            String name = (String) person.get("name");

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
        this.companies = companyList;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }
}
