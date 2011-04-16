package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: 3/28/11
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class HarvestUserSource extends HarvestBaseSource {
    public static final String USER_ID = "User ID";
    public static final String EMAIL = "User Email";
    public static final String FIRST_NAME = "User First Name";
    public static final String LAST_NAME = "User Last Name";
    public static final String FUTURE_PROJECTS_ACCESS = "User Default Access to Future Projects";
    public static final String DEFAULT_HOURLY_RATE = "User Default Hourly Rate";
    public static final String IS_ACTIVE = "User Is Active";
    public static final String IS_ADMIN = "User Is Admin";
    public static final String IS_CONTRACTOR = "User Is Contractor";
    public static final String TELEPHONE = "User Telephone #";
    public static final String DEPARTMENT = "User Department";
    public static final String TIME_ZONE = "User Time Zone";
    public static final String COUNT = "User Count";
    private static final String FULL_NAME = "User Full Name";

    public HarvestUserSource() {
        setFeedName("Users");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_USERS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(USER_ID, EMAIL, FIRST_NAME, LAST_NAME, FULL_NAME,
                DEFAULT_HOURLY_RATE, IS_ACTIVE, IS_ADMIN,
                IS_CONTRACTOR, TELEPHONE, DEPARTMENT, TIME_ZONE, COUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisItem userDimension = new AnalysisDimension(keys.get(USER_ID), true);
        userDimension.setHidden(true);
        analysisItems.add(userDimension);
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL), true));
        analysisItems.add(new AnalysisDimension(keys.get(FIRST_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(LAST_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(FULL_NAME), true));
        analysisItems.add(new AnalysisMeasure(keys.get(DEFAULT_HOURLY_RATE), DEFAULT_HOURLY_RATE, AggregationTypes.AVERAGE, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisDimension(keys.get(IS_ACTIVE), true));
        analysisItems.add(new AnalysisDimension(keys.get(IS_ADMIN), true));
        analysisItems.add(new AnalysisDimension(keys.get(IS_CONTRACTOR), true));
        analysisItems.add(new AnalysisDimension(keys.get(TELEPHONE), true));
        analysisItems.add(new AnalysisDimension(keys.get(DEPARTMENT), true));
        analysisItems.add(new AnalysisDimension(keys.get(TIME_ZONE), true));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet ds = new DataSet();
        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        try {
            Document users = runRestRequest("/people", client, builder, source.getUrl(), true, parentDefinition, false);
            Nodes userNodes = users.query("/users/user");
            for(int i = 0;i < userNodes.size();i++) {
                Node user = userNodes.get(i);
                String userId = queryField(user, "id/text()");
                String email = queryField(user, "email/text()");
                String firstName = queryField(user, "first-name/text()");
                String lastName = queryField(user, "last-name/text()");
                String hourlyRate = queryField(user, "default-hourly-rate/text()");
                String isActive = queryField(user, "is-active/text()");
                String isAdmin = queryField(user, "is-admin/text()");
                String isContractor = queryField(user, "is-contractor/text()");
                String telephone = queryField(user, "telephone/text()");
                String department = queryField(user, "department/text()");
                String timeZone = queryField(user, "timezone/text()");
                String fullName = firstName + " " + lastName;
                IRow row = ds.createRow();
                row.addValue(keys.get(USER_ID), userId);
                row.addValue(keys.get(EMAIL), email);
                row.addValue(keys.get(FIRST_NAME), firstName);
                row.addValue(keys.get(LAST_NAME), lastName);
                row.addValue(keys.get(FULL_NAME), fullName);
                if(hourlyRate != null && hourlyRate.length() > 0)
                    row.addValue(keys.get(DEFAULT_HOURLY_RATE), Double.parseDouble(hourlyRate));
                row.addValue(keys.get(IS_ACTIVE), isActive);
                row.addValue(keys.get(IS_ADMIN), isAdmin);
                row.addValue(keys.get(IS_CONTRACTOR), isContractor);
                row.addValue(keys.get(TELEPHONE), telephone);
                row.addValue(keys.get(DEPARTMENT), department);
                row.addValue(keys.get(TIME_ZONE), timeZone);
            }
        } catch (ParsingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return ds;
    }
}
