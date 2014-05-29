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
public class Batchbook2CommunicationsSource extends Batchbook2BaseSource {

    public static final String ID = "Communication ID";
    public static final String TITLE = "Communication Title";
    public static final String TYPE = "Communication Type";
    public static final String BODY = "Communication Body";
    public static final String CREATED_AT = "Communication Created At";
    public static final String UPDATED_AT = "Communication Updated At";
    public static final String COUNT = "Communication Count";
    public static final String FROM_NAME = "Communication From Name";
    public static final String FROM_ID = "Communication From ID";
    public static final String TO_NAME = "Communication To Name";
    public static final String TO_ID = "Communication To ID";



    public Batchbook2CommunicationsSource() {
        setFeedName("Communications");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID);
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(TYPE, new AnalysisDimension());
        fieldBuilder.addField(CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(UPDATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(BODY, new AnalysisText());
        fieldBuilder.addField(TITLE, new AnalysisDimension());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
        fieldBuilder.addField(FROM_NAME, new AnalysisDimension());
        fieldBuilder.addField(FROM_ID, new AnalysisDimension());
        fieldBuilder.addField(TO_NAME, new AnalysisDimension());
        fieldBuilder.addField(TO_ID, new AnalysisDimension());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_COMMUNICATIONS;
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
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

            DataSet dataSet = new DataSet();

            Map result;

            String nextPage = "1";
            do {
                result = runRestRequest("/communications.json?page=" + nextPage, httpClient, batchbook2CompositeSource);
                List<Map> deals = (List<Map>) result.get("communications");
                for (Map dealMap : deals) {
                    IRow row = dataSet.createRow();
                    String id = getJSONValue(dealMap, "id");
                    row.addValue(keys.get(ID), id);
                    row.addValue(keys.get(TYPE), getJSONValue(dealMap, "type"));
                    row.addValue(keys.get(BODY), getJSONValue(dealMap, "body"));
                    row.addValue(keys.get(TITLE), getJSONValue(dealMap, "title"));
                    row.addValue(keys.get(CREATED_AT), getDate(dealMap, "created_at", dateTimeFormat));
                    row.addValue(keys.get(UPDATED_AT), getDate(dealMap, "updated_at", dateTimeFormat));
                    row.addValue(keys.get(COUNT), 1);
                    List<Map> participants = (List<Map>) dealMap.get("participants");
                    if (participants != null) {
                        for (Map participant : participants) {
                            String pType = getJSONValue(participant, "type");
                            String pID = getJSONValue(participant, "contact_id");
                            String pName = getJSONValue(participant, "contact_name");
                            if ("from".equals(pType)) {
                                row.addValue(keys.get(FROM_NAME), pName);
                                row.addValue(keys.get(FROM_ID), pID);
                            } else if ("to".equals(pType)) {
                                row.addValue(keys.get(TO_NAME), pName);
                                row.addValue(keys.get(TO_ID), pID);
                            }
                        }
                    }
                }
                Object o = result.get("next_page");
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
