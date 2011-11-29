package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 6:39 PM
 */
public class ZendeskUserSource extends ZendeskBaseSource {
    public static final String NAME = "User Name";
    public static final String ID = "User ID";
    public static final String EMAIL = "Email Address";
    public static final String ORGANIZATION_ID = "User Organization ID";
    public static final String ROLE = "Role";
    public static final String COUNT = "User Role";

    public ZendeskUserSource() {
        setFeedName("Users");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(NAME, ID, EMAIL, ORGANIZATION_ID, ROLE, COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(NAME)));
        items.add(new AnalysisDimension(keys.get(ID)));
        items.add(new AnalysisDimension(keys.get(EMAIL)));
        items.add(new AnalysisDimension(keys.get(ORGANIZATION_ID)));
        items.add(new AnalysisDimension(keys.get(ROLE)));
        items.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.ZENDESK_USER;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            ZendeskCompositeSource zendeskCompositeSource = (ZendeskCompositeSource) parentDefinition;

            DataSet dataSet = new DataSet();
            HttpClient httpClient = getHttpClient(zendeskCompositeSource.getZdUserName(), zendeskCompositeSource.getZdPassword());
            ZendeskUserCache zendeskUserCache = zendeskCompositeSource.getOrCreateUserCache(httpClient);
            for (ZendeskUser zendeskUser : zendeskUserCache.getUsers().values()) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(NAME), zendeskUser.getName());
                row.addValue(keys.get(ID), zendeskUser.getId());
                row.addValue(keys.get(ORGANIZATION_ID), zendeskUser.getOrganizationID());
                row.addValue(keys.get(ROLE), zendeskUser.getRole());
                row.addValue(keys.get(EMAIL), zendeskUser.getEmail());
                row.addValue(keys.get(COUNT), 1);
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
