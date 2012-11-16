package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSStackedBarChartDefinition;
import com.easyinsight.analysis.definitions.WSStackedColumnChartDefinition;
import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.core.*;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.FederatedDataSource;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.security.*;
import com.easyinsight.security.SecurityException;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.cache.Cache;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.easyinsight.solutions.SolutionService;
import com.easyinsight.storage.CachedCalculationTransform;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.IDataTransform;
import com.easyinsight.userupload.AnalysisItemFormatMapper;
import com.easyinsight.userupload.ExcelUploadFormat;
import nu.xom.Builder;
import nu.xom.Document;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.hibernate.Session;
import org.apache.jcs.access.exception.CacheException;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 7:32:24 PM
 */
public class AnalysisService {

    private AnalysisStorage analysisStorage = new AnalysisStorage();

    public List<Revision> showHistory(String urlKey) {
        List<Revision> revisions = new ArrayList<Revision>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT REPORT_HISTORY_ID, INSERT_TIME FROM REPORT_HISTORY WHERE URL_KEY = ?");
            stmt.setString(1, urlKey);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long historyID = rs.getLong(1);
                Date insertTime = new Date(rs.getTimestamp(2).getTime());
                Revision revision = new Revision();
                revision.setId(historyID);
                revision.setDate(insertTime);
                revisions.add(revision);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return revisions;
    }

    public WSAnalysisDefinition fromHistory(long id) {
        WSAnalysisDefinition returnReport = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("SELECT REPORT_XML FROM REPORT_HISTORY WHERE REPORT_HISTORY_ID = ?");
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String xml = rs.getString(1);
            XMLImportMetadata xmlImportMetadata = new XMLImportMetadata();
            xmlImportMetadata.setConn(conn);
            Document doc = new Builder().build(new ByteArrayInputStream(xml.getBytes()));
            AnalysisDefinition report = AnalysisDefinition.fromXML(doc.getRootElement(), xmlImportMetadata);
            new AnalysisStorage().saveAnalysis(report, conn);
            conn.commit();
            returnReport = report.createBlazeDefinition();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return returnReport;
    }

    public ReportInfo getReportInfo(long reportID) {
        try {
            SecurityUtil.authorizeInsight(reportID);
        } catch (Exception e) {
            ReportInfo reportInfo = new ReportInfo();
            reportInfo.setAccessDenied(true);
            return reportInfo;
        }
        try {
            WSAnalysisDefinition report = openAnalysisDefinition(reportID);
            boolean dataSourceAccessible;
            try {
                SecurityUtil.authorizeFeedAccess(report.getDataFeedID());
                dataSourceAccessible = true;
            } catch (Exception e) {
                dataSourceAccessible = false;
            }
            ReportInfo reportInfo = new ReportInfo();
            reportInfo.setAdmin(dataSourceAccessible);
            reportInfo.setReport(report);
            return reportInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public ReportJoins determineOverrides(long dataSourceID, List<AnalysisItem> items) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        ReportJoins reportJoins = new ReportJoins();
        EIConnection conn = Database.instance().getConnection();
        try {
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            if (dataSource instanceof CompositeFeedDefinition) {
                CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) dataSource;
                Map<String, List<JoinOverride>> map = new HashMap<String, List<JoinOverride>>();
                Map<String, List<DataSourceDescriptor>> dataSourceMap = new HashMap<String, List<DataSourceDescriptor>>();
                List<DataSourceDescriptor> configurableDataSources = new ArrayList<DataSourceDescriptor>();
                populateMap(map, dataSourceMap, configurableDataSources, compositeFeedDefinition, items, conn);
                reportJoins.setJoinOverrideMap(map);
                reportJoins.setDataSourceMap(dataSourceMap);
                reportJoins.setConfigurableDataSources(configurableDataSources);
            } else if (dataSource instanceof FederatedDataSource) {
                FederatedDataSource federatedDataSource = (FederatedDataSource) dataSource;
                Map<String, List<JoinOverride>> map = new HashMap<String, List<JoinOverride>>();
                Map<String, List<DataSourceDescriptor>> dataSourceMap = new HashMap<String, List<DataSourceDescriptor>>();
                List<DataSourceDescriptor> configurableDataSources = new ArrayList<DataSourceDescriptor>();
                CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) new FeedStorage().getFeedDefinitionData(federatedDataSource.getSources().get(0).getDataSourceID(), conn);
                List<JoinOverride> joinOverrides = new ArrayList<JoinOverride>();
                for (CompositeFeedConnection connection : compositeFeedDefinition.obtainChildConnections()) {
                    JoinOverride joinOverride = new JoinOverride();
                    FeedDefinition source = new FeedStorage().getFeedDefinitionData(connection.getSourceFeedID());
                    FeedDefinition target = new FeedStorage().getFeedDefinitionData(connection.getTargetFeedID());
                    joinOverride.setSourceItem(findSourceItem(connection, items == null ? compositeFeedDefinition.getFields() : items));
                    joinOverride.setTargetItem(findTargetItem(connection, items == null ? compositeFeedDefinition.getFields() : items));
                    joinOverride.setSourceName(source.getFeedName());
                    joinOverride.setTargetName(target.getFeedName());
                    joinOverrides.add(joinOverride);
                }
                List<DataSourceDescriptor> dataSources = new ArrayList<DataSourceDescriptor>();
                for (CompositeFeedNode child : compositeFeedDefinition.getCompositeFeedNodes()) {
                    FeedDefinition childDataSource = new FeedStorage().getFeedDefinitionData(child.getDataFeedID(), conn);
                    dataSources.add(new DataSourceDescriptor(childDataSource.getFeedName(), childDataSource.getDataFeedID(), childDataSource.getFeedType().getType(), false,
                            childDataSource.getDataSourceBehavior()));
                }
                dataSourceMap.put(String.valueOf(dataSourceID), dataSources);
                map.put(String.valueOf(dataSourceID), joinOverrides);
                configurableDataSources.add(new DataSourceDescriptor(dataSource.getFeedName(), dataSource.getDataFeedID(),
                        dataSource.getFeedType().getType(), false, dataSource.getDataSourceBehavior()));
                reportJoins.setJoinOverrideMap(map);
                reportJoins.setDataSourceMap(dataSourceMap);
                reportJoins.setConfigurableDataSources(configurableDataSources);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return reportJoins;
    }

    private void populateMap(Map<String, List<JoinOverride>> map, Map<String, List<DataSourceDescriptor>> dataSourceMap, List<DataSourceDescriptor> configurableDataSources,
                             CompositeFeedDefinition compositeFeedDefinition, List<AnalysisItem> items, EIConnection conn) throws SQLException {
        List<JoinOverride> joinOverrides = new ArrayList<JoinOverride>();

        configurableDataSources.add(new DataSourceDescriptor(compositeFeedDefinition.getFeedName(), compositeFeedDefinition.getDataFeedID(), compositeFeedDefinition.getFeedType().getType(),
                false, compositeFeedDefinition.getDataSourceBehavior()));
        for (CompositeFeedConnection connection : compositeFeedDefinition.obtainChildConnections()) {
            JoinOverride joinOverride = new JoinOverride();
            FeedDefinition source = new FeedStorage().getFeedDefinitionData(connection.getSourceFeedID());
            FeedDefinition target = new FeedStorage().getFeedDefinitionData(connection.getTargetFeedID());
            joinOverride.setSourceItem(findSourceItem(connection, items == null ? compositeFeedDefinition.getFields() : items));
            joinOverride.setTargetItem(findTargetItem(connection, items == null ? compositeFeedDefinition.getFields() : items));
            if (joinOverride.getSourceItem() != null && joinOverride.getTargetItem() != null) {
                joinOverride.setSourceName(source.getFeedName());
                joinOverride.setTargetName(target.getFeedName());
                joinOverrides.add(joinOverride);
            }
        }

        map.put(String.valueOf(compositeFeedDefinition.getDataFeedID()), joinOverrides);
        List<DataSourceDescriptor> dataSources = new ArrayList<DataSourceDescriptor>();
        for (CompositeFeedNode child : compositeFeedDefinition.getCompositeFeedNodes()) {
            FeedDefinition childDataSource = new FeedStorage().getFeedDefinitionData(child.getDataFeedID(), conn);
            dataSources.add(new DataSourceDescriptor(childDataSource.getFeedName(), childDataSource.getDataFeedID(), childDataSource.getFeedType().getType(), false,
                    childDataSource.getDataSourceBehavior()));
            if (childDataSource instanceof CompositeFeedDefinition) {
                populateMap(map, dataSourceMap, configurableDataSources, (CompositeFeedDefinition) childDataSource, null, conn);
            }
        }
        dataSourceMap.put(String.valueOf(compositeFeedDefinition.getDataFeedID()), dataSources);
    }

    private AnalysisItem findSourceItem(CompositeFeedConnection connection, List<AnalysisItem> items) {
        AnalysisItem analysisItem = null;
        for (AnalysisItem item : items) {
            Key key = item.getKey();
            if (key instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) key;
                if (derivedKey.getFeedID() == connection.getSourceFeedID()) {
                    if (connection.getSourceJoin() != null) {
                        if (item.hasType(AnalysisItemTypes.DIMENSION) && item.getKey().toKeyString().equals(connection.getSourceJoin().toKeyString())) {
                            analysisItem = item;
                            break;
                        }
                    } else {
                        if (connection.getSourceItem() != null) {
                            if (connection.getSourceItem().toDisplay().equals(item.toDisplay())) {
                                analysisItem = item;
                                break;
                            }
                        }
                    }
                }
            }

        }
        if (analysisItem == null && connection.getSourceItem() != null) {
            for (AnalysisItem item : items) {
                Key key = item.getKey();
                if (key instanceof DerivedKey) {
                    DerivedKey derivedKey = (DerivedKey) key;
                    if (derivedKey.getFeedID() == connection.getSourceFeedID()) {
                        if (connection.getSourceItem().getKey().toBaseKey().toKeyString().equals(item.getKey().toBaseKey().toKeyString())) {
                            analysisItem = item;
                            break;
                        }
                    }
                }
            }
        }
        return analysisItem;
    }
    
    private AnalysisItem findLabelItem(List<AnalysisItem> items) {
        for (AnalysisItem item : items) {
            if (item.isLabelColumn()) {
                return item;
            }
        }
        return null;
    }

    private AnalysisItem findTargetItem(CompositeFeedConnection connection, List<AnalysisItem> items) {
        AnalysisItem analysisItem = null;
        for (AnalysisItem item : items) {
            Key key = item.getKey();
            if (key instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) key;
                if (derivedKey.getFeedID() == connection.getTargetFeedID()) {
                    if (connection.getTargetJoin() != null) {
                        if (item.hasType(AnalysisItemTypes.DIMENSION) && item.getKey().toKeyString().equals(connection.getTargetJoin().toKeyString())) {
                            analysisItem = item;
                            break;
                        }
                    } else {
                        if (connection.getTargetItem() != null) {
                            if (connection.getTargetItem().toDisplay().equals(item.toDisplay())) {
                                analysisItem = item;
                                break;
                            }
                        }
                    }
                }
            }

        }
        if (analysisItem == null && connection.getTargetItem() != null) {
            for (AnalysisItem item : items) {
                Key key = item.getKey();
                if (key instanceof DerivedKey) {
                    DerivedKey derivedKey = (DerivedKey) key;
                    if (derivedKey.getFeedID() == connection.getTargetFeedID()) {
                        if (connection.getTargetItem().getKey().toBaseKey().toKeyString().equals(item.getKey().toBaseKey().toKeyString())) {
                            analysisItem = item;
                            break;
                        }
                    }
                }
            }
        }
        return analysisItem;
    }

    public void importRows(byte[] bytes, long dataSourceID) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        // have to translate provider name -> related provider
        DataStorage dataStorage = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            // TODO: fix
            FeedDefinition useSource;
            if (dataSource.getFeedName().equals("Therapy Works")) {
                useSource = resolveToName(dataSource, "Data Log");
            } else {
                useSource = dataSource;
            }

            AnalysisItemFormatMapper mapper = new AnalysisItemFormatMapper(useSource.getFields());
            Key providerPseudoField = new NamedKey("Provider");
            Key locationPseudoField = new NamedKey("Location");
            mapper.assignPseudoKey("Provider", providerPseudoField);
            mapper.assignPseudoKey("Location", locationPseudoField);
            PersistableDataSetForm data = new ExcelUploadFormat().createDataSet(bytes, useSource.getFields(), mapper);
            DataSet dataSet = data.toDataSet(null);
            Map<List<String>, String> map = new HashMap<List<String>, String>();

            AnalysisItem relatedProviderField = null;
            AnalysisItem locationField = null;
            AnalysisItem providerField = null;
            for (AnalysisItem field : useSource.getFields()) {
                if ("Related Provider".equals(field.toDisplay())) {
                    relatedProviderField = field;
                } else if ("Provider Name".equals(field.toDisplay())) {
                    providerField = field;
                } else if ("Location Name".equals(field.toDisplay())) {
                    locationField = field;
                }
            }

            WSListDefinition list = new WSListDefinition();
            list.setDataFeedID(dataSourceID);
            list.setFilterDefinitions(new ArrayList<FilterDefinition>());
            list.setColumns(Arrays.asList(locationField, providerField, relatedProviderField));
            DataSet dataSet1 = DataService.listDataSet(list, new InsightRequestMetadata(), conn);
            for (IRow row : dataSet1.getRows()) {
                List<String> key = new ArrayList<String>();
                key.add(row.getValue(providerField.createAggregateKey()).toString());
                key.add(row.getValue(locationField.createAggregateKey()).toString());
                map.put(key, row.getValue(relatedProviderField.createAggregateKey()).toString());
            }

            for (IRow row : dataSet.getRows()) {
                // retrieve location
                // retrieve provider
                // look up the actual related provider
                List<String> pair = new ArrayList<String>();
                Value provider = row.getValue(providerPseudoField);
                Value location = row.getValue(locationPseudoField);
                pair.add(provider.toString());
                pair.add(location.toString());
                String relatedProvider = map.get(pair);
                if (relatedProvider == null) {
                } else {
                    row.addValue(relatedProviderField.getKey(), relatedProvider);
                }
            }
            dataStorage = DataStorage.writeConnection(dataSource, conn);
            dataStorage.insertData(dataSet);
            dataStorage.commit();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void addRow(ActualRow actualRow, long dataSourceID) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            List<IDataTransform> transforms = new ArrayList<IDataTransform>();
            if (dataSource.getMarmotScript() != null && !"".equals(dataSource.getMarmotScript())) {
                StringTokenizer toker = new StringTokenizer(dataSource.getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    transforms.addAll(new ReportCalculation(line).apply(dataSource));
                }
            }
            transforms.add(new CachedCalculationTransform(dataSource));
            DataStorage dataStorage = DataStorage.writeConnection(dataSource, conn);
            try {
                dataStorage.addRow(actualRow, dataSource.getFields(), transforms);
                dataStorage.commit();
            } catch (Exception e) {
                dataStorage.rollback();
                throw e;
            } finally {
                dataStorage.closeConnection();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
    
    public void deleteRow(ActualRow actualRow, long dataSourceID) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            DataStorage dataStorage = DataStorage.writeConnection(dataSource, conn);
            try {
                dataStorage.deleteRow(actualRow.getRowID());
                dataStorage.commit();
            } catch (Exception e) {
                dataStorage.rollback();
                throw e;
            } finally {
                dataStorage.closeConnection();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
    
    public void updateRow(ActualRow actualRow, long dataSourceID) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            List<IDataTransform> transforms = new ArrayList<IDataTransform>();
            if (dataSource.getMarmotScript() != null && !"".equals(dataSource.getMarmotScript())) {
                StringTokenizer toker = new StringTokenizer(dataSource.getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    transforms.addAll(new ReportCalculation(line).apply(dataSource));
                }
            }
            transforms.add(new CachedCalculationTransform(dataSource));
            DataStorage dataStorage = DataStorage.writeConnection(dataSource, conn);
            try {
                dataStorage.updateRow(actualRow, dataSource.getFields(), transforms);
                dataStorage.commit();
            } catch (Exception e) {
                dataStorage.rollback();
                throw e;
            } finally {
                dataStorage.closeConnection();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
    
    public ActualRowSet setupAddRow(long dataSourceID, int offset) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.setUtcOffset(offset);
        try {
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID);
            // TODO: fix
            FeedDefinition useSource;
            if (dataSource.getFeedName().equals("Therapy Works")) {
                useSource = resolveToName(dataSource, "Data Log");
            } else {
                useSource = dataSource;
            }
            Map<String, Collection<JoinLabelOption>> optionMap = new HashMap<String, Collection<JoinLabelOption>>();
            if ("Data Log".equals(useSource.getFeedName())) {
                createOptionMap(dataSource, useSource, optionMap);
            }
            ActualRowSet actualRowSet = new ActualRowSet();
            actualRowSet.setDataSourceID(useSource.getDataFeedID());
            List<AnalysisItem> validFields = new ArrayList<AnalysisItem>();
            ActualRow actualRow = new ActualRow();
            Map<String, Value> map = new HashMap<String, Value>();
            for (AnalysisItem field : useSource.getFields()) {
                if (field.isConcrete() && !field.isDerived()) {
                    validFields.add(field);
                }
                map.put(field.qualifiedName(), new EmptyValue());
            }

            List<AnalysisItem> pool = new ArrayList<AnalysisItem>(validFields);

            List<ActualRowLayoutItem> forms;
            if ("Data Log".equals(useSource.getFeedName())) {
                forms = createForms(pool);
            } else {
                forms = new ArrayList<ActualRowLayoutItem>();
                ActualRowLayoutItem actualRowLayoutItem = new ActualRowLayoutItem();
                actualRowLayoutItem.setAnalysisItems(pool);
                actualRowLayoutItem.setColumns(3);
                actualRowLayoutItem.setColumnWidth(120);
                actualRowLayoutItem.setFormLabelWidth(140);
                forms.add(actualRowLayoutItem);
            }

            actualRow.setValues(map);
            actualRowSet.setRows(Arrays.asList(actualRow));
            actualRowSet.setAnalysisItems(validFields);
            actualRowSet.setForms(forms);
            actualRowSet.setOptions(optionMap);
            return actualRowSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private void createOptionMap(FeedDefinition dataSource, FeedDefinition useSource, Map<String, Collection<JoinLabelOption>> optionMap) {
        DerivedAnalysisDimension calculation = new DerivedAnalysisDimension();
        NamedKey key = new NamedKey("TmpCalculation");
        calculation.setKey(key);
        calculation.setDerivationCode("[Provider Name] + \"-\" + [Location Name]");
        List<AnalysisItem> matchItems = new ArrayList<AnalysisItem>();
        matchItems.add(calculation);

        if (dataSource instanceof CompositeFeedDefinition) {
            CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) dataSource;
            for (IJoin connection : compositeFeedDefinition.getConnections()) {
                CompositeFeedConnection compositeFeedConnection = (CompositeFeedConnection) connection;
                AnalysisItem sourceItem = null;
                AnalysisItem targetItem = null;
                if (connection.getSourceFeedID() == useSource.getDataFeedID()) {
                    sourceItem = findSourceItem(compositeFeedConnection, dataSource.getFields());
                    targetItem = findTargetItem(compositeFeedConnection, dataSource.getFields());
                } else if (connection.getTargetFeedID() == useSource.getDataFeedID()) {
                    sourceItem = findTargetItem(compositeFeedConnection, dataSource.getFields());
                    targetItem = findSourceItem(compositeFeedConnection, dataSource.getFields());
                }
                matchItems.add(targetItem);
                if (sourceItem != null && targetItem != null) {
                    List<JoinLabelOption> options = new ArrayList<JoinLabelOption>();
                    WSListDefinition target = new WSListDefinition();
                    target.setDataFeedID(dataSource.getDataFeedID());
                    target.setColumns(matchItems);
                    target.setFilterDefinitions(new ArrayList<FilterDefinition>());
                    EIConnection conn = Database.instance().getConnection();
                    try {
                        DataSet dataSet = DataService.listDataSet(target, new InsightRequestMetadata(), conn);
                        for (IRow row : dataSet.getRows()) {
                            Value sourceValue = row.getValue(targetItem);
                            Value calcValue = row.getValue(calculation);
                            if (sourceValue.type() == Value.EMPTY || calcValue.type() == Value.EMPTY || sourceValue.toString().equals("(Empty)") ||
                                    calcValue.toString().contains("(Empty)") || sourceValue.toString().equals("") || calcValue.toString().equals("")) {
                            } else {
                                options.add(new JoinLabelOption(sourceValue, calcValue.toString()));

                            }
                        }
                    } finally {
                        Database.closeConnection(conn);
                    }
                    Collections.sort(options, new Comparator<JoinLabelOption>() {

                        public int compare(JoinLabelOption joinLabelOption, JoinLabelOption joinLabelOption1) {
                            return joinLabelOption.getDisplayName().compareTo(joinLabelOption1.getDisplayName());
                        }
                    });
                    optionMap.put(sourceItem.toDisplay(), options);
                }
            }
        }
    }

    public ActualRowSet getActualRows(Map<String, Object> data, AnalysisItem analysisItem, WSAnalysisDefinition report, int offset) {
        SecurityUtil.authorizeFeedAccess(report.getDataFeedID());
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.setUtcOffset(offset);
        try {
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(report.getDataFeedID());
            FeedDefinition useSource = resolveToDataSource(dataSource, analysisItem.getKey());
            Map<String, Collection<JoinLabelOption>> optionMap = new HashMap<String, Collection<JoinLabelOption>>();
            if ("Data Log".equals(useSource.getFeedName())) {
                createOptionMap(dataSource, useSource, optionMap);
            }
            List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
            FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
            filterValueDefinition.setField(analysisItem);
            filterValueDefinition.setSingleValue(true);
            filterValueDefinition.setEnabled(true);
            filterValueDefinition.setInclusive(true);
            filterValueDefinition.setToggleEnabled(true);
            Value value = (Value) data.get(analysisItem.qualifiedName());
            if (value instanceof NumericValue) {
                String stringValue = ((Integer) value.toDouble().intValue()).toString();
                value = new StringValue(stringValue);
            }
            filterValueDefinition.setFilteredValues(Arrays.asList((Object) value));
            filters.add(filterValueDefinition);
            List<AnalysisItem> validFields = new ArrayList<AnalysisItem>();
            for (AnalysisItem field : useSource.getFields()) {
                if (field.isConcrete() && !field.isDerived()) {
                    validFields.add(field);
                }
            }
            DataStorage dataStorage = DataStorage.readConnection(useSource.getFields(), useSource.getDataFeedID());
            try {
                ActualRowSet rowSet = dataStorage.allData(filters, useSource.getFields(), null, insightRequestMetadata);
                rowSet.setOptions(optionMap);
                rowSet.setDataSourceID(useSource.getDataFeedID());
                List<AnalysisItem> pool = new ArrayList<AnalysisItem>(validFields);

                List<ActualRowLayoutItem> forms;
                if ("Data Log".equals(useSource.getFeedName())) {
                    forms = createForms(pool);
                } else {
                    forms = new ArrayList<ActualRowLayoutItem>();
                    ActualRowLayoutItem actualRowLayoutItem = new ActualRowLayoutItem();
                    actualRowLayoutItem.setColumns(3);
                    actualRowLayoutItem.setColumnWidth(120);
                    actualRowLayoutItem.setFormLabelWidth(140);
                    actualRowLayoutItem.setAnalysisItems(pool);
                    forms.add(actualRowLayoutItem);
                }

                rowSet.setForms(forms);
                return rowSet;
            } finally {
                dataStorage.closeConnection();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private List<ActualRowLayoutItem> createForms(List<AnalysisItem> pool) throws SQLException {
        List<ActualRowLayoutItem> forms = new ArrayList<ActualRowLayoutItem>();
        forms.addAll(new ReportCalculation("defineform(2, 0, 300, 110, \"Related Provider\", \"Date\")").apply(pool));
        forms.addAll(new ReportCalculation("defineform(3, 0, 120, 140, \"Visits Schd\", \"Walk-Ins\", \"MC/WC Schd\", \"Init Evals Schd\"," +
                "\"Init Evals CX/NS\", \"FUV CX/NS\", \"Hr-WK per FD\", \"Hr-Patient per FD\", \"Procedures/Day-FD\", \"Notes\")").apply(pool));
        forms.addAll(new ReportCalculation("defineform(3, 0, 120, 140, \"Visits per PMR\", \"Visits-Override\", \"Charges-Override\", \"Adjustments\"," +
                "\"Payments-Override\", \"Expenses\", \"Net Income\", \"wRVU-Override\", \"RVU-Override\", \"Gross Payroll\", \"FTE Hrs for MNT\"," +
                "\"Payments-Bonus Override\", \"Bonus Base Override\", \"Bonus % Override\", \"Bonus Override\", \"Bonus-Splints\", \"HR-Override\"," +
                "\"Hr-Admin\", \"Hr-CME\", \"HR-PTO\", \"Hr-HOL\", \"Hr-Patient per PMR\", \"Hr-WK per PMR\", \"HR-WK-Override\", \"Hr-Paid\"," +
                "\"FUV per PMR\", \"FUV-Override\", \"Charges-CAP\", \"Charges-FFS\", \"Charges-Non-CAP\", \"Charges-Supplies\", \"Charges-Treatment\", \"A/R\"," +
                "\"Payments-CAP\", \"Payments-FFS\", \"Payments-Non-CAP\", \"Payments-Supplies\", \"Payments-Treatment\", \"Referral-HT\", \"Referral-OT\"," +
                "\"Referral-PT\", \"Referral-SP\", \"Referral-Override\")").apply(pool));
        forms.addAll(new ReportCalculation("defineform(3, 1, 120, 140, \"*PT/OT*\")").apply(pool));
        forms.addAll(new ReportCalculation("defineform(3, 1, 120, 140, \"*Orthotics*\")").apply(pool));
        forms.addAll(new ReportCalculation("defineform(3, 1, 120, 140, \"*CA WC*\")").apply(pool));
        forms.addAll(new ReportCalculation("defineform(3, 1, 120, 140, \"*MediCal*\")").apply(pool));
        forms.addAll(new ReportCalculation("defineform(3, 1, 120, 140, \"*SPEECH*\")").apply(pool));
        return forms;
    }

    private FeedDefinition resolveToDataSource(FeedDefinition dataSource, Key key) throws SQLException {
        if (key instanceof NamedKey) {
            return dataSource;
        } else {
            DerivedKey derivedKey = (DerivedKey) key;
            long parentID = derivedKey.getFeedID();
            FeedDefinition parent = new FeedStorage().getFeedDefinitionData(parentID);
            return resolveToDataSource(parent, derivedKey.getParentKey());
        }
    }

    private FeedDefinition resolveToName(FeedDefinition dataSource, String name) throws SQLException {
        if (dataSource.getFeedName().equals(name)) {
            return dataSource;
        } else {
            CompositeFeedDefinition def = (CompositeFeedDefinition) dataSource;
            FeedDefinition match = null;
            for (CompositeFeedNode node : def.getCompositeFeedNodes()) {
                if (node.getDataSourceName().equals(name)) {
                     match = new FeedStorage().getFeedDefinitionData(node.getDataFeedID());
                }
            }
            if (match == null) {
                for (CompositeFeedNode node : def.getCompositeFeedNodes()) {
                    match = resolveToName(new FeedStorage().getFeedDefinitionData(node.getDataFeedID()), name);
                    if (match != null) {
                        break;
                    }
                }
            }
            return match;
        }
    }

    public DrillThroughResponse drillThrough(DrillThrough drillThrough, Object dataObj, AnalysisItem analysisItem,
                                             WSAnalysisDefinition report, String altKey) {
        try {
            Map<String, Object> data;
            if (dataObj instanceof Map) {
                data = (Map<String, Object>) dataObj;
            } else if (dataObj instanceof TrendOutcome) {
                TrendOutcome trendOutcome = (TrendOutcome) dataObj;
                data = new HashMap<String, Object>(trendOutcome.getDimensions());
            } else {
                throw new RuntimeException();
            }
            List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
            if (drillThrough.getMarmotScript() != null && !"".equals(drillThrough.getMarmotScript())) {

                filters = new ArrayList<FilterDefinition>();
                StringTokenizer toker = new StringTokenizer(drillThrough.getMarmotScript(), "\r\n");
                while (toker.hasMoreTokens()) {
                    String line = toker.nextToken();
                    filters.addAll(new ReportCalculation(line).apply(data, new ArrayList<AnalysisItem>(report.getAllAnalysisItems()), report,
                            analysisItem));
                }
            } else {
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {

                } else {
                    if (report.getReportType() == WSAnalysisDefinition.HEATMAP) {
                        CoordinateValue coordinateValue = (CoordinateValue) data.get(analysisItem.qualifiedName());
                        FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
                        filterValueDefinition.setField(analysisItem);
                        filterValueDefinition.setSingleValue(true);
                        filterValueDefinition.setEnabled(true);
                        filterValueDefinition.setInclusive(true);
                        filterValueDefinition.setToggleEnabled(true);
                        filterValueDefinition.setShowOnReportView(drillThrough.isShowDrillThroughFilters());
                        filterValueDefinition.setFilteredValues(Arrays.asList((Object) coordinateValue.getZip()));
                        filters.add(filterValueDefinition);
                    } else if (report.getReportType() == WSAnalysisDefinition.STACKED_COLUMN) {
                        WSStackedColumnChartDefinition stackedColumnChartDefinition = (WSStackedColumnChartDefinition) report;
                        {
                            FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
                            filterValueDefinition.setField(stackedColumnChartDefinition.getStackItem());
                            filterValueDefinition.setShowOnReportView(drillThrough.isShowDrillThroughFilters());
                            filterValueDefinition.setSingleValue(true);
                            filterValueDefinition.setEnabled(true);
                            filterValueDefinition.setInclusive(true);
                            filterValueDefinition.setToggleEnabled(true);

                            filterValueDefinition.setFilteredValues(Arrays.asList((Object) altKey));
                            filters.add(filterValueDefinition);
                        }
                        {
                            FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
                            filterValueDefinition.setField(stackedColumnChartDefinition.getXaxis());
                            filterValueDefinition.setShowOnReportView(drillThrough.isShowDrillThroughFilters());
                            filterValueDefinition.setSingleValue(true);
                            filterValueDefinition.setEnabled(true);
                            filterValueDefinition.setInclusive(true);
                            filterValueDefinition.setToggleEnabled(true);

                            filterValueDefinition.setFilteredValues(Arrays.asList(data.get(stackedColumnChartDefinition.getXaxis().qualifiedName())));
                            filters.add(filterValueDefinition);
                        }
                    } else if (report.getReportType() == WSAnalysisDefinition.STACKED_BAR) {
                        WSStackedBarChartDefinition stackedColumnChartDefinition = (WSStackedBarChartDefinition) report;
                        {
                            FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
                            filterValueDefinition.setField(stackedColumnChartDefinition.getStackItem());
                            filterValueDefinition.setShowOnReportView(drillThrough.isShowDrillThroughFilters());
                            filterValueDefinition.setSingleValue(true);
                            filterValueDefinition.setEnabled(true);
                            filterValueDefinition.setInclusive(true);
                            filterValueDefinition.setToggleEnabled(true);

                            filterValueDefinition.setFilteredValues(Arrays.asList((Object) altKey));
                            filters.add(filterValueDefinition);
                        }
                        {
                            FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
                            filterValueDefinition.setField(stackedColumnChartDefinition.getYaxis());
                            filterValueDefinition.setShowOnReportView(drillThrough.isShowDrillThroughFilters());
                            filterValueDefinition.setSingleValue(true);
                            filterValueDefinition.setEnabled(true);
                            filterValueDefinition.setInclusive(true);
                            filterValueDefinition.setToggleEnabled(true);

                            filterValueDefinition.setFilteredValues(Arrays.asList(data.get(stackedColumnChartDefinition.getYaxis().qualifiedName())));
                            filters.add(filterValueDefinition);
                        }
                    } else {
                        Object target = data.get(analysisItem.qualifiedName());
                        boolean multiValue = false;
                        if (target instanceof Value) {
                            Value value = (Value) target;
                            if (value.getOtherValues() != null) {
                                multiValue = true;
                            }
                        }
                        FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
                        filterValueDefinition.setField(analysisItem);
                        filterValueDefinition.setShowOnReportView(drillThrough.isShowDrillThroughFilters());

                        filterValueDefinition.setEnabled(true);
                        filterValueDefinition.setInclusive(true);
                        filterValueDefinition.setToggleEnabled(true);
                        if (multiValue) {
                            filterValueDefinition.setSingleValue(false);
                            Value value = (Value) target;
                            List<Object> objs = new ArrayList<Object>();
                            for (Value val : value.getOtherValues()) {
                                objs.add(val);
                            }
                            filterValueDefinition.setFilteredValues(objs);
                        } else {
                            filterValueDefinition.setSingleValue(true);
                            filterValueDefinition.setFilteredValues(Arrays.asList(data.get(analysisItem.qualifiedName())));
                        }
                        filters.add(filterValueDefinition);
                    }
                    if (drillThrough.isAddAllFilters()) {
                        filters.addAll(new ReportCalculation("drillthroughAddFilters()").apply(data, new ArrayList<AnalysisItem>(report.getAllAnalysisItems()), report,
                                analysisItem));
                    }
                    if (drillThrough.isFilterRowGroupings()) {
                        for (AnalysisItem grouping : report.getAllAnalysisItems()) {
                            if (grouping.hasType(AnalysisItemTypes.DIMENSION)) {
                                FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
                                filterValueDefinition.setField(grouping);
                                filterValueDefinition.setSingleValue(true);
                                filterValueDefinition.setEnabled(true);
                                filterValueDefinition.setShowOnReportView(drillThrough.isShowDrillThroughFilters());
                                filterValueDefinition.setToggleEnabled(true);
                                filterValueDefinition.setInclusive(true);
                                filterValueDefinition.setFilteredValues(Arrays.asList(data.get(grouping.qualifiedName())));
                                filters.add(filterValueDefinition);
                            }
                        }
                    }
                }
            }
            DrillThroughResponse drillThroughResponse = new DrillThroughResponse();
            EIDescriptor descriptor;
            if (drillThrough.getReportID() != null && drillThrough.getReportID() != 0) {
                InsightResponse insightResponse = openAnalysisIfPossibleByID(drillThrough.getReportID());
                descriptor = insightResponse.getInsightDescriptor();
            } else {
                DashboardDescriptor dashboardDescriptor = new DashboardDescriptor();
                String urlKey = new DashboardStorage().urlKeyForID(drillThrough.getDashboardID());
                dashboardDescriptor.setId(drillThrough.getDashboardID());
                dashboardDescriptor.setUrlKey(urlKey);
                descriptor = dashboardDescriptor;
            }
            drillThroughResponse.setDescriptor(descriptor);
            drillThroughResponse.setFilters(filters);
            return drillThroughResponse;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<Date> testFormat(String format, String value1, String value2, String value3) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            Date date1 = null;
            Date date2 = null;
            Date date3 = null;
            if (value1 != null) date1 = transform(dateFormat, value1);
            if (value2 != null) date2 = transform(dateFormat, value2);
            if (value3 != null) date3 = transform(dateFormat, value3);
            return Arrays.asList(date1, date2, date3);
        } catch (IllegalArgumentException iae) {
            return new ArrayList<Date>();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private Date transform(DateFormat dateFormat, String value) {
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            return null;
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    public AnalysisItem cloneItem(AnalysisItem analysisItem) {
        System.out.println("WHYYYYYYYYYYYY");
        try {
            AnalysisItem copy = analysisItem.clone();
            if (copy instanceof AnalysisHierarchyItem ||
                    copy instanceof AnalysisCalculation ||
                    copy instanceof DerivedAnalysisDateDimension ||
                    copy instanceof DerivedAnalysisDimension) {
                Key key = new NamedKey("Copy of " + analysisItem.toDisplay());
                copy.setKey(key);
                copy.setDisplayName("Copy of " + analysisItem.toDisplay());
            } else {
                copy.setOriginalDisplayName(analysisItem.toDisplay());
                copy.setDisplayName("Copy of " + analysisItem.toDisplay());
            }
            copy.setConcrete(false);
            return copy;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<InsightDescriptor> getInsightDescriptorsForDataSource(long dataSourceID) {
        long userID = SecurityUtil.getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            return analysisStorage.getInsightDescriptorsForDataSource(userID, SecurityUtil.getAccountID(), dataSourceID, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public Collection<InsightDescriptor> getInsightDescriptors() {
        long userID = SecurityUtil.getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            return analysisStorage.getReports(userID, SecurityUtil.getAccountID(), conn).values();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public WSAnalysisDefinition saveAs(WSAnalysisDefinition saveDefinition, String newName) {
        SecurityUtil.authorizeInsight(saveDefinition.getAnalysisID());
        EIConnection conn = Database.instance().getConnection();
        long reportID;
        try {
            conn.setAutoCommit(false);
            Session session = Database.instance().createSession(conn);
            AnalysisDefinition analysisDefinition = AnalysisDefinitionFactory.fromWSDefinition(saveDefinition);
            Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), conn);
            AnalysisDefinition clone = analysisDefinition.clone(null, feed.getFields(), false);
            clone.setAuthorName(SecurityUtil.getUserName());
            clone.setTitle(newName);
            List<UserToAnalysisBinding> bindings = new ArrayList<UserToAnalysisBinding>();
            bindings.add(new UserToAnalysisBinding(SecurityUtil.getUserID(), UserPermission.OWNER));
            clone.setUserBindings(bindings);
            session.close();
            session = Database.instance().createSession(conn);
            analysisStorage.saveAnalysis(clone, session);
            session.flush();
            session.close();
            reportID = clone.getAnalysisID();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            AnalysisDefinition savedReport = analysisStorage.getPersistableReport(reportID, session);
            WSAnalysisDefinition result = savedReport.createBlazeDefinition();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public WSAnalysisDefinition copyReport(WSAnalysisDefinition saveDefinition, long targetID) {
        SecurityUtil.authorizeInsight(saveDefinition.getAnalysisID());
        EIConnection conn = Database.instance().getConnection();
        long reportID;
        try {
            conn.setAutoCommit(false);
            FeedDefinition targetDataSource = new FeedStorage().getFeedDefinitionData(targetID);
            Session session = Database.instance().createSession(conn);
            AnalysisDefinition analysisDefinition = AnalysisDefinitionFactory.fromWSDefinition(saveDefinition);
            AnalysisDefinition clone = analysisDefinition.clone(targetDataSource, targetDataSource.getFields(), true);
            clone.setDataFeedID(targetDataSource.getDataFeedID());
            clone.setAuthorName(SecurityUtil.getUserName());
            List<UserToAnalysisBinding> bindings = new ArrayList<UserToAnalysisBinding>();
            bindings.add(new UserToAnalysisBinding(SecurityUtil.getUserID(), UserPermission.OWNER));
            clone.setUserBindings(bindings);
            session.close();
            session = Database.instance().createSession(conn);
            analysisStorage.saveAnalysis(clone, session);
            session.flush();
            session.close();
            reportID = clone.getAnalysisID();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            AnalysisDefinition savedReport = analysisStorage.getPersistableReport(reportID, session);
            WSAnalysisDefinition result = savedReport.createBlazeDefinition();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void keepReport(long reportID, long sourceReportID) {
        SecurityUtil.authorizeInsight(reportID);
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            AnalysisDefinition baseReport = analysisStorage.getPersistableReport(reportID, session);
            Map<Long, AnalysisDefinition> reports = new HashMap<Long, AnalysisDefinition>();
            Map<Long, Dashboard> dashboards = new HashMap<Long, Dashboard>();
            SolutionService.recurseReport(reports, dashboards, baseReport, session, conn);

            for (AnalysisDefinition report : reports.values()) {
                report.setTemporaryReport(false);
                new AnalysisStorage().clearCache(report.getAnalysisID(), report.getDataFeedID());
                session.update(report);
            }
            session.flush();
            for (Dashboard tDashboard : dashboards.values()) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD SET TEMPORARY_DASHBOARD = ? where dashboard_id = ?");
                updateStmt.setBoolean(1, false);
                updateStmt.setLong(2, tDashboard.getId());
                updateStmt.executeUpdate();
            }
            PreparedStatement queryStmt = conn.prepareStatement("SELECT EXCHANGE_REPORT_INSTALL_ID FROM EXCHANGE_REPORT_INSTALL WHERE USER_ID = ? AND " +
                    "REPORT_ID = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            queryStmt.setLong(2, sourceReportID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long id = rs.getLong(1);
                PreparedStatement updateTimeStmt = conn.prepareStatement("UPDATE EXCHANGE_REPORT_INSTALL SET install_date = ? WHERE EXCHANGE_REPORT_INSTALL_ID = ?");
                updateTimeStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                updateTimeStmt.setLong(2, id);
                updateTimeStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO EXCHANGE_REPORT_INSTALL (USER_ID, REPORT_ID, INSTALL_DATE) VALUES (?, ?, ?)");
                insertStmt.setLong(1, SecurityUtil.getUserID());
                insertStmt.setLong(2, sourceReportID);
                insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                insertStmt.execute();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
            Database.closeConnection(conn);
        }
    }

    public void shareReport(long reportID) {
        SecurityUtil.authorizeInsight(reportID);
        Connection conn = Database.instance().getConnection();
        try {
            new AnalysisStorage().clearCache(reportID, 0);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE ANALYSIS SET ACCOUNT_VISIBLE = ? WHERE ANALYSIS_ID = ?");
            updateStmt.setBoolean(1, false);
            updateStmt.setLong(2, reportID);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public String validateCalculation(String calculationString, long dataSourceID, List<AnalysisItem> reportItems, WSAnalysisDefinition report) {
        SecurityUtil.authorizeFeed(dataSourceID, Roles.SUBSCRIBER);
        EIConnection conn = Database.instance().getConnection();
        try {
            Feed feed = FeedRegistry.instance().getFeed(dataSourceID, conn);
            List<AnalysisItem> allItems = new ArrayList<AnalysisItem>(feed.getFields());
            allItems.addAll(reportItems);
            CalculationTreeNode tree;
            ICalculationTreeVisitor visitor;
            CalculationsParser.startExpr_return ret;
            CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(calculationString));
            CommonTokenStream tokes = new CommonTokenStream();
            tokes.setTokenSource(lexer);
            CalculationsParser parser = new CalculationsParser(tokes);
            parser.setTreeAdaptor(new NodeFactory());
            Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
            Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
            try {
                ret = parser.startExpr();
                tree = (CalculationTreeNode) ret.getTree();

                if (allItems != null) {
                    for (AnalysisItem analysisItem : allItems) {
                        List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                        if (items == null) {
                            items = new ArrayList<AnalysisItem>(1);
                            keyMap.put(analysisItem.getKey().toKeyString(), items);
                        }
                        items.add(analysisItem);
                    }
                    for (AnalysisItem analysisItem : allItems) {
                        List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                        if (items == null) {
                            items = new ArrayList<AnalysisItem>(1);
                            displayMap.put(analysisItem.toDisplay(), items);
                        }
                        items.add(analysisItem);
                    }
                }
                if (report != null && report.getFilterDefinitions() != null) {
                    for (FilterDefinition filter : report.getFilterDefinitions()) {
                        filter.calculationItems(displayMap);
                    }
                }
                visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
                tree.accept(visitor);
            } catch (RecognitionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            VariableListVisitor variableVisitor = new VariableListVisitor();
            tree.accept(variableVisitor);

            Set<KeySpecification> specs = variableVisitor.getVariableList();

            for (KeySpecification spec : specs) {
                try {
                    spec.findAnalysisItem(keyMap, displayMap);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }

            return null;
        } catch (ClassCastException cce) {
            if ("org.antlr.runtime.tree.CommonErrorNode cannot be cast to com.easyinsight.calculations.CalculationTreeNode".equals(cce.getMessage())) {
                return "There was a syntax error in your expression.";
            } else {
                return cce.getMessage();
            }
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public ReportMetrics getReportMetrics(long reportID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            return analysisStorage.getRating(reportID, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public ReportMetrics rateReport(long reportID, int rating) {
        long userID = SecurityUtil.getUserID();
        double ratingAverage;
        int ratingCount;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getExistingRatingStmt = conn.prepareStatement("SELECT user_report_rating_id FROM " +
                    "user_report_rating WHERE user_id = ? AND report_id = ?");
            getExistingRatingStmt.setLong(1, userID);
            getExistingRatingStmt.setLong(2, reportID);
            ResultSet rs = getExistingRatingStmt.executeQuery();
            if (rs.next()) {
                PreparedStatement updateRatingStmt = conn.prepareStatement("UPDATE user_report_rating " +
                        "SET RATING = ? WHERE user_report_rating_id = ?");
                updateRatingStmt.setInt(1, rating);
                updateRatingStmt.setLong(2, rs.getLong(1));
                updateRatingStmt.executeUpdate();
            } else {
                PreparedStatement insertRatingStmt = conn.prepareStatement("INSERT INTO user_report_rating " +
                        "(USER_ID, report_id, rating) values (?, ?, ?)");
                insertRatingStmt.setLong(1, userID);
                insertRatingStmt.setLong(2, reportID);
                insertRatingStmt.setInt(3, rating);
                insertRatingStmt.execute();
            }
            PreparedStatement queryStmt = conn.prepareStatement("SELECT AVG(RATING), COUNT(RATING) FROM USER_REPORT_RATING WHERE " +
                    "REPORT_ID = ?");
            queryStmt.setLong(1, reportID);
            ResultSet queryRS = queryStmt.executeQuery();
            queryRS.next();
            ratingAverage = queryRS.getDouble(1);
            ratingCount = queryRS.getInt(2);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

        return new ReportMetrics(ratingCount, ratingAverage, rating);
    }

    public WSAnalysisDefinition saveAnalysisDefinition(WSAnalysisDefinition wsAnalysisDefinition) {

        long userID = SecurityUtil.getUserID();
        if (wsAnalysisDefinition.getAnalysisID() > 0) {
            SecurityUtil.authorizeInsight(wsAnalysisDefinition.getAnalysisID());
        }
        try {
            Cache.getCache(Cache.EMBEDDED_REPORTS).remove(wsAnalysisDefinition.getDataFeedID());
        } catch (CacheException e) {
            LogClass.error(e);
        }
        long reportID;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            PreparedStatement getBindingsStmt = conn.prepareStatement("SELECT USER_ID, RELATIONSHIP_TYPE FROM USER_TO_ANALYSIS WHERE ANALYSIS_ID = ?");
            getBindingsStmt.setLong(1, wsAnalysisDefinition.getAnalysisID());
            ResultSet rs = getBindingsStmt.executeQuery();
            List<UserToAnalysisBinding> bindings = new ArrayList<UserToAnalysisBinding>();
            while (rs.next()) {
                long bindingUserID = rs.getLong(1);
                int relationshipType = rs.getInt(2);
                bindings.add(new UserToAnalysisBinding(bindingUserID, relationshipType));
            }
            if (bindings.isEmpty()) {
                bindings.add(new UserToAnalysisBinding(userID, UserPermission.OWNER));
            }
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM USER_TO_ANALYSIS WHERE ANALYSIS_ID = ?");
            stmt.setLong(1, wsAnalysisDefinition.getAnalysisID());
            stmt.executeUpdate();

            AnalysisDefinition analysisDefinition = AnalysisDefinitionFactory.fromWSDefinition(wsAnalysisDefinition);
            analysisDefinition.setUserBindings(bindings);
            analysisDefinition.setAuthorName(SecurityUtil.getUserName());
            analysisStorage.saveAnalysis(analysisDefinition, session);
            XMLMetadata xmlMetadata = new XMLMetadata();
            xmlMetadata.setConn(conn);
            /*String xml = analysisDefinition.toXML(xmlMetadata);
            PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO REPORT_HISTORY (INSERT_TIME, URL_KEY, REPORT_XML, ACCOUNT_ID) VALUES (?, ?, ?, ?)");
            saveStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            saveStmt.setString(2, analysisDefinition.getUrlKey());
            saveStmt.setString(3, xml);
            saveStmt.setLong(4, SecurityUtil.getAccountID());
            saveStmt.execute();*/
            session.flush();
            conn.commit();
            reportID = analysisDefinition.getAnalysisID();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        session = Database.instance().createSession();
        try {
            session.beginTransaction();
            AnalysisDefinition savedReport = analysisStorage.getPersistableReport(reportID, session);
            WSAnalysisDefinition result = savedReport.createBlazeDefinition();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void deleteAnalysisDefinition(long reportID) {
        int role = SecurityUtil.authorizeInsight(reportID);
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            AnalysisDefinition dbAnalysisDef = analysisStorage.getPersistableReport(reportID, session);
            boolean canDelete = role == Roles.OWNER;
            if (canDelete) {
                try {
                    session.delete(dbAnalysisDef);
                    session.flush();
                } catch (Exception e) {

                    // hibernate not cooperating, so delete it the hard way

                    PreparedStatement manualDeleteStmt = conn.prepareStatement("DELETE FROM ANALYSIS WHERE ANALYSIS_ID = ?");
                    manualDeleteStmt.setLong(1, reportID);
                    manualDeleteStmt.executeUpdate();
                }
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
            Database.closeConnection(conn);
        }
    }

    public List<FilterDefinition> getFilters(long reportID) {
        try {
            List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
            SecurityUtil.authorizeInsight(reportID);
            WSAnalysisDefinition report = analysisStorage.getAnalysisDefinition(reportID);
            for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
                FilterDefinition clone = filterDefinition.clone();
                filters.add(clone);
            }
            return filters;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public WSAnalysisDefinition openAnalysisDefinition(long analysisID) {
        try {
            SecurityUtil.authorizeInsight(analysisID);
            return analysisStorage.getAnalysisDefinition(analysisID);
        } catch (Exception e) {
            LogClass.error(e);
            return null;
        }
    }

    public InsightResponse openAnalysisIfPossible(String urlKey) {
        InsightResponse insightResponse = null;
        try {
            try {
                Connection conn = Database.instance().getConnection();
                try {
                    PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE FROM ANALYSIS WHERE URL_KEY = ?");
                    queryStmt.setString(1, urlKey);
                    ResultSet rs = queryStmt.executeQuery();
                    if (rs.next()) {
                        long analysisID = rs.getLong(1);
                        SecurityUtil.authorizeInsight(analysisID);
                        insightResponse = new InsightResponse(InsightResponse.SUCCESS, new InsightDescriptor(analysisID, rs.getString(2),
                                rs.getLong(3), rs.getInt(4), urlKey, Roles.NONE, false));
                    } else {
                        insightResponse = new InsightResponse(InsightResponse.REJECTED, null);
                    }
                } finally {
                    Database.closeConnection(conn);
                }

            } catch (SecurityException e) {
                long userID = SecurityUtil.getUserID(false);
                if (userID > 0) {
                    Connection conn = Database.instance().getConnection();
                    try {
                        PreparedStatement accountStmt = conn.prepareStatement("SELECT ACCOUNT_ID FROM USER, USER_TO_ANALYSIS, ANALYSIS WHERE ANALYSIS.URL_KEY = ? AND " +
                                "ANALYSIS.ANALYSIS_ID = USER_TO_ANALYSIS.ANALYSIS_ID AND USER_TO_ANALYSIS.USER_ID = USER.USER_ID");
                        accountStmt.setString(1, urlKey);
                        ResultSet rs = accountStmt.executeQuery();
                        if (rs.next()) {
                            long accountID = rs.getLong(1);
                            if (accountID == SecurityUtil.getAccountID()) {
                                insightResponse = new InsightResponse(InsightResponse.PRIVATE_ACCESS, null);
                            } else {
                                insightResponse = new InsightResponse(InsightResponse.REJECTED, null);
                            }
                        }
                    } finally {
                        Database.closeConnection(conn);
                    }
                }
                if (insightResponse == null) {
                    if (e.getReason() == InsightResponse.NEED_LOGIN)
                        insightResponse = new InsightResponse(InsightResponse.NEED_LOGIN, null);
                    else
                        insightResponse = new InsightResponse(InsightResponse.REJECTED, null);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            return null;
        }
        return insightResponse;
    }

    public InsightResponse openAnalysisIfPossibleByID(long analysisID) {
        InsightResponse insightResponse;
        try {
            try {
                SecurityUtil.authorizeInsight(analysisID);
                Connection conn = Database.instance().getConnection();
                try {
                    PreparedStatement queryStmt = conn.prepareStatement("SELECT TITLE, DATA_FEED_ID, REPORT_TYPE, URL_KEY FROM ANALYSIS WHERE ANALYSIS_ID = ?");
                    queryStmt.setLong(1, analysisID);
                    ResultSet rs = queryStmt.executeQuery();
                    rs.next();
                    insightResponse = new InsightResponse(InsightResponse.SUCCESS, new InsightDescriptor(analysisID, rs.getString(1),
                            rs.getLong(2), rs.getInt(3), rs.getString(4), Roles.NONE, false));
                } finally {
                    Database.closeConnection(conn);
                }

            } catch (SecurityException e) {
                if (e.getReason() == InsightResponse.NEED_LOGIN)
                    insightResponse = new InsightResponse(InsightResponse.NEED_LOGIN, null);
                else
                    insightResponse = new InsightResponse(InsightResponse.REJECTED, null);
            }
        } catch (Exception e) {
            LogClass.error(e);
            return null;
        }
        return insightResponse;
    }

    public UserCapabilities getUserCapabilitiesForFeed(long feedID) {
        if (SecurityUtil.getUserID(false) == 0) {
            return new UserCapabilities(Roles.NONE, Roles.NONE, false, false);
        }
        long userID = SecurityUtil.getUserID();
        int feedRole = Integer.MAX_VALUE;
        boolean groupMember = false;
        boolean hasReports = false;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT ROLE FROM UPLOAD_POLICY_USERS WHERE " +
                    "USER_ID = ? AND FEED_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, feedID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                feedRole = rs.getInt(1);
            }
            PreparedStatement queryStmt = conn.prepareStatement("SELECT COUNT(COMMUNITY_GROUP.COMMUNITY_GROUP_ID) FROM COMMUNITY_GROUP, GROUP_TO_USER_JOIN WHERE " +
                    "USER_ID = ? AND GROUP_TO_USER_JOIN.GROUP_ID = COMMUNITY_GROUP.COMMUNITY_GROUP_ID");
            queryStmt.setLong(1, userID);
            ResultSet groupRS = queryStmt.executeQuery();
            groupRS.next();
            groupMember = groupRS.getInt(1) > 0;
            PreparedStatement analysisCount = conn.prepareStatement("SELECT COUNT(analysis_id) from analysis WHERE data_feed_id = ?");
            analysisCount.setLong(1, feedID);
            ResultSet analysisCountRS = analysisCount.executeQuery();
            analysisCountRS.next();
            hasReports = analysisCountRS.getInt(1) > 0;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return new UserCapabilities(Integer.MAX_VALUE, feedRole, groupMember, hasReports);
    }

    public UserCapabilities getUserCapabilitiesForInsight(long feedID, long insightID) {
        if (SecurityUtil.getUserID(false) == 0) {
            return new UserCapabilities(Roles.NONE, Roles.NONE, false, false);
        }
        UserCapabilities userCapabilities = getUserCapabilitiesForFeed(feedID);
        try {
            SecurityUtil.authorizeInsight(insightID);
            userCapabilities.setAnalysisRole(Roles.OWNER);
        } catch (SecurityException se) {
            userCapabilities.setAnalysisRole(Roles.NONE);
        }
        return userCapabilities;
    }

    public List<IntentionSuggestion> generatePossibleIntentions(WSAnalysisDefinition report, EIConnection conn) throws SQLException {
        List<IntentionSuggestion> suggestions = new ArrayList<IntentionSuggestion>();
        Feed feed = FeedRegistry.instance().getFeed(report.getDataFeedID());
        DataSourceInfo dataSourceInfo = feed.createSourceInfo(conn);
        FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(report.getDataFeedID(), conn);
        suggestions.addAll(dataSource.suggestIntentions(report, dataSourceInfo));
        suggestions.addAll(report.suggestIntentions(report));
        Collections.sort(suggestions, new Comparator<IntentionSuggestion>() {

            public int compare(IntentionSuggestion intentionSuggestion, IntentionSuggestion intentionSuggestion1) {
                return intentionSuggestion.getPriority().compareTo(intentionSuggestion1.getPriority());
            }
        });
        return suggestions;
    }

    public List<IntentionSuggestion> generatePossibleIntentions(WSAnalysisDefinition report) {
        EIConnection conn = Database.instance().getConnection();
        try {
            List<IntentionSuggestion> suggestions = new ArrayList<IntentionSuggestion>();
            Feed feed = FeedRegistry.instance().getFeed(report.getDataFeedID());
            DataSourceInfo dataSourceInfo = feed.createSourceInfo(conn);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(report.getDataFeedID(), conn);
            suggestions.addAll(dataSource.suggestIntentions(report, dataSourceInfo));
            suggestions.addAll(report.suggestIntentions(report));
            Collections.sort(suggestions, new Comparator<IntentionSuggestion>() {

                public int compare(IntentionSuggestion intentionSuggestion, IntentionSuggestion intentionSuggestion1) {
                    return intentionSuggestion.getPriority().compareTo(intentionSuggestion1.getPriority());
                }
            });
            return suggestions;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
    /*
    class com.easyinsight.pipeline.FilterComponent -
    [{Subject=filtering duplicate data, Organization Name=Easy Insight, Comment Created At=2012-11-05},
    {Subject=Problems setting up application, Organization Name=Easy Insight, Comment Created At=2012-11-05},
    {Subject=Zendesk Integration, Organization Name=Easy Insight, Comment Created At=2012-11-05},
    {Subject=Help Doc and Reseller, Organization Name=(Empty), Comment Created At=2012-11-01},
    {Subject=Zendesk Integration, Organization Name=(Empty), Comment Created At=2012-11-05}, {Subject=Problems setting up application, Organization Name=(Empty), Comment Created At=2012-11-04}, {Subject=Help Doc and Reseller, Organization Name=Easy Insight, Comment Created At=2012-11-05}, {Subject=filtering duplicate data, Organization Name=(Empty), Comment Created At=2012-11-04}]
     */

    public List<Intention> getIntentions(WSAnalysisDefinition report, List<AnalysisItem> fields, int scope, int type) {
        try {
            if (scope == IntentionSuggestion.SCOPE_DATA_SOURCE) {
                FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(report.getDataFeedID());
                return dataSource.createIntentions(report, fields, type);
            } else if (scope == IntentionSuggestion.SCOPE_REPORT) {
                return report.createIntentions(fields, type);
            } else {
                throw new UnsupportedOperationException();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
