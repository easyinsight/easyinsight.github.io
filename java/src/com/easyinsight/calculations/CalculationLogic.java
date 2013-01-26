package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.logging.LogClass;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

import java.util.*;

/**
 * User: jamesboe
 * Date: 10/6/11
 * Time: 12:40 PM
 */
public class CalculationLogic {
    public void calculate(String code, WSAnalysisDefinition report, Collection<AnalysisItem> allItems, InsightRequestMetadata insightRequestMetadata) {
        CalculationMetadata calculationMetadata = new CalculationMetadata();
        calculationMetadata.setReport(report);
        calculationMetadata.setInsightRequestMetadata(insightRequestMetadata);
        calculationMetadata.setDataSourceFields(allItems);
        CalculationTreeNode calculationTreeNode;
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(code));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
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
            KeyDisplayMapper mapper = KeyDisplayMapper.create(allItems);
            Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
            Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();
            if (report != null && report.getFilterDefinitions() != null) {
                for (FilterDefinition filter : report.getFilterDefinitions()) {
                    filter.calculationItems(displayMap);
                }
            }
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
            calculationTreeNode.accept(visitor);

            calculateResults(calculationTreeNode, calculationMetadata);

        } catch (FunctionException fe) {
            throw new ReportException(new AnalysisItemFault(fe.getMessage() + " in the calculation of " + code + ".", null));
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            if ("org.antlr.runtime.tree.CommonErrorNode cannot be cast to com.easyinsight.calculations.CalculationTreeNode".equals(e.getMessage())) {
                throw new ReportException(new AnalysisItemFault("Syntax error in the calculation of " + code + ".", null));
            }
            LogClass.error("On calculating " + code, e);
            throw new ReportException(new AnalysisItemFault(e.getMessage() + " in the calculation of " + code, null));
        }
    }

    protected void calculateResults(CalculationTreeNode calculationTreeNode, CalculationMetadata calculationMetadata) {

    }
}
