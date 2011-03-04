package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

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
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(dimension.getDerivationCode()));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        try {
            ret = parser.expr();
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
        CalculationTreeNode calculationTreeNode = (CalculationTreeNode) ret.getTree();
        for (int i = 0; i < calculationTreeNode.getChildCount();i++) {
            if (!(calculationTreeNode.getChild(i) instanceof CalculationTreeNode)) {
                calculationTreeNode.deleteChild(i);
                break;
            }
        }
        visitor = new ResolverVisitor(pipelineData.getAllItems(), new FunctionFactory());
        calculationTreeNode.accept(visitor);
        for (IRow row : dataSet.getRows()) {
            Value value = dimension.calculate(row, calculationTreeNode);
            row.addValue(dimension.createAggregateKey(), value);
        }
        pipelineData.getReportItems().add(dimension);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}