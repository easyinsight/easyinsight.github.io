package com.easyinsight.analysis;

import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.IJoin;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.CleanupComponent;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.PipelineData;
import com.easyinsight.security.SecurityUtil;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
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
                                                      Map<String, List<AnalysisItem>> displayMap, Collection<AnalysisItem> insightItems,
                                                      boolean getEverything, boolean includeFilters, int criteria) {
        CalculationTreeNode tree;
        Set<KeySpecification> specs;

        ICalculationTreeVisitor visitor;
        CalculationsParser.startExpr_return ret;

        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(calculationString));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());

        try {
            ret = parser.startExpr();
            tree = (CalculationTreeNode) ret.getTree();

            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
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

        specs = variableVisitor.getVariableList();


        List<AnalysisItem> analysisItemList = new ArrayList<AnalysisItem>();

        for (KeySpecification spec : specs) {
            AnalysisItem analysisItem;
            try {
                analysisItem = spec.findAnalysisItem(keyMap, displayMap);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            if (analysisItem != null) {
                analysisItemList.addAll(analysisItem.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria, new HashSet<AnalysisItem>()));
            }
        }

        return analysisItemList;
    }

    private DataSet createDataSet(List<AnalysisItem> allFields, Feed feed, List<FilterDefinition> dlsFilters, EIConnection conn, Map<String, List<AnalysisItem>> keyMap,
                                  Map<String, List<AnalysisItem>> displayMap) throws RecognitionException {
        CalculationTreeNode calculationTreeNode;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(code));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        ret = parser.expr();
        calculationTreeNode = (CalculationTreeNode) ret.getTree();
        for (int i = 0; i < calculationTreeNode.getChildCount(); i++) {
            if (!(calculationTreeNode.getChild(i) instanceof CalculationTreeNode)) {
                calculationTreeNode.deleteChild(i);
                break;
            }
        }
        ResolverVisitor resolverVisitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
        calculationTreeNode.accept(resolverVisitor);
        VariableListVisitor variableVisitor = new VariableListVisitor();
        calculationTreeNode.accept(variableVisitor);

        Set<KeySpecification> specs = variableVisitor.getVariableList();
        Set<AnalysisItem> analysisItemList = new HashSet<AnalysisItem>();
        for (KeySpecification spec : specs) {
            if (spec instanceof NamedKeySpecification) {
                NamedKeySpecification namedKeySpecification = (NamedKeySpecification) spec;
                if ("Persona".equals(namedKeySpecification.getKey())) {
                    continue;
                }
            }
            AnalysisItem analysisItem;
            try {
                analysisItem = spec.findAnalysisItem(keyMap, displayMap);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            if (analysisItem != null) {
                analysisItemList.addAll(analysisItem.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), true, true, CleanupComponent.AGGREGATE_CALCULATIONS, new HashSet<AnalysisItem>()));
            }
        }
        if (analysisItemList.size() > 0) {
            List<IComponent> filterComponents = new ArrayList<IComponent>();
            for (FilterDefinition filterDefinition : dlsFilters) {
                analysisItemList.add(filterDefinition.getField());
                filterComponents.addAll(filterDefinition.createComponents(true, new DefaultFilterProcessor(), null, false));
            }
            DataSet set = feed.getAggregateDataSet(analysisItemList, dlsFilters, new InsightRequestMetadata(), feed.getFields(), false, conn);
            PipelineData pipelineData = new PipelineData(null, null, new InsightRequestMetadata(), null, null, null, null);
            for (IComponent component : filterComponents) {
                set = component.apply(set, pipelineData);
            }
            return set;
        } else {
            return null;
        }
    }

    public List<IJoin> applyJoinCalculation() throws RecognitionException {
        CompositeCalculationMetadata compositeCalculationMetadata = new CompositeCalculationMetadata();
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(code));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        ret = parser.expr();
        CalculationTreeNode calculationTreeNode = (CalculationTreeNode) ret.getTree();
        for (int i = 0; i < calculationTreeNode.getChildCount(); i++) {
            if (!(calculationTreeNode.getChild(i) instanceof CalculationTreeNode)) {
                calculationTreeNode.deleteChild(i);
                break;
            }
        }
        visitor = new ResolverVisitor(new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(), new FunctionFactory());
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
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(code));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        ret = parser.expr();
        CalculationTreeNode calculationTreeNode = (CalculationTreeNode) ret.getTree();
        for (int i = 0; i < calculationTreeNode.getChildCount(); i++) {
            if (!(calculationTreeNode.getChild(i) instanceof CalculationTreeNode)) {
                calculationTreeNode.deleteChild(i);
                break;
            }
        }
        visitor = new ResolverVisitor(new HashMap<String, List<AnalysisItem>>(), new HashMap<String, List<AnalysisItem>>(), new FunctionFactory());
        calculationTreeNode.accept(visitor);
        ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, drillthroughCalculationMetadata);
        calculationTreeNode.accept(rowVisitor);
        return drillthroughCalculationMetadata.getDrillThroughFilters();
    }

    public void apply(Dashboard dashboard, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap, Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters) throws SQLException {
        try {
            AnalysisDimension personaDimension = new AnalysisDimension();
            Key key = new NamedKey("Persona");
            personaDimension.setKey(key);
            keyMap.put("Persona", Arrays.<AnalysisItem>asList(personaDimension));
            List<AnalysisItem> myFields = new ArrayList<AnalysisItem>(allFields);
            myFields.add(personaDimension);

            DataSet dataSet = createDataSet(myFields, feed, dlsFilters, conn, keyMap, displayMap);
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
            CalculationsParser.expr_return ret;
            CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(code));
            CommonTokenStream tokes = new CommonTokenStream();
            tokes.setTokenSource(lexer);
            CalculationsParser parser = new CalculationsParser(tokes);
            parser.setTreeAdaptor(new NodeFactory());
            ret = parser.expr();
            calculationTreeNode = (CalculationTreeNode) ret.getTree();
            for (int i = 0; i < calculationTreeNode.getChildCount(); i++) {
                if (!(calculationTreeNode.getChild(i) instanceof CalculationTreeNode)) {
                    calculationTreeNode.deleteChild(i);
                    break;
                }
            }
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public void apply(FilterDefinition filterDefinition, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap, Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters) {
        try {
            //DataSet dataSet = createDataSet(allFields, feed, dlsFilters, conn, keyMap, displayMap);
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setFilterDefinition(filterDefinition);
            //calculationMetadata.setDataSet(dataSet);
            calculationMetadata.setDataSourceFields(allFields);
            CalculationTreeNode calculationTreeNode;
            ICalculationTreeVisitor visitor;
            CalculationsParser.expr_return ret;
            CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(code));
            CommonTokenStream tokes = new CommonTokenStream();
            tokes.setTokenSource(lexer);
            CalculationsParser parser = new CalculationsParser(tokes);
            parser.setTreeAdaptor(new NodeFactory());
            ret = parser.expr();
            calculationTreeNode = (CalculationTreeNode) ret.getTree();
            for (int i = 0; i < calculationTreeNode.getChildCount(); i++) {
                if (!(calculationTreeNode.getChild(i) instanceof CalculationTreeNode)) {
                    calculationTreeNode.deleteChild(i);
                    break;
                }
            }
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public void apply(WSAnalysisDefinition report, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap,
                      Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters) {
        try {
            //DataSet dataSet = createDataSet(allFields, feed, dlsFilters, conn, keyMap, displayMap);
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setReport(report);
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
            CalculationsParser.expr_return ret;
            CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(code));
            CommonTokenStream tokes = new CommonTokenStream();
            tokes.setTokenSource(lexer);
            CalculationsParser parser = new CalculationsParser(tokes);
            parser.setTreeAdaptor(new NodeFactory());
            ret = parser.expr();
            calculationTreeNode = (CalculationTreeNode) ret.getTree();
            for (int i = 0; i < calculationTreeNode.getChildCount(); i++) {
                if (!(calculationTreeNode.getChild(i) instanceof CalculationTreeNode)) {
                    calculationTreeNode.deleteChild(i);
                    break;
                }
            }
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(null, null, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    public void applyAfterReport(WSAnalysisDefinition report, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap, IRow row) {
        try {
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setReport(report);
            calculationMetadata.setDataSourceFields(allFields);
            CalculationTreeNode calculationTreeNode;
            ICalculationTreeVisitor visitor;
            CalculationsParser.expr_return ret;
            CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(code));
            CommonTokenStream tokes = new CommonTokenStream();
            tokes.setTokenSource(lexer);
            CalculationsParser parser = new CalculationsParser(tokes);
            parser.setTreeAdaptor(new NodeFactory());
            ret = parser.expr();
            calculationTreeNode = (CalculationTreeNode) ret.getTree();
            for (int i = 0; i < calculationTreeNode.getChildCount(); i++) {
                if (!(calculationTreeNode.getChild(i) instanceof CalculationTreeNode)) {
                    calculationTreeNode.deleteChild(i);
                    break;
                }
            }
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
            calculationTreeNode.accept(visitor);
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(row, null, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
    }
}
