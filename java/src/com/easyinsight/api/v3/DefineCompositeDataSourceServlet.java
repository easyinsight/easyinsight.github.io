package com.easyinsight.api.v3;


import com.easyinsight.analysis.*;
import com.easyinsight.api.ServiceRuntimeException;
import com.easyinsight.core.Key;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.util.RandomTextGenerator;
import nu.xom.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 1:35 PM
 */
public class DefineCompositeDataSourceServlet extends APIServlet {

    @Override
    protected ResponseInfo processXML(Document document, EIConnection conn) throws Exception {

        DataStorage dataStorage = null;
        try {

            Nodes dataSourceNameNodes = document.query("/defineCompositeDataSource/dataSourceName/text()");
            String dataSourceName;
            if (dataSourceNameNodes.size() == 0) {
                return new ResponseInfo(ResponseInfo.BAD_REQUEST, "<message>You need to specify a data source name.</message>");
            } else {
                dataSourceName = dataSourceNameNodes.get(0).getValue();
            }
            Map<Long, Boolean> dataSourceMap = findDataSourceIDsByName(dataSourceName, conn);
            CompositeFeedDefinition compositeFeedDefinition;
            if (dataSourceMap.size() == 0) {
                compositeFeedDefinition = new CompositeFeedDefinition();
                compositeFeedDefinition.setFeedName(dataSourceName);
                compositeFeedDefinition.setUploadPolicy(new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID()));
                compositeFeedDefinition.setApiKey(RandomTextGenerator.generateText(12));
            } else if (dataSourceMap.size() == 1) {
                compositeFeedDefinition = (CompositeFeedDefinition) new FeedStorage().getFeedDefinitionData(dataSourceMap.keySet().iterator().next(), conn);
            } else {
                throw new ServiceRuntimeException("More than one data source was found by that name.");
            }

            Nodes dataSources = document.query("/defineDataSource/dataSources/dataSource");
            Map<String, CompositeFeedNode> compositeNodes = new HashMap<String, CompositeFeedNode>();
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED WHERE " +
                    "DATA_FEED.API_KEY = ? OR DATA_FEED.FEED_NAME = ?");
            List<CompositeFeedConnection> compositeConnections = new ArrayList<CompositeFeedConnection>();
            for (int i = 0; i < dataSources.size(); i++) {
                Node dataSourceNode = dataSources.get(i);
                String dataSource = dataSourceNode.getValue();
                queryStmt.setString(1, dataSource);
                queryStmt.setString(2, dataSource);
                ResultSet dataSetRS = queryStmt.executeQuery();
                if (dataSetRS.next()) {
                    long dataSourceID = dataSetRS.getLong(1);
                    compositeNodes.put(dataSource, new CompositeFeedNode(dataSourceID, 0, 0));
                } else {
                    throw new ServiceRuntimeException("We couldn't find a data source with the key of " + dataSource + ".");
                }
            }

            Nodes connectionNodes = document.query("/defineDataSource/connections");

            for (int i = 0; i < connectionNodes.size(); i++) {
                Node connectionNode = connectionNodes.get(i);
                String sourceDataSource;
                String targetDataSource;
                String sourceDataSourceField;
                String targetDataSourceField;
                Nodes sourceDateSourceNodes = connectionNode.query("sourceDataSource/text()");
                if (sourceDateSourceNodes.size() == 0) {
                    throw new ServiceRuntimeException("You need to specify a source data source for each connection.");
                }
                sourceDataSource = sourceDateSourceNodes.get(0).getValue();

                Nodes targetDataSourceNodes = connectionNode.query("targetDataSource/text()");
                if (targetDataSourceNodes.size() == 0) {
                    throw new ServiceRuntimeException("You need to specify a target data source for each connection.");
                }
                targetDataSource = targetDataSourceNodes.get(0).getValue();

                Nodes sourceDateSourceFieldNodes = connectionNode.query("sourceDataSourceField/text()");
                if (sourceDateSourceFieldNodes.size() == 0) {
                    throw new ServiceRuntimeException("You need to specify a source data source field for each connection.");
                }
                sourceDataSourceField = sourceDateSourceFieldNodes.get(0).getValue();

                Nodes targetDataSourceFieldNodes = connectionNode.query("targetDataSourceField/text()");
                if (targetDataSourceFieldNodes.size() == 0) {
                    throw new ServiceRuntimeException("You need to specify a target data source field for each connection.</message></response>");
                }
                targetDataSourceField = targetDataSourceFieldNodes.get(0).getValue();

                CompositeFeedNode source = compositeNodes.get(sourceDataSource);
                CompositeFeedNode target = compositeNodes.get(targetDataSource);
                FeedDefinition sourceFeed = new FeedStorage().getFeedDefinitionData(source.getDataFeedID(), conn);
                FeedDefinition targetFeed = new FeedStorage().getFeedDefinitionData(target.getDataFeedID(), conn);
                Key sourceKey = findKey(sourceDataSourceField, sourceFeed);
                Key targetKey = findKey(targetDataSourceField, targetFeed);
                compositeConnections.add(new CompositeFeedConnection(source.getDataFeedID(), target.getDataFeedID(),
                        sourceKey, targetKey));
            }

            compositeFeedDefinition.setCompositeFeedNodes(new ArrayList<CompositeFeedNode>(compositeNodes.values()));
            compositeFeedDefinition.setConnections(compositeConnections);

            compositeFeedDefinition.populateFields(conn);

            long feedID = new FeedStorage().addFeedDefinitionData(compositeFeedDefinition, conn);
            DataStorage.liveDataSource(feedID, conn);
            return new ResponseInfo(ResponseInfo.ALL_GOOD, "<dataSourceKey>" + compositeFeedDefinition.getApiKey() + "</dataSourceKey>");
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
        }
    }

    private Key findKey(String fieldName, FeedDefinition dataSource) {
        for (AnalysisItem field : dataSource.getFields()) {
            if (fieldName.equals(field.getKey().toKeyString())) {
                return field.getKey();
            }
        }
        return null;
    }
}
