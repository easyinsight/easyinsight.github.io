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
 * Date: 3/22/11
 * Time: 1:30 PM
 */
public class ZendeskGroupToUserJoinSource extends ZendeskBaseSource {

    public static final String GROUP_ID = "Join Group ID";
    public static final String USER_ID = "Join User ID";

    public ZendeskGroupToUserJoinSource() {
        setFeedName("Group to User Join");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(GROUP_ID, USER_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(GROUP_ID)));
        items.add(new AnalysisDimension(keys.get(USER_ID)));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.ZENDESK_GROUP_TO_USER;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            ZendeskCompositeSource zendeskCompositeSource = (ZendeskCompositeSource) parentDefinition;
            DataSet dataSet = new DataSet();
            HttpClient httpClient = getHttpClient(zendeskCompositeSource.getZdUserName(), zendeskCompositeSource.getZdPassword());
            Builder builder = new Builder();
            Document doc = runRestRequest(zendeskCompositeSource, httpClient, "/groups.xml", builder);
            Nodes groupNodes = doc.query("/groups/group");
            for (int i = 0; i < groupNodes.size(); i++) {
                Node groupNode = groupNodes.get(i);
                String groupID = queryField(groupNode, "id/text()");
                Nodes userNodes = groupNode.query("users/user");
                for (int j = 0; j < userNodes.size(); j++) {
                    Node userNode = userNodes.get(j);
                    String userID = queryField(userNode, "id/text()");
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(GROUP_ID), groupID);
                    row.addValue(keys.get(USER_ID), userID);
                }
            }
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
