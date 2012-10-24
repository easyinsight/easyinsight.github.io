package com.easyinsight.datafeeds.insightly;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/22/12
 * Time: 7:24 PM
 */
public class InsightlyContactSource extends InsightlyBaseSource {

    public static final String CONTACT_ID = "Contact ID";
    public static final String FIRST_NAME = "Contact First Name";
    public static final String LAST_NAME = "Contact Last Name";
    public static final String FULL_NAME = "Contact Full Name";
    public static final String BACKGROUND = "Contact Background";
    public static final String DATE_CREATED = "Contact Date Created";
    public static final String DATE_UPDATED = "Contact Date Updated";
    public static final String CONTACT_COUNT = "Contact Count";
    public static final String ORGANIZATION_ID = "Contact Organization ID";

    public InsightlyContactSource() {
        setFeedName("Contacts");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CONTACT_ID, FIRST_NAME, LAST_NAME, FULL_NAME,  BACKGROUND, DATE_CREATED, DATE_UPDATED, CONTACT_COUNT, ORGANIZATION_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(CONTACT_ID)));
        fields.add(new AnalysisDimension(keys.get(FIRST_NAME)));
        fields.add(new AnalysisDimension(keys.get(LAST_NAME)));
        fields.add(new AnalysisDimension(keys.get(FULL_NAME)));
        fields.add(new AnalysisDimension(keys.get(BACKGROUND)));
        InsightlyCompositeSource insightlyCompositeSource = (InsightlyCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(insightlyCompositeSource.getInsightlyApiKey(), "x");
        List customFields = runJSONRequest("customFields", insightlyCompositeSource, httpClient);
        for (Object customFieldObject : customFields) {
            Map customFieldMap = (Map) customFieldObject;
            String fieldFor = customFieldMap.get("FIELD_FOR").toString();
            if ("CONTACT".equals(fieldFor)) {
                String customFieldID = customFieldMap.get("CUSTOM_FIELD_ID").toString();
                Key key = keys.get(customFieldID);
                if (key == null) {
                    key = new NamedKey(customFieldID);
                }

                String fieldType = customFieldMap.get("FIELD_TYPE").toString();
                if ("DATE".equals(fieldType)) {
                    fields.add(new AnalysisDateDimension(key, customFieldMap.get("FIELD_NAME").toString(), AnalysisDateDimension.DAY_LEVEL));
                } else {
                    fields.add(new AnalysisDimension(key, customFieldMap.get("FIELD_NAME").toString()));
                }
            }

        }
        fields.add(new AnalysisDimension(keys.get(ORGANIZATION_ID)));
        fields.add(new AnalysisDateDimension(keys.get(DATE_CREATED), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisDateDimension(keys.get(DATE_UPDATED), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisMeasure(keys.get(CONTACT_COUNT), AggregationTypes.SUM));
        return fields;
    }

    private Value getValue(Map map, String param) {
        Object obj = map.get(param);
        if (obj == null) {
            return new EmptyValue();
        } else {
            return new StringValue(obj.toString());
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DataSet dataSet = new DataSet();
            InsightlyCompositeSource insightlyCompositeSource = (InsightlyCompositeSource) parentDefinition;
            HttpClient httpClient = getHttpClient(insightlyCompositeSource.getInsightlyApiKey(), "x");
            List contactList = runJSONRequest("contacts", insightlyCompositeSource, httpClient);
            for (Object contactObj : contactList) {
                IRow row = dataSet.createRow();
                Map contactMap = (Map) contactObj;
                row.addValue(keys.get(CONTACT_ID), contactMap.get("CONTACT_ID").toString());
                String firstName = getValue(contactMap, "FIRST_NAME").toString();
                String lastName = getValue(contactMap, "LAST_NAME").toString();
                row.addValue(keys.get(FIRST_NAME), firstName);
                row.addValue(keys.get(LAST_NAME), lastName);
                row.addValue(keys.get(FULL_NAME), firstName + " " + lastName);
                row.addValue(keys.get(BACKGROUND), getValue(contactMap, "BACKGROUND"));
                for (AnalysisItem field : getFields()) {
                    if (field.getKey().toKeyString().startsWith("CONTACT_FIELD")) {
                        if (field.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            Object obj = contactMap.get(field.getKey().toKeyString());
                            if (obj != null) {
                                row.addValue(field.getKey(), sdf.parse(obj.toString()));
                            }
                        } else {
                            row.addValue(field.getKey(), getValue(contactMap, field.getKey().toKeyString()));
                        }
                    }
                }
                row.addValue(keys.get(DATE_CREATED), new DateValue(sdf.parse(contactMap.get("DATE_CREATED_UTC").toString())));
                row.addValue(keys.get(DATE_UPDATED), new DateValue(sdf.parse(contactMap.get("DATE_UPDATED_UTC").toString())));
                row.addValue(keys.get(CONTACT_COUNT), 1);
                row.addValue(keys.get(ORGANIZATION_ID), getValue(contactMap, "DEFAULT_LINKED_ORGANISATION"));
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_CONTACTS;
    }
}
