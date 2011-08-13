package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

/**
 * User: jamesboe
 * Date: Nov 23, 2009
 * Time: 11:25:37 AM
 */
public class DerivedGroupingComponent implements IComponent {

    private DerivedAnalysisDimension dimension;

    public DerivedGroupingComponent(DerivedAnalysisDimension dimension) {
        this.dimension = dimension;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (dataSet.getRows().size() == 0) {
            return dataSet;
        }
        CalculationTreeNode calculationTreeNode;
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(dimension.getDerivationCode()));
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
            visitor = new ResolverVisitor(pipelineData.getAllItems(), new FunctionFactory());
            calculationTreeNode.accept(visitor);

            for (IRow row : dataSet.getRows()) {
                ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(row, dimension);
                calculationTreeNode.accept(rowVisitor);
                Value value = new StringValue(rowVisitor.getResult().toString());
                row.addValue(dimension.createAggregateKey(), value);
            }

        } catch (FunctionException fe) {
            throw new ReportException(new AnalysisItemFault(fe.getMessage() + " in the calculation of " + dimension.getDerivationCode() + ".", dimension));
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + " in calculating " + dimension.getDerivationCode(), e);
        }
        pipelineData.getReportItems().add(dimension);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}