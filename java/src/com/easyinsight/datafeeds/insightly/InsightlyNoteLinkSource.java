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
 * Date: 2/10/14
 * Time: 7:27 AM
 */
public class InsightlyNoteLinkSource extends InsightlyBaseSource {

    public static final String NOTE_LINK_ID = "Note Link ID";
    public static final String NOTE_ID = "Note ID";
    public static final String CONTACT_ID = "Contact ID";
    public static final String ORGANIZATION_ID = "Organization ID";
    public static final String OPPORTUNITY_ID = "Opportunity ID";
    public static final String PROJECT_ID = "Project ID";

    public InsightlyNoteLinkSource() {
        setFeedName("Note Links");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(NOTE_ID, NOTE_LINK_ID, CONTACT_ID, ORGANIZATION_ID, OPPORTUNITY_ID, PROJECT_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {

        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(NOTE_ID)));
        fields.add(new AnalysisDimension(keys.get(NOTE_LINK_ID)));
        fields.add(new AnalysisDimension(keys.get(CONTACT_ID)));
        fields.add(new AnalysisDimension(keys.get(ORGANIZATION_ID)));
        fields.add(new AnalysisDimension(keys.get(OPPORTUNITY_ID)));
        fields.add(new AnalysisDimension(keys.get(PROJECT_ID)));
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
            int skip = 0;
            int returned;
            do {
                returned = 0;
                List contactList = runJSONRequest("notes?$top=9999&skip=" + skip, insightlyCompositeSource, httpClient);
                for (Object contactObj : contactList) {
                    returned++;
                    skip++;
                    Map contactMap = (Map) contactObj;
                    List<Map> noteLinks = (List<Map>) contactMap.get("NOTELINKS");
                    for (Map noteLink : noteLinks) {
                        IRow row = dataSet.createRow();

                        row.addValue(keys.get(NOTE_ID), getValue(noteLink, "NOTE_ID"));
                        row.addValue(keys.get(NOTE_LINK_ID), getValue(noteLink, "NOTE_LINK_ID"));
                        row.addValue(keys.get(CONTACT_ID), getValue(noteLink, "CONTACT_ID"));
                        row.addValue(keys.get(ORGANIZATION_ID), getValue(noteLink, "ORGANISATION_ID"));
                        row.addValue(keys.get(OPPORTUNITY_ID), getValue(noteLink, "OPPORTUNITY_ID"));
                        row.addValue(keys.get(PROJECT_ID), getValue(noteLink, "PROJECT_ID"));
                    }
                }
            } while (returned == 999);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_NOTE_LINKS;
    }
}
