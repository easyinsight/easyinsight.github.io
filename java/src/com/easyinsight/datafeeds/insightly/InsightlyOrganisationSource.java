package com.easyinsight.datafeeds.insightly;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/22/12
 * Time: 7:24 PM
 */
public class InsightlyOrganisationSource extends InsightlyBaseSource {

    public static final String ORGANIZATION_ID = "Organization ID";

    public static final String NAME = "Organization Name";
    public static final String BACKGROUND = "Organization Background";
    public static final String DATE_CREATED = "Organization Date Created";
    public static final String DATE_UPDATED = "Organization Date Updated";
    public static final String ORGANISATION_COUNT = "Organization Count";

    public static final String ORGANISATION_TAGS = "Organization Tags";

    public static final String WORK_EMAIL = "Organization Work Email";

    public static final String WORK_PHONE = "Organization Work Phone";

    public static final String WORK_ADDRESS = "Organization Work Address";
    public static final String WORK_CITY = "Organization Work City";
    public static final String WORK_STATE = "Organization Work State";
    public static final String WORK_COUNTRY = "Organization Work Country";
    public static final String WORK_POSTAL = "Organization Work Postal Code";

    public InsightlyOrganisationSource() {
        setFeedName("Organizations");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ORGANIZATION_ID, NAME,  BACKGROUND, DATE_CREATED, DATE_UPDATED, ORGANISATION_COUNT, ORGANISATION_TAGS,
                WORK_PHONE, WORK_EMAIL, WORK_ADDRESS, WORK_CITY, WORK_COUNTRY, WORK_POSTAL, WORK_STATE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(ORGANIZATION_ID)));
        fields.add(new AnalysisDimension(keys.get(NAME)));
        fields.add(new AnalysisDimension(keys.get(BACKGROUND)));

        Key workEmailKey = keys.get(WORK_EMAIL);
        if (workEmailKey == null) {
            workEmailKey = new NamedKey(WORK_EMAIL);
        }
        fields.add(new AnalysisDimension(workEmailKey));

        Key workPhoneKey = keys.get(WORK_PHONE);
        if (workPhoneKey == null) {
            workPhoneKey = new NamedKey(WORK_PHONE);
        }
        fields.add(new AnalysisDimension(workPhoneKey));

        Key workAddressKey = keys.get(WORK_ADDRESS);
        if (workAddressKey == null) {
            workAddressKey = new NamedKey(WORK_ADDRESS);
        }
        fields.add(new AnalysisDimension(workAddressKey));

        Key workCityKey = keys.get(WORK_CITY);
        if (workCityKey == null) {
            workCityKey = new NamedKey(WORK_CITY);
        }
        fields.add(new AnalysisDimension(workCityKey));

        Key workStateKey = keys.get(WORK_STATE);
        if (workStateKey == null) {
            workStateKey = new NamedKey(WORK_STATE);
        }
        fields.add(new AnalysisDimension(workStateKey));

        Key workCountryKey = keys.get(WORK_COUNTRY);
        if (workCountryKey == null) {
            workCountryKey = new NamedKey(WORK_COUNTRY);
        }
        fields.add(new AnalysisDimension(workCountryKey));

        Key workPostalKey = keys.get(WORK_POSTAL);
        if (workPostalKey == null) {
            workPostalKey = new NamedKey(WORK_POSTAL);
        }
        fields.add(new AnalysisDimension(workPostalKey));

        Key contactTagsKey = keys.get(ORGANISATION_TAGS);
        if (contactTagsKey == null) {
            contactTagsKey = new NamedKey(ORGANISATION_TAGS);
        }
        fields.add(new AnalysisList(contactTagsKey, true, ","));

        InsightlyCompositeSource insightlyCompositeSource = (InsightlyCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(insightlyCompositeSource.getInsightlyApiKey(), "x");
        List customFields = runJSONRequest("customFields", insightlyCompositeSource, httpClient);
        for (Object customFieldObject : customFields) {
            Map customFieldMap = (Map) customFieldObject;
            String fieldFor = customFieldMap.get("FIELD_FOR").toString();
            if ("ORGANISATION".equals(fieldFor)) {
                String customFieldID = customFieldMap.get("CUSTOM_FIELD_ID").toString();
                Key key = keys.get(customFieldID);
                if (key == null) {
                    key = new NamedKey(customFieldID);
                }

                String fieldType = customFieldMap.get("FIELD_TYPE").toString();
                if ("DATE".equals(fieldType)) {
                    fields.add(new AnalysisDateDimension(key, customFieldMap.get("FIELD_NAME").toString(), AnalysisDateDimension.DAY_LEVEL, true));
                } else {
                    fields.add(new AnalysisDimension(key, customFieldMap.get("FIELD_NAME").toString()));
                }
            }

        }
        fields.add(new AnalysisDateDimension(keys.get(DATE_CREATED), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisDateDimension(keys.get(DATE_UPDATED), true, AnalysisDateDimension.DAY_LEVEL));
        fields.add(new AnalysisMeasure(keys.get(ORGANISATION_COUNT), AggregationTypes.SUM));
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
            List contactList = runJSONRequest("organisations", insightlyCompositeSource, httpClient);
            for (Object contactObj : contactList) {
                IRow row = dataSet.createRow();
                Map contactMap = (Map) contactObj;
                row.addValue(keys.get(ORGANIZATION_ID), contactMap.get("ORGANISATION_ID").toString());
                row.addValue(keys.get(NAME), getValue(contactMap, "ORGANISATION_NAME"));
                row.addValue(keys.get(BACKGROUND), getValue(contactMap, "BACKGROUND"));
                for (AnalysisItem field : getFields()) {
                    if (field.getKey().toKeyString().startsWith("ORGANISATION_FIELD")) {
                        if (field.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            Object obj = contactMap.get(field.getKey().toKeyString());
                            if (obj != null) {
                                try {
                                    row.addValue(field.getKey(), sdf.parse(obj.toString()));
                                } catch (ParseException e) {

                                }
                            }
                        } else {
                            row.addValue(field.getKey(), getValue(contactMap, field.getKey().toKeyString()));
                        }
                    }
                }
                row.addValue(keys.get(DATE_CREATED), new DateValue(sdf.parse(contactMap.get("DATE_CREATED_UTC").toString())));
                row.addValue(keys.get(DATE_UPDATED), new DateValue(sdf.parse(contactMap.get("DATE_UPDATED_UTC").toString())));
                row.addValue(keys.get(ORGANISATION_COUNT), 1);

                List contactInfos = (List) contactMap.get("CONTACTINFOS");
                if (contactInfos != null) {
                    for (Object contactInfoObj : contactInfos) {
                        Map contactInfoMap = (Map) contactInfoObj;
                        String type = getValue(contactInfoMap, "TYPE").toString();
                        String label = getValue(contactInfoMap, "LABEL").toString();
                        Value detail = getValue(contactInfoMap, "DETAIL");
                        if ("PHONE".equals(type) && "Work".equals(label)) {
                            row.addValue(keys.get(WORK_PHONE), detail);
                        } else if ("EMAIL".equals(type) && "Work".equals(label)) {
                            row.addValue(keys.get(WORK_EMAIL), detail);
                        }
                    }
                }
                List addresses = (List) contactMap.get("ADDRESSES");
                if (addresses != null) {
                    for (Object mapObject : addresses) {
                        Map map = (Map) mapObject;
                        String addressType = getValue(map, "ADDRESS_TYPE").toString();
                        if ("WORK".equals(addressType)) {
                            row.addValue(WORK_STATE, getValue(map, "STATE"));
                            row.addValue(WORK_COUNTRY, getValue(map, "COUNTRY"));
                            row.addValue(WORK_POSTAL, getValue(map, "POSTCODE"));
                            row.addValue(WORK_ADDRESS, getValue(map, "STREET"));
                            row.addValue(WORK_CITY, getValue(map, "CITY"));
                        }
                    }
                }
                StringBuilder sb = new StringBuilder();
                List contacts = (List) contactMap.get("TAGS");
                if (contacts != null) {
                    for (Object obj : contacts) {
                        Map map = (Map) obj;
                        sb.append(map.get("TAG_NAME").toString()).append(",");
                    }
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                row.addValue(keys.get(ORGANISATION_TAGS), sb.toString());
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_ORGANIZATIONS;
    }
}
