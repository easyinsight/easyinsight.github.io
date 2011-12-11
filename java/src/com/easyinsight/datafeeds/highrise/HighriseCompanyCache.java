package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/25/11
 * Time: 10:18 AM
 */
public class HighriseCompanyCache extends HighRiseBaseSource {
    private List<HighriseCompany> companyList = new ArrayList<HighriseCompany>();
    private Set<String> companyIDs = new HashSet<String>();

    public List<HighriseCompany> getCompanyList() {
        return companyList;
    }

    public Set<String> getCompanyIDs() {
        return companyIDs;
    }

    public void populateCaches(HttpClient client, String url, FeedDefinition parentDefinition, Date lastTime, HighriseCache highriseCache) throws HighRiseLoginException, ParsingException, ParseException {

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);


        Builder builder = new Builder();
        try {
            int offset = 0;
            int companyCount;
            do {
                companyCount = 0;
                Document companies;
                if (offset == 0) {
                    companies = runRestRequest("/companies.xml?", client, builder, url, true, false, parentDefinition);
                } else {
                    companies = runRestRequest("/companies.xml?n=" + offset, client, builder, url, true, false, parentDefinition);
                }
                Nodes companyNodes = companies.query("/companies/company");
                for (int i = 0; i < companyNodes.size(); i++) {

                    Node companyNode = companyNodes.get(i);
                    String name = queryField(companyNode, "name/text()");
                    String id = queryField(companyNode, "id/text()");
                    String background = queryField(companyNode, "background/text()");
                    String zip = "";
                    String city = "";
                    String state = "";
                    String country = "";
                    String tagString = "";
                    String street = "";

                    Nodes contactDataNodes = companyNode.query("contact-data/addresses/address");
                    if (contactDataNodes.size() > 0) {
                        Node contactDataNode = contactDataNodes.get(0);
                        zip = queryField(contactDataNode, "zip/text()");
                        country = queryField(contactDataNode, "country/text()");
                        state = queryField(contactDataNode, "state/text()");
                        city = queryField(contactDataNode, "city/text()");
                        street = queryField(contactDataNode, "street/text()");
                    }
                    Date createdAt = deadlineFormat.parse(queryField(companyNode, "created-at/text()"));

                    Date updatedAt = deadlineFormat.parse(queryField(companyNode, "updated-at/text()"));
                    String personId = queryField(companyNode, "author-id/text()");
                    String responsiblePartyName = highriseCache.getUserName(personId);

                    Nodes tagNodes = companyNode.query("tags/tag/name/text()");
                    if (tagNodes.size() > 0) {
                        StringBuilder tagBuilder = new StringBuilder();
                        for (int j = 0; j < tagNodes.size(); j++) {
                            String tag = tagNodes.get(j).getValue();
                            tagBuilder.append(tag).append(",");
                        }
                        tagString = tagBuilder.substring(0, tagBuilder.length() - 1);

                    }
                    Map<String, String> customFields = new HashMap<String, String>();
                    contactDataNodes = companyNode.query("subject_datas/subject_data");
                    if (contactDataNodes.size() > 0) {
                        for (int j = 0; j < contactDataNodes.size(); j++) {
                            Node contactDataNode = contactDataNodes.get(j);
                            String subjectFieldID = queryField(contactDataNode, "subject_field_id/text()");
                            String value = queryField(contactDataNode, "value/text()");
                            customFields.put(subjectFieldID, value);
                        }
                    }
                    
                    String workEmail = "";
                    String homeEmail = "";
                    String otherEmail = "";

                    Nodes emailNodes = companyNode.query("contact-data/email-addresses/email-address");
                    for (int j = 0; j < emailNodes.size(); j++) {
                        Node emailNode = emailNodes.get(j);
                        String location = queryField(emailNode, "location/text()");
                        String email = queryField(emailNode, "address/text()");
                        if ("Work".equals(location)) {
                            workEmail = email;
                        } else if ("Home".equals(location)) {
                            homeEmail = email;
                        } else if ("Other".equals(location)) {
                            otherEmail = email;
                        }
                    }
                    
                    String mobilePhone = "";
                    String workPhone = "";
                    String faxPhone = "";
                    String homePhone = "";

                    Nodes phoneNodes = companyNode.query("contact-data/phone-numbers/phone-number");
                    for (int j = 0; j < phoneNodes.size(); j++) {
                        Node phoneNode = phoneNodes.get(j);
                        String phoneNumber = queryField(phoneNode, "number/text()");
                        String location = queryField(phoneNode, "location/text()");
                        if ("Mobile".equals(location)) {
                            mobilePhone = phoneNumber;
                        } else if ("Work".equals(location)) {
                            workPhone = phoneNumber;
                        } else if ("Fax".equals(location)) {
                            faxPhone = phoneNumber;
                        } else if ("Home".equals(location)) {
                            homePhone = phoneNumber;
                        }
                    }


                    companyList.add(new HighriseCompany(name, id, tagString, responsiblePartyName, createdAt, updatedAt, zip, background,
                            country, state, city, street, workPhone, homePhone, mobilePhone, faxPhone, workEmail, homeEmail, otherEmail, customFields));
                    companyIDs.add(id);
                    companyCount++;
                }
                offset += 500;
            } while (companyCount == 500);
        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
