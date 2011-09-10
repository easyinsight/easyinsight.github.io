package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: 3/24/11
 * Time: 9:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class HarvestClientContactSource extends HarvestBaseSource {

    public static final String ID = "Contact ID";
    public static final String CLIENT_ID =  "Contact Client ID";
    public static final String EMAIL = "Contact Email";
    public static final String FIRST_NAME = "Contact First Name";
    public static final String LAST_NAME = "Contact Last Name";
    public static final String FULL_NAME = "Contact Full Name";
    public static final String OFFICE_PHONE = "Contact Office Phone #";
    public static final String MOBILE_PHONE = "Contact Mobile Phone #";
    public static final String FAX = "Contacts Fax #";
    public static final String COUNT = "Contacts Count";

    public HarvestClientContactSource() {
        setFeedName("Contacts");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_CONTACTS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, CLIENT_ID, EMAIL, FIRST_NAME, LAST_NAME, FULL_NAME, OFFICE_PHONE, MOBILE_PHONE, FAX, COUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisItem contactId = new AnalysisDimension(keys.get(ID), true);
        contactId.setHidden(true);
        analysisItems.add(contactId);
        AnalysisItem clientId = new AnalysisDimension(keys.get(CLIENT_ID), true);
        clientId.setHidden(true);
        analysisItems.add(clientId);
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL), true));
        analysisItems.add(new AnalysisDimension(keys.get(FIRST_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(LAST_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(FULL_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(OFFICE_PHONE), true));
        analysisItems.add(new AnalysisDimension(keys.get(MOBILE_PHONE), true));
        analysisItems.add(new AnalysisDimension(keys.get(FAX), true));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        try {
            Document contacts = runRestRequest("/contacts", client, builder, source.getUrl(), true, source, false);
            Nodes contactNodes = contacts.query("/contacts/contact");
            for(int i = 0;i < contactNodes.size();i++) {
                Node curContact = contactNodes.get(i);
                String contactId = queryField(curContact, "id/text()");
                String clientId = queryField(curContact, "client-id/text()");
                String email = queryField(curContact, "email/text()");
                String firstName = queryField(curContact, "first-name/text()");
                String lastName = queryField(curContact, "last-name/text()");
                String officePhone = queryField(curContact,  "phone-office/text()");
                String mobilePhone = queryField(curContact, "phone-mobile/text()");
                String fax = queryField(curContact, "fax/text()");
                String fullName = firstName + " " + lastName;
                IRow row = dataSet.createRow();
                row.addValue(keys.get(ID), contactId);
                row.addValue(keys.get(CLIENT_ID), clientId);
                row.addValue(keys.get(EMAIL), email);
                row.addValue(keys.get(FIRST_NAME), firstName);
                row.addValue(keys.get(LAST_NAME), lastName);
                row.addValue(keys.get(FULL_NAME), fullName);
                row.addValue(keys.get(OFFICE_PHONE), officePhone);
                row.addValue(keys.get(MOBILE_PHONE), mobilePhone);
                row.addValue(keys.get(FAX), fax);
                row.addValue(keys.get(COUNT), 1.0);
            }
        } catch (ParsingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return dataSet;
    }


}
