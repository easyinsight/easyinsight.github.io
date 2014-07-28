package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 7/16/12
 * Time: 10:12 AM
 */
public class Batchbook2DealSource extends Batchbook2BaseSource {

    public static final String ID = "Deal ID";
    public static final String DESCRIPTION = "Deal Description";
    public static final String AMOUNT = "Deal Amount";
    public static final String ARCHIVED_AT = "Deal Archived At";
    public static final String ARCHIVED = "Deal Archived";
    public static final String STATUS = "Deal Status";
    public static final String CREATED_AT = "Deal Created At";
    public static final String UPDATED_AT = "Deal Updated At";
    public static final String URL = "Deal URL";
    public static final String DEAL_COMPANY_ID = "Deal Company ID";
    public static final String DEAL_PERSON_ID = "Deal Person ID";
    public static final String EXPECTED_CLOSE = "Expected Close Date"; // date
    public static final String STAGE = "Deal Stage";
    public static final String OWNER = "Deal Owner";
    public static final String CREATED_BY = "Deal Created By";
    public static final String COUNT = "Deal Count";
    public static final String TITLE = "Deal Title";



    public Batchbook2DealSource() {
        setFeedName("Deals");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID);
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(TITLE, new AnalysisDimension());
        fieldBuilder.addField(DESCRIPTION, new AnalysisDimension());
        fieldBuilder.addField(STATUS, new AnalysisDimension());
        fieldBuilder.addField(ARCHIVED, new AnalysisDimension());
        fieldBuilder.addField(URL, new AnalysisDimension());
        fieldBuilder.addField(STAGE, new AnalysisDimension());
        fieldBuilder.addField(DEAL_COMPANY_ID, new AnalysisDimension());
        fieldBuilder.addField(DEAL_PERSON_ID, new AnalysisDimension());
        fieldBuilder.addField(OWNER, new AnalysisDimension());
        fieldBuilder.addField(CREATED_BY, new AnalysisDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(ARCHIVED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(EXPECTED_CLOSE, new AnalysisDateDimension(true));
        fieldBuilder.addField(AMOUNT, new AnalysisMeasure());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_DEALS;
    }

    protected Date getDate(Map map, String value, SimpleDateFormat dateFormat) {
        Object obj = map.get(value);
        if (obj == null) {
            return null;
        }
        String string = obj.toString();
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            LogClass.error(e);
            return null;
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Batchbook2CompositeSource batchbook2CompositeSource = (Batchbook2CompositeSource) parentDefinition;

            HttpClient httpClient = Batchbook2BaseSource.getHttpClient(batchbook2CompositeSource.getToken(), "");
            BatchbookCache cache = batchbook2CompositeSource.getOrCreateCache(httpClient);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Map<String, String> stageMap = new HashMap<String, String>();
            Map stages = runV2RestRequest("/stages.json", httpClient, batchbook2CompositeSource);
            List<Map> stageList = (List<Map>) stages.get("stages");
            for (Map stage : stageList) {
                String stageID = getJSONValue(stage, "id");
                String title = getJSONValue(stage, "title");
                stageMap.put(stageID, title);
            }
            Map<String, String> userMap = new HashMap<String, String>();
            Map users = runV2RestRequest("/users.json", httpClient, batchbook2CompositeSource);
            List<Map> contactList = (List<Map>) users.get("contacts");
            for (Map contact : contactList) {
                String userID = getJSONValue(contact, "id");
                String displayName = getJSONValue(contact, "display_name");
                userMap.put(userID, displayName);
            }
            DataSet dataSet = new DataSet();

            Map result;

            String nextPage = "1";
            do {
                result = runV2RestRequest("/deals.json?page=" + nextPage, httpClient, batchbook2CompositeSource);
                List<Map> deals = (List<Map>) result.get("deals");
                for (Map dealMap : deals) {
                    IRow row = dataSet.createRow();
                    String id = getJSONValue(dealMap, "id");
                    row.addValue(keys.get(ID), id);
                    row.addValue(keys.get(DESCRIPTION), getJSONValue(dealMap, "description"));
                    row.addValue(keys.get(STATUS), getJSONValue(dealMap, "status"));
                    String stageID = getJSONValue(dealMap, "stage_id");
                    String stage = stageMap.get(stageID);
                    row.addValue(keys.get(STAGE), stage);
                    row.addValue(keys.get(TITLE), getJSONValue(dealMap, "title"));
                    String ownedBy = getJSONValue(dealMap, "assigned_to_id");
                    row.addValue(keys.get(OWNER), userMap.get(ownedBy));
                    String creatorID = getJSONValue(dealMap, "creator_id");
                    row.addValue(keys.get(CREATED_BY), userMap.get(creatorID));
                    row.addValue(keys.get(CREATED_AT), getDate(dealMap, "created_at", dateTimeFormat));
                    row.addValue(keys.get(UPDATED_AT), getDate(dealMap, "updated_at", dateTimeFormat));
                    row.addValue(keys.get(ARCHIVED_AT), getDate(dealMap, "archived_at", dateTimeFormat));
                    row.addValue(keys.get(EXPECTED_CLOSE), getDate(dealMap, "date", dateFormat));
                    row.addValue(keys.get(COUNT), 1);
                    row.addValue(keys.get(AMOUNT), getJSONValue(dealMap, "amount"));
                    row.addValue(keys.get(ARCHIVED), getJSONValue(dealMap, "archived"));
                    row.addValue(keys.get(URL), batchbook2CompositeSource.getUrl() + "/filter-deals/deals/deal-" + id);
                    String dealWith = getJSONValue(dealMap, "deal_with_id");
                    row.addValue(keys.get(DEAL_COMPANY_ID), dealWith);
                }
                Object o = ((Map) result.get("meta")).get("next_page");
                if (o != null) {
                    nextPage = o.toString();
                } else {
                    nextPage = null;
                }
            } while (nextPage != null);

            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
