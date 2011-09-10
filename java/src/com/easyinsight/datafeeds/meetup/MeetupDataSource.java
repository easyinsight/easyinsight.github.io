package com.easyinsight.datafeeds.meetup;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: May 6, 2010
 * Time: 5:17:49 PM
 */
public class MeetupDataSource extends ServerDataSourceDefinition {

    public static final String STATE = "State";
    public static final String JOINED = "Joined On";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String NAME = "Name";
    public static final String ID = "ID";
    public static final String VISITED = "Visited On";
    public static final String CITY = "City";
    public static final String LINK = "Link";
    public static final String COUNT = "Count";

    private String meetupAPIKey = "2558613c7962106a1d265554cf4e17";

    public MeetupDataSource() {
        setFeedName("Meetup");
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PERSONAL;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        DataSet dataSet = new DataSet();
        try {
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod("http://api.meetup.com/members.xml/?group_urlname=bdnewtech&key=" + meetupAPIKey);
            boolean done = false;
            while (!done) {
                httpClient.executeMethod(getMethod);
                Builder builder = new Builder();
                Document doc = builder.build(getMethod.getResponseBodyAsStream());

                Nodes items = doc.query("/results/items/item");
                if (items.size() == 0) {
                    done = true;
                } else{
                    for (int i = 0; i < items.size(); i++) {
                        IRow row = dataSet.createRow();
                        Node person = items.get(i);
                        String state = queryField(person, "state/text()");
                        String joined = queryField(person, "joined/text()");
                        String latitude = queryField(person, "lat/text()");
                        String longitude = queryField(person, "lon/text()");
                        String name = queryField(person, "name/text()");
                        String id = queryField(person, "id/text()");
                        String visited = queryField(person, "visited/text()");
                        String city = queryField(person, "city/text()");
                        String link = queryField(person, "link/text()");
                        row.addValue(keys.get(STATE), state);
                        row.addValue(keys.get(LATITUDE), latitude);
                        row.addValue(keys.get(LONGITUDE), longitude);
                        row.addValue(keys.get(NAME), name);
                        row.addValue(keys.get(ID), id);
                        row.addValue(keys.get(COUNT), 1);
                        row.addValue(keys.get(LINK), link);
                        row.addValue(keys.get(CITY), city);
                        //System.out.println(state + " - " + joined + " - " + latitude + " - " + longitude + " - " + name + " - " + id + " - " + visited + " - " + city + " - " + link);
                    }
                }
                if (!done) {
                    Nodes nextNodes = doc.query("/results/head/next/text()");
                    if (nextNodes.size() == 0) {
                        done = true;
                    } else {
                        String nextURL = nextNodes.get(0).getValue();
                        System.out.println(nextURL);
                        getMethod = new GetMethod(nextURL);
                    }
                }
                if (IDataStorage != null) {
                    IDataStorage.insertData(dataSet);
                    dataSet = new DataSet();
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        if (IDataStorage == null) {
            return dataSet;
        } else {
            return null;
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.MEETUP;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(STATE), true));
        items.add(new AnalysisDimension(keys.get(NAME), true));
        items.add(new AnalysisDimension(keys.get(ID), true));
        items.add(new AnalysisDimension(keys.get(CITY), true));
        
        items.add(new AnalysisDateDimension(keys.get(JOINED), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(VISITED), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisLatitude(keys.get(LATITUDE), true, "Latitude"));
        items.add(new AnalysisLatitude(keys.get(LONGITUDE), true, "Longitude"));
        items.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        items.add(new AnalysisDimension(keys.get(LINK), true));
        return items;
    }

    protected static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(STATE, JOINED, LATITUDE, LONGITUDE, NAME, ID, VISITED, CITY, LINK, COUNT);
    }
}
