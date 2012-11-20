package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSCompareYearsDefinition;
import com.easyinsight.analysis.definitions.WSYTDDefinition;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportService;

import java.util.*;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:19:19 PM
 */
public abstract class Pipeline {

    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String LAST = "last_calculations";
    public static final String LAST_FILTERS = "last_filters";

    private List<IComponent> components = new ArrayList<IComponent>();
    private PipelineData pipelineData;
    private ResultsBridge resultsBridge = new ListResultsBridge();
    private AnalysisItemRetrievalStructure structure = new AnalysisItemRetrievalStructure(null);

    public Pipeline setup(WSAnalysisDefinition report, Feed dataSource, InsightRequestMetadata insightRequestMetadata) {
        structure.setReport(report);
        structure.setInsightRequestMetadata(insightRequestMetadata);
        List<AnalysisItem> allFields = new ArrayList<AnalysisItem>(dataSource.getFields());
        if (report.getAddedItems() != null) {
            allFields.addAll(report.getAddedItems());
        }
        Set<AnalysisItem> allNeededAnalysisItems = compilePipelineData(report, insightRequestMetadata, allFields, dataSource, null);
        components = generatePipelineCommands(allNeededAnalysisItems, pipelineData.getAllRequestedItems(), report.retrieveFilterDefinitions(), report, pipelineData.getAllItems(), insightRequestMetadata);
        if (report.hasCustomResultsBridge()) {
            resultsBridge = report.getCustomResultsBridge();
        }
        return this;
    }

    private Feed preassigned;

    public void setPreassigned(Feed preassigned) {
        this.preassigned = preassigned;
    }

    private List<AnalysisItem> additionalItems;

    public void setAdditionalItems(List<AnalysisItem> additionalItems) {
        this.additionalItems = additionalItems;
    }

    public Pipeline setup(WSAnalysisDefinition report, InsightRequestMetadata insightRequestMetadata) {
        if (preassigned == null) {
            throw new RuntimeException("You can only use this setup() call when you've preassigned an earlier data source.");
        }
        structure.setReport(report);
        structure.setInsightRequestMetadata(insightRequestMetadata);
        List<AnalysisItem> allFields = new ArrayList<AnalysisItem>(preassigned.getFields());
        allFields.addAll(additionalItems);
        /*if (report.getAddedItems() != null) {
            allFields.addAll(report.getAddedItems());
        }*/
        Set<AnalysisItem> allNeededAnalysisItems = compilePipelineData(report, insightRequestMetadata, allFields, preassigned, null);
        components = generatePipelineCommands(allNeededAnalysisItems, pipelineData.getAllRequestedItems(), report.retrieveFilterDefinitions(), report, pipelineData.getAllItems(), insightRequestMetadata);
        if (report.hasCustomResultsBridge()) {
            resultsBridge = report.getCustomResultsBridge();
        }
        return this;
    }

    public AnalysisItemRetrievalStructure getStructure() {
        return structure;
    }

    public Pipeline setup(WSAnalysisDefinition report, Feed dataSource, InsightRequestMetadata insightRequestMetadata, Set<AnalysisItem> reportItems) {
        structure.setReport(report);
        structure.setInsightRequestMetadata(insightRequestMetadata);
        int i = 1;
        if (insightRequestMetadata.getIntermediatePipelines() != null) {
            for (String pipe : insightRequestMetadata.getIntermediatePipelines()) {
                structure.getSections().add(i++, pipe);
            }
        }
        List<AnalysisItem> allFields = new ArrayList<AnalysisItem>(dataSource.getFields());
        if (report.getAddedItems() != null) {
            allFields.addAll(report.getAddedItems());
        }
        Set<AnalysisItem> allNeededAnalysisItems = compilePipelineData(report, insightRequestMetadata, allFields, dataSource, reportItems);
        components = generatePipelineCommands(allNeededAnalysisItems, pipelineData.getAllRequestedItems(), report.retrieveFilterDefinitions(), report, pipelineData.getAllItems(), insightRequestMetadata);
        if (report.hasCustomResultsBridge()) {
            resultsBridge = report.getCustomResultsBridge();
        }
        return this;
    }

    public Pipeline setup(WSAnalysisDefinition report, Feed dataSource, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allFields) {
        structure.setReport(report);
        structure.setInsightRequestMetadata(insightRequestMetadata);
        int i = 1;
        if (insightRequestMetadata.getIntermediatePipelines() != null) {
            for (String pipe : insightRequestMetadata.getIntermediatePipelines()) {
                structure.getSections().add(i++, pipe);
            }
        }
        Set<AnalysisItem> allNeededAnalysisItems = compilePipelineData(report, insightRequestMetadata, allFields, dataSource, null);
        components = generatePipelineCommands(allNeededAnalysisItems, pipelineData.getAllRequestedItems(), report.retrieveFilterDefinitions(), report, pipelineData.getAllItems(), insightRequestMetadata);
        if (report.hasCustomResultsBridge()) {
            resultsBridge = report.getCustomResultsBridge();
        }
        return this;
    }

    public Pipeline setup(WSAnalysisDefinition report, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allItems) {
        structure.setReport(report);
        structure.setInsightRequestMetadata(insightRequestMetadata);
        int i = 1;
        if (insightRequestMetadata.getIntermediatePipelines() != null) {
            for (String pipe : insightRequestMetadata.getIntermediatePipelines()) {
                structure.getSections().add(i++, pipe);
            }
        }
        Set<AnalysisItem> allNeededAnalysisItems = compilePipelineData(report, insightRequestMetadata, allItems, null, null);
        components = generatePipelineCommands(allNeededAnalysisItems, pipelineData.getAllRequestedItems(), report.retrieveFilterDefinitions(), report, pipelineData.getAllItems(), insightRequestMetadata);
        if (report.hasCustomResultsBridge()) {
            resultsBridge = report.getCustomResultsBridge();
        }
        return this;
    }

    public PipelineData getPipelineData() {
        return pipelineData;
    }

    public Pipeline setup(Set<AnalysisItem> analysisItems, List<AnalysisItem> allFields, InsightRequestMetadata insightRequestMetadata) {
        pipelineData = new PipelineData(null, analysisItems, null, allFields, new HashMap<String, String>(), analysisItems, null);
        components = generatePipelineCommands(analysisItems, analysisItems, new ArrayList<FilterDefinition>(), null, allFields, insightRequestMetadata);
        return this;
    }

    protected abstract List<IComponent> generatePipelineCommands(Set<AnalysisItem> allNeededAnalysisItems, Set<AnalysisItem> reportItems, Collection<FilterDefinition> filters, WSAnalysisDefinition report, List<AnalysisItem> allItems, InsightRequestMetadata insightRequestMetadata);
         
    private Set<AnalysisItem> compilePipelineData(WSAnalysisDefinition report, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allFields, Feed dataSource,
                                                  Set<AnalysisItem> allRequestedAnalysisItems) {


        if (allRequestedAnalysisItems == null) {
            allRequestedAnalysisItems = report.getAllAnalysisItems();
        } 
        if (report instanceof WSYTDDefinition || report instanceof WSCompareYearsDefinition) {
            for (AnalysisItem analysisItem : new HashSet<AnalysisItem>(allRequestedAnalysisItems)) {
                if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                    AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
                    if (analysisCalculation.getAggregation() == AggregationTypes.AVERAGE) {
                        List<AnalysisItem> baseItems = analysisItem.getAnalysisItems(allFields, allRequestedAnalysisItems, false, true, new HashSet<AnalysisItem>(), structure);
                        allRequestedAnalysisItems.addAll(baseItems);
                        List<AnalysisItem> linkItems = analysisItem.addLinkItems(allFields);
                        allRequestedAnalysisItems.addAll(linkItems);
                        if (analysisItem.isVirtual()) {
                            allRequestedAnalysisItems.add(analysisItem);
                        }
                    }
                }
            }
        }
        allRequestedAnalysisItems.remove(null);

        Set<AnalysisItem> allNeededAnalysisItems = new LinkedHashSet<AnalysisItem>();

        if (report.retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                List<AnalysisItem> items = filterDefinition.getAnalysisItems(allFields, allRequestedAnalysisItems, false, true, new HashSet<AnalysisItem>(), structure);
                allNeededAnalysisItems.addAll(items);
            }
        }
        for (AnalysisItem item : allRequestedAnalysisItems) {
            if (item.isValid()) {
                List<AnalysisItem> baseItems = item.getAnalysisItems(allFields, allRequestedAnalysisItems, false, true, new HashSet<AnalysisItem>(), structure);
                allNeededAnalysisItems.addAll(baseItems);
                List<AnalysisItem> linkItems = item.addLinkItems(allFields);
                allNeededAnalysisItems.addAll(linkItems);
                if (item.isVirtual()) {
                    allNeededAnalysisItems.add(item);
                }
            }
        }
        allNeededAnalysisItems.addAll(report.getLimitFields());
        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
        for (AnalysisItem analysisItem : allFields) {
            List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
            if (items == null) {
                items = new ArrayList<AnalysisItem>(1);
                keyMap.put(analysisItem.getKey().toKeyString(), items);
            }
            items.add(analysisItem);
        }
        for (AnalysisItem analysisItem : allFields) {
            List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
            if (items == null) {
                items = new ArrayList<AnalysisItem>(1);
                displayMap.put(analysisItem.toDisplay(), items);
            }
            items.add(analysisItem);
        }
        if (report.getReportRunMarmotScript() != null) {
            StringTokenizer toker = new StringTokenizer(report.getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                List<AnalysisItem> items = ReportCalculation.getAnalysisItems(line, allFields, keyMap, displayMap, new HashSet<AnalysisItem>(), false, true, structure);
                allNeededAnalysisItems.addAll(items);
            }
        }

        Map<Long, AnalysisItem> uniqueFields = new HashMap<Long, AnalysisItem>();

        if (report.getUniqueIteMap() != null) {
            Set<Long> ids = new HashSet<Long>();
            for (AnalysisItem analysisItem : allNeededAnalysisItems) {
                Key key = analysisItem.getKey();
                long dsID = toID(key);
                if (dsID != 0) {
                    ids.add(dsID);
                }
            }


            for (Long id : ids) {
                AnalysisDimension analysisDimension = (AnalysisDimension) report.getUniqueIteMap().get(id);
                if (analysisDimension != null) {
                    //analysisDimension.setGroup(false);
                    uniqueFields.put(id, analysisDimension);
                }
            }
            allNeededAnalysisItems.addAll(uniqueFields.values());
        }

        if (!report.getAdditionalGroupingItems().isEmpty()) {
            allNeededAnalysisItems.addAll(report.getAdditionalGroupingItems());
        }
        /*EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement query = conn.prepareStatement("SELECT analysis_item_id FROM data_source_to_unique_field WHERE data_source_id = ? and child_source_id = ?");
            for (Long id : ids) {
                query.setLong(1, report.getDataFeedID());
                query.setLong(2, id);
                ResultSet rs = query.executeQuery();
                if (rs.next()) {
                    long uniqueID = rs.getLong(1);
                    if (!rs.wasNull()) {
                        Session session = Database.instance().createSession(conn);
                        AnalysisDimension uniqueItem = (AnalysisDimension) session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, uniqueID).list().get(0);
                        uniqueItem.afterLoad();
                        for (AnalysisItem field : allFields) {
                            if (field.getKey().toBaseKey().equals(uniqueItem.getKey().toBaseKey()) && field.getType() == uniqueItem.getType()) {
                                uniqueFields.put(id, field);
                                AnalysisDimension dim = (AnalysisDimension) field;
                                dim.setGroup(false);
                            }
                        }
                    }
                }
            }
            query.close();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }*/


        pipelineData = new PipelineData(report, allNeededAnalysisItems, insightRequestMetadata, allFields, dataSource == null ? new HashMap<String, String>() : dataSource.getProperties(), allRequestedAnalysisItems,
                uniqueFields);
        return allNeededAnalysisItems;
    }
    
    private long toID(Key key) {
        if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            Key next = derivedKey.getParentKey();
            if (next instanceof NamedKey) {
                return derivedKey.getFeedID();
            }
            return toID(next);
        }
        return 0;
    }

    protected final Collection<AnalysisItem> items(int type, Collection<AnalysisItem> items) {
        Collection<AnalysisItem> matchingItems = new ArrayList<AnalysisItem>();
        for (AnalysisItem item : items) {
            if (item.hasType(type)) {
                matchingItems.add(item);
            }
        }
        return matchingItems;
    }

    public DataSet toDataSet(DataSet dataSet) {
        for (IComponent component : components) {
            dataSet = component.apply(dataSet, pipelineData);
            if (pipelineData.getReport() != null && pipelineData.getReport().isLogReport()) {
                String componentName;
                if (component instanceof DescribableComponent) {
                    componentName = ((DescribableComponent) component).getDescription();
                } else {
                    componentName = component.getClass().getName();
                }
                logger.append("<h1>" + componentName + "</h1>");
                logger.append(ExportService.dataSetToHTMLTable(pipelineData.getReportItems(), dataSet, null, pipelineData.getInsightRequestMetadata()));
            }
        }
        return dataSet;
    }

    private DataSet resultSet;

    public DataSet getResultSet() {
        return resultSet;
    }

    /*
    class com.easyinsight.pipeline.AggregationComponent -
    [{Hours Cost=4.0, Hours=4.0, Project ID=1701498, Expenses - User ID=203116, Expenses Total Cost=500.0, User ID=203116, Time Tracking Project ID=1701498, Time Tracking User ID=203116, Expenses Project ID=1701498, User Default Hourly Rate=1.0, Project Name=Shivano Budgeted},
    {Hours Cost=20.0, Hours=20.0, Project ID=1701498, Expenses - User ID=203116, Expenses Total Cost=500.0, User ID=203116, Time Tracking Project ID=1701498, Time Tracking User ID=203116, Expenses Project ID=1701498, User Default Hourly Rate=1.0, Project Name=Shivano Budgeted},
    {Hours Cost=50.0, Hours=50.0, Project ID=1701498, Expenses - User ID=203116, Expenses Total Cost=500.0, User ID=203116, Time Tracking Project ID=1701498, Time Tracking User ID=203116, Expenses Project ID=1701498, User Default Hourly Rate=1.0, Project Name=Shivano Budgeted},
    {Hours Cost=5000.0, Hours=5.0, Project ID=1701498, Expenses - User ID=203116, Expenses Total Cost=500.0, User ID=203115, Time Tracking Project ID=1701498, Time Tracking User ID=203115, Expenses Project ID=1701498, User Default Hourly Rate=1000.0, Project Name=Shivano Budgeted}]
     */
    
    private StringBuilder logger = new StringBuilder();

    public DataResults toList(DataSet dataSet, EIConnection conn, Map<AnalysisItem, AnalysisItem> aliases) {
        for (IComponent component : components) {
            //System.out.println(component.getClass() + " - " + dataSet.getRows());
            /*if (pipelineData.getReport().isLogReport()) {
                logger.append("<h1>" + component.getClass().getName() + "</h1>");
                logger.append(ExportService.dataSetToHTMLTable(pipelineData.getReportItems(), dataSet, conn, pipelineData.getInsightRequestMetadata()));
            }*/
            long startTime = System.currentTimeMillis();
            dataSet = component.apply(dataSet, pipelineData);
            long endTime = System.currentTimeMillis();
            /*if (pipelineData.getReport().isLogReport()) {
                System.out.println(dataSet.getRows().size() + " - " + pipelineData.getReportItems().size() + " - " + component.getClass().getName() + " - " + (endTime - startTime));
            }*/
        }
        resultSet = dataSet;
        DataResults results = resultsBridge.toDataResults(dataSet, new ArrayList<AnalysisItem>(pipelineData.getAllRequestedItems()), aliases);
        for (IComponent component : components) {
            component.decorate(results);
        }
        return results;
    }
    
    public String toLogString() {
        return logger.toString();
    }
}
