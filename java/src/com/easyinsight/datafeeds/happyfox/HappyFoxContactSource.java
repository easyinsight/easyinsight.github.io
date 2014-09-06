package com.easyinsight.datafeeds.happyfox;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 8/15/14
 * Time: 11:27 AM
 */
public class HappyFoxContactSource extends HappyFoxBaseSource {

    public static final String CONTACT_ID = "Contact ID";
    public static final String CONTACT_EMAIL = "Contact Email";
    public static final String CONTACT_NAME = "Contact Name";
    public static final String CONTACT_COUNT = "Contact Count";

    public HappyFoxContactSource() {
        setFeedName("Contacts");
    }

    private transient Map<String, AnalysisItem> customFields;

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(CONTACT_ID, new AnalysisDimension());
        fieldBuilder.addField(CONTACT_EMAIL, new AnalysisDimension());
        fieldBuilder.addField(CONTACT_NAME, new AnalysisDimension());
        fieldBuilder.addField(CONTACT_COUNT, new AnalysisMeasure());
        HappyFoxCompositeSource happyFoxCompositeSource = (HappyFoxCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(happyFoxCompositeSource.getHfApiKey(), happyFoxCompositeSource.getAuthKey());
        List<Map> userCustomFields = runRestRequestForList("user_custom_fields/", client, happyFoxCompositeSource);
        customFields = new HashMap<>();
        for (Map userCustomField : userCustomFields) {
            String name = userCustomField.get("name").toString();
            String customFieldID = userCustomField.get("id").toString();
            String type = userCustomField.get("type").toString();
            if ("text".equals(type) || "multiple_choice".equals(type)) {
                AnalysisDimension customField = new AnalysisDimension(name);
                fieldBuilder.addField(customFieldID, customField);
                customFields.put(customFieldID, customField);
            }
        }
    }



    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = new DataSet();

            HappyFoxCompositeSource happyFoxCompositeSource = (HappyFoxCompositeSource) parentDefinition;
            HttpClient client = getHttpClient(happyFoxCompositeSource.getHfApiKey(), happyFoxCompositeSource.getAuthKey());
            int page = 1;
            int pageCount;
            do {
                Map response;
                if (page == 1) {
                    response = runRestRequestForMap("users/?size=50", client, happyFoxCompositeSource);
                } else {
                    response = runRestRequestForMap("users/?size=50&page=" + page, client, happyFoxCompositeSource);
                }
                Map pageInfo = (Map) response.get("page_info");
                pageCount = (int) pageInfo.get("page_count");
                page++;
                List<Map> data = (List<Map>) response.get("data");
                for (Map ticket : data) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(CONTACT_ID), getJSONValue(ticket, "id"));
                    row.addValue(keys.get(CONTACT_NAME), getJSONValue(ticket, "name"));
                    row.addValue(keys.get(CONTACT_EMAIL), getJSONValue(ticket, "email"));
                    row.addValue(keys.get(CONTACT_COUNT), 1);
                    List<Map> customFields = (List<Map>) ticket.get("custom_fields");
                    for (Map customField : customFields) {
                        String customFieldID = customField.get("id").toString();
                        Object val = customField.get("value");
                        if (val != null) {
                            String customFieldValue = val.toString();
                            if (this.customFields.containsKey(customFieldID)) {
                                row.addValue(keys.get(customFieldID), customFieldValue);
                            }
                        }
                    }
                }
            } while (page <= pageCount);
            return dataSet;
        } catch (ReportException e) {
            throw e;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HAPPYFOX_CONTACTS;
    }
}
