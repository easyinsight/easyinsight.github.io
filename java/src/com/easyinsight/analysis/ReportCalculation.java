package com.easyinsight.analysis;

import com.easyinsight.calculations.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.IJoin;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.Pipeline;
import com.easyinsight.pipeline.PipelineData;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataTransform;
import org.antlr.runtime.RecognitionException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 1:08 PM
 */
public class ReportCalculation {

    private String code;

    public ReportCalculation(String code) {
        this.code = code;
    }

    public static List<AnalysisItem> getAnalysisItems(String calculationString, List<AnalysisItem> allItems, Map<String, List<AnalysisItem>> keyMap,
                                                      Map<String, List<AnalysisItem>> displayMap, Map<String, List<AnalysisItem>> unqualifiedDisplayMap, Collection<AnalysisItem> insightItems,
                                                      boolean getEverything, boolean includeFilters, AnalysisItemRetrievalStructure structure) {
        CalculationTreeNode tree;

        ICalculationTreeVisitor visitor;


        try {
            tree = CalculationHelper.createTree(calculationString, false);

            visitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), structure.getNamespaceMap());
            tree.accept(visitor);
        } catch (FunctionException fe) {
            throw new ReportException(new AnalysisItemFault(fe.getMessage() + " in the calculation of " + calculationString + ".", null));
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            if ("org.antlr.runtime.tree.CommonErrorNode cannot be cast to com.easyinsight.calculations.CalculationTreeNode".equals(e.getMessage())) {
                throw new ReportException(new AnalysisItemFault("Syntax error in the calculation of " + calculationString + ".", null));
            }
            throw new ReportException(new AnalysisItemFault(e.getMessage() + " in the calculation of " + calculationString + ".", null));
        }

        VariableListVisitor variableVisitor = new VariableListVisitor();
        tree.accept(variableVisitor);

        List<AnalysisItem> analysisItemList = new ArrayList<AnalysisItem>();

        for (AnalysisItem analysisItem : variableVisitor.getVariableList()) {
            analysisItemList.addAll(analysisItem.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, new HashSet<AnalysisItem>(), structure));
        }

        return analysisItemList;
    }

    private DataSet createDataSet(List<AnalysisItem> allFields, Feed feed, List<FilterDefinition> dlsFilters, EIConnection conn, Map<String, List<AnalysisItem>> keyMap,
                                  Map<String, List<AnalysisItem>> displayMap, Map<String, List<AnalysisItem>> unqualifiedDisplayMap) throws RecognitionException {
        CalculationTreeNode calculationTreeNode;
        calculationTreeNode = CalculationHelper.createTree(code, false);
        ResolverVisitor resolverVisitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), new NamespaceGenerator().generate(feed.getFeedID(), null, conn));
        calculationTreeNode.accept(resolverVisitor);
        VariableListVisitor variableVisitor = new VariableListVisitor();
        calculationTreeNode.accept(variableVisitor);

        Set<AnalysisItem> analysisItemList = new HashSet<AnalysisItem>();



        for (AnalysisItem analysisItem : analysisItemList) {
            // TODO: clean up
            /*if (spec instanceof NamedKeySpecification) {
                NamedKeySpecification namedKeySpecification = (NamedKeySpecification) spec;
                if ("Persona".equals(namedKeySpecification.getKey())) {
                    continue;
                }
            }*/
            analysisItemList.addAll(analysisItem.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), true, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null)));

        }
        if (analysisItemList.size() > 0) {
            List<IComponent> filterComponents = new ArrayList<IComponent>();
            for (FilterDefinition filterDefinition : dlsFilters) {
                analysisItemList.add(filterDefinition.getField());
                filterComponents.addAll(filterDefinition.createComponents(Pipeline.AFTER, new DefaultFilterProcessor(), null, false));
            }
            DataSet set = feed.getAggregateDataSet(analysisItemList, dlsFilters, new InsightRequestMetadata(), feed.getFields(), false, conn);
            PipelineData pipelineData = new PipelineData(null, null, new InsightRequestMetadata(), null, null, null, null, null);
            for (IComponent component : filterComponents) {
                set = component.apply(set, pipelineData);
            }
            return set;
        } else {
            return null;
        }
    }

    public List<IJoin> applyJoinCalculation(List<FilterDefinition> filters, long dataSourceID) throws RecognitionException {
        CompositeCalculationMetadata compositeCalculationMetadata = new CompositeCalculationMetadata();
        compositeCalculationMetadata.setFilters(filters);
        ICalculationTreeVisitor visitor;
        CalculationTreeNode calculationTreeNode = CalculationHelper.createTree(code, false);
        visitor = new ResolverVisitor(new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(),
                new FunctionFactory(),
                new NamespaceGenerator().generate(dataSourceID, null, null));
        calculationTreeNode.accept(visitor);
        ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, compositeCalculationMetadata);
        calculationTreeNode.accept(rowVisitor);
        return compositeCalculationMetadata.getJoins();
    }

    public List<FilterDefinition> apply(Map<String, Object> data, List<AnalysisItem> analysisItems, WSAnalysisDefinition report,
                                        AnalysisItem field) throws RecognitionException, SQLException {
        DrillthroughCalculationMetadata drillthroughCalculationMetadata = new DrillthroughCalculationMetadata();
        drillthroughCalculationMetadata.setData(data);
        drillthroughCalculationMetadata.setField(field);
        drillthroughCalculationMetadata.setReport(report);
        FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(report.getDataFeedID());
        drillthroughCalculationMetadata.setDataSourceFields(dataSource.getFields());
        drillthroughCalculationMetadata.setAnalysisItems(analysisItems);
        ICalculationTreeVisitor visitor;
        CalculationTreeNode calculationTreeNode = CalculationHelper.createTree(code, false);

        visitor = new ResolverVisitor(new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(),
                new HashMap<String, List<AnalysisItem>>(), new FunctionFactory(),
                new NamespaceGenerator().generate(report.getDataFeedID(), null, null));
        calculationTreeNode.accept(visitor);
        ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, drillthroughCalculationMetadata);
        calculationTreeNode.accept(rowVisitor);
        return drillthroughCalculationMetadata.getDrillThroughFilters();
    }

    public List<IDataTransform> apply(FeedDefinition dataSource) throws SQLException {
        try {
            DataSourceCalculationMetadata dataSourceCalculationMetadata = new DataSourceCalculationMetadata();
            dataSourceCalculationMetadata.setDataSource(dataSource);
            CalculationTreeNode calculationTreeNode;
            ICalculationTreeVisitor visitor;
            calculationTreeNode = CalculationHelper.createTree(code, false);
            visitor = new ResolverVisitor(new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(),
                    new HashMap<String, List<AnalysisItem>>(), new FunctionFactory(),
                    new NamespaceGenerator().generate(dataSource.getDataFeedID(), null, null));
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, dataSourceCalculationMetadata);
            calculationTreeNode.accept(rowVisitor);
            return dataSourceCalculationMetadata.getTransforms();
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ActualRowLayoutItem> apply(List<AnalysisItem> analysisItems, long dataSourceID) throws SQLException {
        try {
            FormCalculationMetadata dataSourceCalculationMetadata = new FormCalculationMetadata();
            dataSourceCalculationMetadata.setAnalysisItemPool(analysisItems);
            CalculationTreeNode calculationTreeNode;
            ICalculationTreeVisitor visitor;
            calculationTreeNode = CalculationHelper.createTree(code, false);
            visitor = new ResolverVisitor(new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(),
                    new HashMap<String, List<AnalysisItem>>(), new FunctionFactory(),
                    new NamespaceGenerator().generate(dataSourceID, null, null));
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, dataSourceCalculationMetadata);
            calculationTreeNode.accept(rowVisitor);
            return dataSourceCalculationMetadata.getForms();
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public void apply(Dashboard dashboard, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap,
                      Map<String, List<AnalysisItem>> unqualifiedDisplayMap, Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters) throws SQLException {
        try {
            AnalysisDimension personaDimension = new AnalysisDimension();
            Key key = new NamedKey("Persona");
            personaDimension.setKey(key);
            keyMap.put("Persona", Arrays.<AnalysisItem>asList(personaDimension));
            List<AnalysisItem> myFields = new ArrayList<AnalysisItem>(allFields);
            myFields.add(personaDimension);

            DataSet dataSet = createDataSet(myFields, feed, dlsFilters, conn, keyMap, displayMap, unqualifiedDisplayMap);
            if (dataSet == null) {
                dataSet = new DataSet();
                dataSet.createRow();
            }
            PreparedStatement stmt = conn.prepareStatement("SELECT PERSONA.persona_name FROM USER, PERSONA WHERE USER.PERSONA_ID = PERSONA.PERSONA_ID AND USER.USER_ID = ?");
            stmt.setLong(1, SecurityUtil.getUserID());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String personaName = rs.getString(1);
                for (IRow row : dataSet.getRows()) {
                    row.addValue(personaDimension.createAggregateKey(), personaName);
                }
            }
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setDashboard(dashboard);
            calculationMetadata.setDataSet(dataSet);
            calculationMetadata.setConnection(conn);

            calculationMetadata.setDataSourceFields(myFields);
            CalculationTreeNode calculationTreeNode;
            ICalculationTreeVisitor visitor;
            calculationTreeNode = CalculationHelper.createTree(code, false);
            visitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), new NamespaceGenerator().generate(feed.getFeedID(), null, conn));
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public void apply(FilterDefinition filterDefinition, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap,
                      Map<String, List<AnalysisItem>> unqualifiedDisplayMap, Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters) {
        try {
            //DataSet dataSet = createDataSet(allFields, feed, dlsFilters, conn, keyMap, displayMap);
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setFilterDefinition(filterDefinition);
            //calculationMetadata.setDataSet(dataSet);
            calculationMetadata.setDataSourceFields(allFields);
            CalculationTreeNode calculationTreeNode;
            ICalculationTreeVisitor visitor;
            calculationTreeNode = CalculationHelper.createTree(code, false);
            visitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), new HashMap<String, UniqueKey>());
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public Value filterApply(WSAnalysisDefinition report, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap,
                             Map<String, List<AnalysisItem>> unqualifiedDisplayMap,
                             Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters, InsightRequestMetadata insightRequestMetadata, boolean shift) throws RecognitionException {
        //DataSet dataSet = createDataSet(allFields, feed, dlsFilters, conn, keyMap, displayMap);
        CalculationMetadata calculationMetadata = new CalculationMetadata();
        calculationMetadata.setFilterTimeShift(shift);
        calculationMetadata.setFeed(feed);
        calculationMetadata.setReport(report);
        calculationMetadata.setInsightRequestMetadata(insightRequestMetadata);
        calculationMetadata.setConnection(conn);
        if (feed != null) {
            calculationMetadata.setDataSource(feed.getDataSource());
        }
        Collection<FilterDefinition> allFilters = new ArrayList<FilterDefinition>();
        if (report != null && report.getFilterDefinitions() != null) {
            allFilters.addAll(report.getFilterDefinitions());
        }
        if (dlsFilters != null) {
            allFilters.addAll(dlsFilters);
        }
        calculationMetadata.setFilters(allFilters);
        //calculationMetadata.setDataSet(dataSet);
        calculationMetadata.setDataSourceFields(allFields);
        CalculationTreeNode calculationTreeNode;
        ICalculationTreeVisitor visitor;
        calculationTreeNode = CalculationHelper.createTree(code, false);
        visitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), new NamespaceGenerator().generate(feed != null ? feed.getFeedID() : 0, null, conn));
        calculationTreeNode.accept(visitor);
        ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, calculationMetadata);
        calculationTreeNode.accept(rowVisitor);
        return rowVisitor.getResult();
    }

    public void apply(WSAnalysisDefinition report, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap,
                      Map<String, List<AnalysisItem>> unqualifiedDisplayMap, Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters, InsightRequestMetadata insightRequestMetadata) throws RecognitionException {
            //DataSet dataSet = createDataSet(allFields, feed, dlsFilters, conn, keyMap, displayMap);
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setFeed(feed);
            calculationMetadata.setReport(report);
            calculationMetadata.setInsightRequestMetadata(insightRequestMetadata);
            calculationMetadata.setConnection(conn);
        if (feed != null) {
                calculationMetadata.setDataSource(feed.getDataSource());
        }
            Collection<FilterDefinition> allFilters = new ArrayList<FilterDefinition>();
            if (report.getFilterDefinitions() != null) {
                allFilters.addAll(report.getFilterDefinitions());
            }
            if (dlsFilters != null) {
                allFilters.addAll(dlsFilters);
            }
            calculationMetadata.setFilters(allFilters);
            //calculationMetadata.setDataSet(dataSet);
            calculationMetadata.setDataSourceFields(allFields);
            CalculationTreeNode calculationTreeNode;
            ICalculationTreeVisitor visitor;
            calculationTreeNode = CalculationHelper.createTree(code, false);



            visitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), new NamespaceGenerator().generate(report.getDataFeedID(), report.getAddonReports(), conn));
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
    }

    public void applyAfterReport(WSAnalysisDefinition report, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap,
                                 Map<String, List<AnalysisItem>> unqualifiedDisplayMap, IRow row, Map<String, UniqueKey> namespaceMap,
                                 CalculationMetadata calculationMetadata) throws RecognitionException {



            CalculationTreeNode calculationTreeNode;
            ICalculationTreeVisitor visitor;
            calculationTreeNode = CalculationHelper.createTree(code, false);

            visitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), namespaceMap);
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(row, null, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
    }
}
