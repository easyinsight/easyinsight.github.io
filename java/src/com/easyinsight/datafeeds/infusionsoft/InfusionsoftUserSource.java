package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.xmlrpc.XmlRpcException;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftUserSource extends InfusionsoftTableSource {

    public static final String USER_ID = "Id";
    public static final String EMAIL = "Email";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String NAME = "Name";

    public InfusionsoftUserSource() {
        setFeedName("Users");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_USERS;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(USER_ID, new AnalysisDimension());
        fieldBuilder.addField(EMAIL, new AnalysisDimension());
        fieldBuilder.addField(FIRST_NAME, new AnalysisDimension());
        fieldBuilder.addField(LAST_NAME, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
    }

    public List<AnalysisItem> createTeamItems() {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(new NamedKey("Id")));
        analysisitems.add(new AnalysisDimension(new NamedKey("Name")));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = query("User", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition,
                    Arrays.asList(NAME));
            Map<String, String> map = new HashMap<>();
            for (IRow row : dataSet.getRows()) {
                String userID = row.getValue(new NamedKey(InfusionsoftUserSource.USER_ID)).toString();
                String firstName = row.getValue(new NamedKey(InfusionsoftUserSource.FIRST_NAME)).toString();
                String lastName = row.getValue(new NamedKey(InfusionsoftUserSource.LAST_NAME)).toString();
                String name = firstName + " " + lastName;
                map.put(userID, name);
            }
            ((InfusionsoftCompositeSource) parentDefinition).setUserCache(map);
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<InfusionsoftUser> getUsers(InfusionsoftCompositeSource parent) throws MalformedURLException, XmlRpcException {
        AnalysisItem userID = new AnalysisDimension(new NamedKey(USER_ID));
        AnalysisItem email = new AnalysisDimension(new NamedKey(EMAIL));
        DataSet savedFilters = query("User", Arrays.asList(userID, email), parent);
        List<InfusionsoftUser> reports = new ArrayList<>();
        for (IRow row : savedFilters.getRows()) {
            String userIDValue = row.getValue(userID).toString();
            String emailValue = row.getValue(email).toString();
            reports.add(new InfusionsoftUser(userIDValue, emailValue));
        }
        return reports;
    }
}
