package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
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
 * Time: 6:38 PM
 */
public class ZendeskOrganizationSource extends ZendeskBaseSource {
    public static final String NAME = "Organization Name";
    public static final String ID = "Organization ID";

    public ZendeskOrganizationSource() {
        setFeedName("Organizations");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(NAME, ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(NAME)));
        items.add(new AnalysisDimension(keys.get(ID)));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.ZENDESK_ORGANIZATION;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            ZendeskCompositeSource zendeskCompositeSource = (ZendeskCompositeSource) parentDefinition;
            DataSet dataSet = new DataSet();
            HttpClient httpClient = getHttpClient(zendeskCompositeSource.getZdUserName(), zendeskCompositeSource.getZdPassword());
            Builder builder = new Builder();
            Document doc = runRestRequest(zendeskCompositeSource, httpClient, "/organizations.xml", builder);
            Nodes organizationNodes = doc.query("/organizations/organization");
            for (int i = 0; i < organizationNodes.size(); i++) {
                Node organizationNode = organizationNodes.get(i);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(NAME), queryField(organizationNode, "name/text()"));
                row.addValue(keys.get(ID), queryField(organizationNode, "id/text()"));
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
