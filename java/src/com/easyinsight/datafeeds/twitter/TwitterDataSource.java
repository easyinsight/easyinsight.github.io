package com.easyinsight.datafeeds.twitter;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.*;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.users.Credentials;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 9:36:57 PM
 */
public class TwitterDataSource extends ServerDataSourceDefinition {

    public static final String TWEET_ID = "Tweet ID";
    public static final String PUBLISHED = "Published Time";
    public static final String STATUS_LINK = "Link";
    public static final String TITLE = "Title";
    public static final String CONTENT = "Content";
    public static final String UPDATED = "Updated";
    public static final String IMAGE_LOCATION = "Image Location";
    public static final String SOURCE = "Source";
    public static final String LANGUAGE = "Language";
    public static final String AUTHOR_NAME = "Author";
    public static final String AUTHOR_URL = "Author URL";
    public static final String COUNT = "Count";

    private ArrayList<String> searches = new ArrayList<String>();

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.ADMINISTRATOR;
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.NO_CREDENTIALS;
    }

    public FeedType getFeedType() {
        return FeedType.TWITTER;
    }

    @Override
    public Feed createFeedObject() {
        return new TwitterFeed(searches);

    }


    public ArrayList<String> getSearches() {
        return searches;
    }

    public void setSearches(ArrayList<String> searches) {
        this.searches = searches;
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        DataSet ds = new DataSet();
        return ds;
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(TWEET_ID, PUBLISHED, STATUS_LINK, TITLE, CONTENT, UPDATED, IMAGE_LOCATION, SOURCE,
                LANGUAGE, AUTHOR_NAME, AUTHOR_URL, COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(TWEET_ID), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(PUBLISHED), true, AnalysisDateDimension.MINUTE_LEVEL));


        analysisItems.add(createURLDimension(keys, STATUS_LINK));

        analysisItems.add(new AnalysisDimension(keys.get(TITLE), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTENT), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(UPDATED), true, AnalysisDateDimension.MINUTE_LEVEL));
        analysisItems.add(createURLDimension(keys, IMAGE_LOCATION));
        analysisItems.add(new AnalysisDimension(keys.get(SOURCE), true));
        analysisItems.add(new AnalysisDimension(keys.get(LANGUAGE), true));
        analysisItems.add(new AnalysisDimension(keys.get(AUTHOR_NAME), true));
        analysisItems.add(createURLDimension(keys, AUTHOR_URL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    private AnalysisDimension createURLDimension(Map<String, Key> keys, String fieldName) {
        AnalysisDimension dim = new AnalysisDimension(keys.get(fieldName), true);
        List<Link> links = new ArrayList<Link>();
        URLLink url = new URLLink();
        url.setLabel(fieldName);
        url.setUrl("[" + fieldName + "]");
        links.add(url);
        dim.setLinks(links);
        return dim;
    }

    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM TWITTER WHERE DATA_FEED_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement twitterStmt = conn.prepareStatement("INSERT INTO TWITTER (DATA_FEED_ID, SEARCH) VALUES (?, ?)");
        twitterStmt.setLong(1, getDataFeedID());
        for(String s : getSearches()) {
            twitterStmt.setString(2, s);
            twitterStmt.execute();
        }
    }

    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT SEARCH FROM TWITTER WHERE DATA_FEED_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        while (rs.next()) {
            searches.add(rs.getString(1));
        }
    }
}