package com.easyinsight.datafeeds.gnip;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.analysis.*;
import com.easyinsight.users.Account;
import com.easyinsight.users.Credentials;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DataStorage;
import com.gnipcentral.client.GnipException;
import com.gnipcentral.client.resource.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Mar 30, 2009
 * Time: 6:05:16 PM
 */
public class GnipDataSource extends ServerDataSourceDefinition {

    public static final int MINUTE_MILLISECONDS = 60 * 1000;
    public static final int REFRESH_TIME = 30 * MINUTE_MILLISECONDS;

    public static final String PUBLISHER = "Publisher";
    public static final String ACTION = "Action";
    public static final String ACTOR_NAME = "Actor";
    public static final String TAGS = "Tags";
    public static final String COUNT = "Count";
    public static final String TIME = "Time";
    public static final String TO = "To";
    public static final String DESTINATIONURL = "Destination";
    public static final String URL = "URL";
    public static final String REGARDINGURL = "Regarding";
    public static final String SOURCE = "Source";
    public static final String LONGITUDE = "Longitude";
    public static final String LATITUDE = "Latitude";
    public static final String TAG_DELIMETER = ",";

    private List<GnipFilter> filters;

    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    public FeedType getFeedType() {
        return FeedType.GNIP;
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.STANDARD_USERNAME_PW;
    }

    public String validateCredentials(com.easyinsight.users.Credentials credentials) {
        String result = null;
        GnipHelper gh = new GnipHelper();
        gh.setUsername(credentials.getUserName());
        gh.setPassword(credentials.getPassword());
        try {
            gh.testConnection();
        } catch (GnipException e) {
            result = e.getMessage();
        }

        return result;
    }

    public DataSet getDataSet(com.easyinsight.users.Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition) {
        DataSet ds = new DataSet();
        List<Exception> exceptions = new LinkedList<Exception>();
        GnipHelper gh = new GnipHelper();
        gh.setUsername(credentials.getUserName());
        gh.setPassword(credentials.getPassword());
        for(GnipFilter filter : filters) {
            for(int i = 0; i < REFRESH_TIME;i += MINUTE_MILLISECONDS) {
                DateTime bucket = new DateTime(now.getTime() - i);
                try {
                    Activities acts = gh.getActivities(filter, bucket);
                    if(acts.getActivities() != null)
                        for(Activity curActivity : acts.getActivities()) {
                            IRow row = ds.createRow();
                            row.addValue(keys.get(PUBLISHER), acts.getPublisherName());
                            row.addValue(keys.get(ACTION), curActivity.getAction());
                            String actors = "";
                            boolean first = true;
                            for(Actor actor : curActivity.getActors()) {
                                if(!first)
                                    actors = actors + TAG_DELIMETER;
                                actors = actors + actor.getValue();
                                first = false;
                            }
                            row.addValue(keys.get(ACTOR_NAME), actors);
                            String tags = "";
                            first = true;
                            if(curActivity.getKeywords() != null)
                                for(String keyword : curActivity.getKeywords()) {
                                    if(!first)
                                        tags = tags + TAG_DELIMETER;
                                    tags = tags + keyword;
                                    first = false;
                                }
                            if(curActivity.getTags() != null)
                                for(GnipValue tag : curActivity.getTags()) {
                                    if(!first)
                                        tags = tags + TAG_DELIMETER;
                                    tags = tags + tag.getValue();
                                    first = false;
                                }
                            if(first == false)
                                row.addValue(keys.get(TAGS), tags);


                            if(curActivity.getTos() != null) {
                                String tos = "";
                                first = true;
                                for(GnipValue to : curActivity.getTos()) {
                                    if(!first)
                                        tos = tos + TAG_DELIMETER;
                                    tos = tos + to.getValue();
                                    first = false;
                                }
                                row.addValue(keys.get(TO), tos);
                            }


                            if(curActivity.getSources() != null) {
                                String sources = "";
                                first = true;
                                for(String s : curActivity.getSources()) {
                                    if(!first)
                                        sources = sources + TAG_DELIMETER;
                                    sources = sources + s;
                                    first = false;
                                }
                                row.addValue(keys.get(SOURCE), sources);
                            }

                            if(curActivity.getUrl() != null)
                                row.addValue(keys.get(URL), curActivity.getUrl());

                            if(curActivity.getPlaces() != null && curActivity.getPlaces().size() > 0) {
                                Place p = curActivity.getPlaces().get(0);
                                if(p.getPoint() != null && p.getPoint().length >= 2) {
                                    row.addValue(keys.get(LONGITUDE), p.getPoint()[0]);
                                    row.addValue(keys.get(LATITUDE), p.getPoint()[1]);                                }
                            }

                            if(curActivity.getRegardingUrls() != null && curActivity.getRegardingUrls().size() > 0)
                                row.addValue(keys.get(REGARDINGURL), curActivity.getRegardingUrls().get(0).getUrl());

                            if(curActivity.getDestinationUrls() != null && curActivity.getDestinationUrls().size() > 0)
                                row.addValue(keys.get(DESTINATIONURL), curActivity.getDestinationUrls().get(0).getUrl());

                            if(curActivity.getAt() != null)
                                row.addValue(keys.get(TIME), curActivity.getAt().toDate());
                            row.addValue(keys.get(COUNT), new NumericValue(1));
                        }
                } catch (GnipException e) {
                    LogClass.error(e);
                    exceptions.add(e);
                }
            }
        }
        if(exceptions.size() > 0)
            throw new RuntimeException(exceptions.get(0));

        
        return ds;

    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(PUBLISHER, ACTION, ACTOR_NAME, TAGS, TIME, URL, DESTINATIONURL,
                TO, REGARDINGURL, SOURCE, LATITUDE, LONGITUDE, COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(PUBLISHER), true));
        analysisItems.add(new AnalysisDimension(keys.get(ACTION), true));
        analysisItems.add(new AnalysisList(keys.get(ACTOR_NAME), true, TAG_DELIMETER));
        analysisItems.add(new AnalysisList(keys.get(TAGS), true, TAG_DELIMETER));
        analysisItems.add(new AnalysisDateDimension(keys.get(TIME), true, AnalysisDateDimension.MINUTE_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(URL), true));
        analysisItems.add(new AnalysisDimension(keys.get(DESTINATIONURL), true));
        analysisItems.add(new AnalysisDimension(keys.get(TO), true));
        analysisItems.add(new AnalysisDimension(keys.get(REGARDINGURL), true));
        analysisItems.add(new AnalysisDimension(keys.get(LONGITUDE), true));
        analysisItems.add(new AnalysisDimension(keys.get(LATITUDE), true));
        analysisItems.add(new AnalysisList(keys.get(SOURCE), true, TAG_DELIMETER));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.COUNT));
        return analysisItems;
    }

    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM GNIP WHERE DATA_FEED_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement gnipInsertStmt = conn.prepareStatement("INSERT INTO GNIP (DATA_FEED_ID, PUBLISHER_ID, PUBLISHER_SCOPE, FILTER_ID) VALUES (?,?,?,?)");
        for(int i = 0;i < filters.size();i++) {
            GnipFilter gf = filters.get(i);
            gnipInsertStmt.setLong(1, getDataFeedID());
            gnipInsertStmt.setString(2, gf.getPublisherName());
            gnipInsertStmt.setString(3, gf.getScope().name());
            gnipInsertStmt.setString(4, gf.getFilterName());
            gnipInsertStmt.execute();
        }
    }

    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT PUBLISHER_ID, PUBLISHER_SCOPE, FILTER_ID FROM GNIP WHERE DATA_FEED_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        filters = new LinkedList<GnipFilter>();
        if (rs.next()) {
            GnipFilter gf = new GnipFilter();
            gf.setPublisherName(rs.getString("PUBLISHER_ID"));
            gf.setScope(PublisherScope.valueOf(rs.getString("PUBLISHER_SCOPE")));
            gf.setFilterName(rs.getString("FILTER_ID"));
            filters.add(gf);
        }
        loadStmt.close();
    }

    public List<GnipFilter> getFilters() {
        if(filters == null)
            filters = new LinkedList<GnipFilter>();
        return filters;
    }

    public void setFilters(List<GnipFilter> filters) {
        this.filters = filters;
    }

    protected void addData(DataStorage dataStorage, DataSet dataSet) throws SQLException {
        dataStorage.insertData(dataSet);
    }

    public boolean isConfigured() {
        return filters != null && filters.size() > 0;
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        GnipDataSource gnipDataSource = (GnipDataSource) super.clone(conn);
        gnipDataSource.setFilters(new ArrayList<GnipFilter>());
        return gnipDataSource;
    }
}