package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Nov 23, 2009
 * Time: 11:25:37 AM
 */
public class CalculationComponent implements IComponent {

    private AnalysisCalculation analysisCalculation;

    public CalculationComponent(AnalysisCalculation analysisCalculation) {
        this.analysisCalculation = analysisCalculation;
    }

    public AnalysisCalculation getAnalysisCalculation() {
        return analysisCalculation;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (dataSet.getRows().size() == 0) {
            return dataSet;
        }
        CalculationTreeNode calculationTreeNode;
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(analysisCalculation.getCalculationString()));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        Resolver r = new Resolver();

        for (Key key : dataSet.getRow(0).getKeys()) {
            r.addKey(key);
        }
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        try {
            ret = parser.expr();
            calculationTreeNode = (CalculationTreeNode) ret.getTree();
            for (int i = 0; i < calculationTreeNode.getChildCount();i++) {
                if (!(calculationTreeNode.getChild(i) instanceof CalculationTreeNode)) {
                    calculationTreeNode.deleteChild(i);
                    break;
                }
            }
            Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : pipelineData.getAllItems()) {
                List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    keyMap.put(analysisItem.getKey().toKeyString(), items);
                }
                items.add(analysisItem);
            }
            Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : pipelineData.getAllItems()) {
                List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    displayMap.put(analysisItem.toDisplay(), items);
                }
                items.add(analysisItem);
            }
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
            calculationTreeNode.accept(visitor);
            for (IRow row : dataSet.getRows()) {
                ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(row, analysisCalculation);
                calculationTreeNode.accept(rowVisitor);
                Value result = rowVisitor.getResult();
                if (result.type() == Value.EMPTY) {

                } else if (result.type() == Value.NUMBER) {

                } else {
                    result = new NumericValue(result.toDouble());
                }
                row.addValue(analysisCalculation.createAggregateKey(), result);
            }
        } catch (FunctionException fe) {
            throw new ReportException(new AnalysisItemFault(fe.getMessage(), analysisCalculation));
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + " in calculating " + analysisCalculation.getCalculationString(), e);
        }
        pipelineData.getReportItems().add(analysisCalculation);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
