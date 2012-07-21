package test.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.userupload.UploadPolicy;

import java.util.ArrayList;
import java.util.List;

/**
* User: jamesboe
* Date: 7/19/12
* Time: 1:30 PM
*/
class DataSourceWrapper implements  ITestConstants {

    private FeedDefinition dataSource;
    private EIConnection conn;

    DataSourceWrapper(FeedDefinition dataSource, EIConnection conn) {
        this.dataSource = dataSource;
        this.conn = conn;
    }

    public static DataSourceWrapper createDataSource(EIConnection conn, Object... params) throws Exception {
        return createDataSource("Data Source Test", conn, params);
    }

    public static DataSourceWrapper createDataSource(String name, EIConnection conn, Object... params) throws Exception {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        for (int i = 0; i < params.length; i+=2) {
            String paramName = (String) params[i];
            Integer type = (Integer) params[i + 1];
            if (type == GROUPING) {
                fields.add(new AnalysisDimension(new NamedKey(paramName)));
            } else if (type == MEASURE) {
                fields.add(new AnalysisMeasure(new NamedKey(paramName), AggregationTypes.SUM));
            } else if (type == DATE) {
                fields.add(new AnalysisDateDimension(new NamedKey(paramName), true, AnalysisDateDimension.DAY_LEVEL));
            }
        }
        FeedDefinition feedDefinition = new FeedDefinition();
        feedDefinition.setFeedName(name);
        UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
        feedDefinition.setUploadPolicy(uploadPolicy);
        feedDefinition.setFields(fields);
        FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), uploadPolicy);
        feedDefinition.setDataFeedID(result.getFeedID());
        return new DataSourceWrapper(feedDefinition, conn);
    }

    public static DataSourceWrapper createJoinedSource(String name, EIConnection conn, DataSourceWrapper... children) throws Exception {
        List<CompositeFeedNode> nodes = new ArrayList<CompositeFeedNode>();
        for (DataSourceWrapper child : children) {
            nodes.add(new CompositeFeedNode(child.getDataSource().getDataFeedID(), 0, 0, child.getDataSource().getFeedName(), child.getDataSource().getFeedType().getType()));
        }
        CompositeFeedDefinition def = new FeedService().createCompositeFeed(nodes, new ArrayList<CompositeFeedConnection>(), name, conn);
        return new DataSourceWrapper(def, conn);
    }

    public void join(DataSourceWrapper wrapper1, DataSourceWrapper wrapper2, String field1, String field2) throws Exception {
        CompositeFeedConnection connection = new CompositeFeedConnection();
        connection.setSourceFeedID(wrapper1.getDataSource().getDataFeedID());
        connection.setTargetFeedID(wrapper2.getDataSource().getDataFeedID());
        AnalysisItem sourceField = null;
        AnalysisItem targetField = null;
        for (AnalysisItem field : wrapper1.getDataSource().getFields()) {
            if (field1.equals(field.toDisplay())) {
                sourceField = field;
            }
        }
        if (sourceField == null) {
            throw new RuntimeException();
        }
        for (AnalysisItem field : wrapper2.getDataSource().getFields()) {
            if (field2.equals(field.toDisplay())) {
                targetField = field;
            }
        }
        if (targetField == null) {
            throw new RuntimeException();
        }
        connection.setSourceItem(sourceField);
        connection.setTargetItem(targetField);
        CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) dataSource;
        compositeFeedDefinition.getConnections().add(connection);
        new FeedStorage().updateDataFeedConfiguration(compositeFeedDefinition, conn);
    }

    public void addRow(Object... params) throws Exception {
        DataSet dataSet = new DataSet();
        IRow row = dataSet.createRow();
        for (int i = 0; i < dataSource.getFields().size(); i++) {
            AnalysisItem analysisItem = dataSource.getFields().get(i);
            Object param = params[i];
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                row.addValue(analysisItem.getKey(), (Number) param);
            } else {
                row.addValue(analysisItem.getKey(), (String) param);
            }
        }
        DataStorage dataStorage = DataStorage.writeConnection(dataSource, conn);
        dataStorage.insertData(dataSet);
        dataStorage.commit();
        dataStorage.closeConnection();
    }

    public ReportWrapper createReport() {
        WSListDefinition wsListDefinition = new WSListDefinition();
        wsListDefinition.setFilterDefinitions(new ArrayList<FilterDefinition>());
        wsListDefinition.setDataFeedID(dataSource.getDataFeedID());
        wsListDefinition.setColumns(new ArrayList<AnalysisItem>());
        FeedMetadata feedMetadata = new DataService().getFeedMetadata(dataSource.getDataFeedID());
        return new ReportWrapper(wsListDefinition, feedMetadata);
    }

    public FeedDefinition getDataSource() {
        return dataSource;
    }

    public EIConnection getConn() {
        return conn;
    }
}
