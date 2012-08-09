package test.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.FallthroughConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.userupload.UploadPolicy;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

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
                AnalysisDateDimension date = new AnalysisDateDimension(new NamedKey(paramName), true, AnalysisDateDimension.DAY_LEVEL);
                //date.setDateOnlyField(true);
                fields.add(new AnalysisDateDimension(new NamedKey(paramName), true, AnalysisDateDimension.DAY_LEVEL));
            }
        }
        FeedDefinition feedDefinition = new FeedDefinition();
        feedDefinition.setFeedName(name);
        UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
        feedDefinition.setUploadPolicy(uploadPolicy);
        feedDefinition.setFields(fields);
        FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), uploadPolicy);
        result.getTableDefinitionMetadata().commit();
        result.getTableDefinitionMetadata().closeConnection();
        feedDefinition.setDataFeedID(result.getFeedID());
        return new DataSourceWrapper(feedDefinition, conn);
    }

    public static DataSourceWrapper createDataSource(FeedType feedType, EIConnection conn) throws Exception {
        FeedDefinition feedDefinition = new DataSourceTypeRegistry().createDataSource(feedType);
        UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
        feedDefinition.setUploadPolicy(uploadPolicy);
        IServerDataSourceDefinition serverDataSourceDefinition = (IServerDataSourceDefinition) feedDefinition;
        feedDefinition.setDataFeedID(serverDataSourceDefinition.create(conn, null, null));
        new FeedStorage().updateDataFeedConfiguration(feedDefinition, conn);
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

    public void fallthroughJoin(DataSourceWrapper wrapper1, DataSourceWrapper wrapper2, String... fields) throws Exception {
        List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();
        for (int i = 0; i < fields.length; i += 2) {
            CompositeFeedConnection connection = new CompositeFeedConnection();
            connection.setSourceFeedID(wrapper1.getDataSource().getDataFeedID());
            connection.setTargetFeedID(wrapper2.getDataSource().getDataFeedID());
            String field1 = fields[i];
            String field2 = fields[i + 1];
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
            connections.add(connection);
        }
        FallthroughConnection fallthroughConnection = new FallthroughConnection(wrapper1.getDataSource().getDataFeedID(), wrapper2.getDataSource().getDataFeedID(),
                connections);
        CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) dataSource;
        compositeFeedDefinition.getConnections().add(fallthroughConnection);
        new FeedStorage().updateDataFeedConfiguration(compositeFeedDefinition, conn);
    }

    public void join(DataSourceWrapper wrapper1, DataSourceWrapper wrapper2, ChildConnection childConnection) throws Exception {
        CompositeFeedConnection connection = childConnection.createConnection((IServerDataSourceDefinition) wrapper1.getDataSource(),
                (IServerDataSourceDefinition) wrapper2.getDataSource());
        CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) dataSource;
        compositeFeedDefinition.getConnections().add(connection);
        new FeedStorage().updateDataFeedConfiguration(compositeFeedDefinition, conn);
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

    public void addNamedRow(Object... params) throws Exception {
        DataSet dataSet = new DataSet();
        Map<String, AnalysisItem> fieldMap = new HashMap<String, AnalysisItem>();
        for (AnalysisItem analysisItem : dataSource.getFields()) {
            fieldMap.put(analysisItem.getKey().toKeyString(), analysisItem);
            fieldMap.put(analysisItem.toDisplay(), analysisItem);
        }
        IRow row = dataSet.createRow();
        for (int i = 0; i < params.length; i += 2) {
            String paramName = (String) params[i];
            AnalysisItem analysisItem = fieldMap.get(paramName);
            if (analysisItem == null) {
                throw new RuntimeException("Couldn't find field " + paramName);
            }
            Object param = params[i + 1];
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

    public void addRow(Object... params) throws Exception {
        DataSet dataSet = new DataSet();
        IRow row = dataSet.createRow();
        for (int i = 0; i < dataSource.getFields().size(); i++) {
            AnalysisItem analysisItem = dataSource.getFields().get(i);
            Object param = params[i];
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                row.addValue(analysisItem.getKey(), (Number) param);
            } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                if (param instanceof Date) {
                    row.addValue(analysisItem.getKey(), (Date) param);
                } else if (param instanceof String) {
                    AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                    SimpleDateFormat sdf = new SimpleDateFormat(date.getCustomDateFormat());
                    row.addValue(analysisItem.getKey(), sdf.parse((String) param));
                } else {
                    throw new RuntimeException();
                }
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

    public DataSourceWrapper find(FeedType targetType, EIConnection conn) throws SQLException {
        CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) getDataSource();
        for (CompositeFeedNode child : compositeFeedDefinition.getCompositeFeedNodes()) {
            if (child.getDataSourceType() == targetType.getType()) {
                return new DataSourceWrapper(new FeedStorage().getFeedDefinitionData(child.getDataFeedID(), conn), conn);
            }
        }
        throw new RuntimeException();
    }
}
