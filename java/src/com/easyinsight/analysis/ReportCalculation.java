package com.easyinsight.analysis;

import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.CleanupComponent;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.PipelineData;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

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
        for (int i = 0; i < calculationTreeNode.getChildCount();i++) {
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
            AnalysisItem analysisItem;
            try {
                analysisItem = spec.findAnalysisItem(keyMap, displayMap);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            if (analysisItem != null) {
                analysisItemList.addAll(analysisItem.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), true, true, CleanupComponent.AGGREGATE_CALCULATIONS));
            }
        }
        if (analysisItemList.size() > 0) {
            List<IComponent> filterComponents = new ArrayList<IComponent>();
            for (FilterDefinition filterDefinition : dlsFilters) {
                analysisItemList.add(filterDefinition.getField());
                filterComponents.addAll(filterDefinition.createComponents(true, new DefaultFilterProcessor(), null, false));
            }
            DataSet set = feed.getAggregateDataSet(analysisItemList, dlsFilters, new InsightRequestMetadata(), feed.getFields(), false, conn);
            PipelineData pipelineData = new PipelineData(null, null, new InsightRequestMetadata(), null, null, null);
            for (IComponent component : filterComponents) {
                set = component.apply(set, pipelineData);
            }
            return set;
        } else {
            return null;
        }
    }

    public void apply(FilterDefinition filterDefinition, List<AnalysisItem> allFields, Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters) {
        try {
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
            DataSet dataSet = createDataSet(allFields, feed, dlsFilters, conn, keyMap,  displayMap);
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setFilterDefinition(filterDefinition);
            calculationMetadata.setDataSet(dataSet);
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
            for (int i = 0; i < calculationTreeNode.getChildCount();i++) {
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

    public void apply(WSAnalysisDefinition report, List<AnalysisItem> allFields, Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters) {
        try {
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
            DataSet dataSet = createDataSet(allFields, feed, dlsFilters, conn, keyMap,  displayMap);
            CalculationMetadata calculationMetadata = new CalculationMetadata();
            calculationMetadata.setReport(report);
            calculationMetadata.setDataSet(dataSet);
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
            for (int i = 0; i < calculationTreeNode.getChildCount();i++) {
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
}
