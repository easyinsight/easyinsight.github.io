package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Value;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:36:20 PM
 */
public class ResolverVisitor implements ICalculationTreeVisitor {
    private Resolver variableResolver;
    private Collection<AnalysisItem> analysisItems;
    private FunctionFactory functionResolver;
    private int aggregationType;

    public ResolverVisitor(Collection<AnalysisItem> analysisItems, FunctionFactory f) {
        this.analysisItems = analysisItems;
        functionResolver = f;
        aggregationType = 0;
    }

    public ResolverVisitor(Collection<AnalysisItem> analysisItems, FunctionFactory f, int aggregationType) {
        this.analysisItems = analysisItems;
        functionResolver = f;
        this.aggregationType = aggregationType;
    }


    private void visitChildren(CalculationTreeNode node) {
        for(int i = 0;i < node.getChildCount();i++) {
            if (node.getChild(i) instanceof CalculationTreeNode) {
                ((CalculationTreeNode) node.getChild(i)).accept(new ResolverVisitor(analysisItems, functionResolver));
            }
        }
    }

    public void visit(CalculationTreeNode node) {
        visitChildren(node);
    }

    public void visit(NumberNode node) {
        visitChildren(node);
    }

    public void visit(AddNode node) {
        visitChildren(node);
    }

    public void visit(StringNode node) {
        visitChildren(node);
    }

    public void visit(SubtractNode node) {
        visitChildren(node);
    }

    public void visit(MultiplyNode node) {
        visitChildren(node);
    }

    public void visit(DivideNode node) {
        visitChildren(node);
    }

    public void visit(ExponentNode node) {
        visitChildren(node);
    }

    public void visit(VariableNode node) {
        if(aggregationType == 0)
            node.resolveVariableKey(analysisItems);
        else
            node.resolveVariableKey(analysisItems, aggregationType);
    }

    public void visit(FunctionNode node) {
        node.resolveFunction(functionResolver);
        if(node.getFunction() instanceof CastFunction) {
            CastFunction f = (CastFunction) node.getFunction();
            if(node.getChildCount() != 2 || !(node.getChild(1) instanceof VariableNode)) {
                throw new IllegalArgumentException();
            }
            else {
                ((CalculationTreeNode) node.getChild(1)).accept(new ResolverVisitor(analysisItems, functionResolver, f.getAggregationType()));
            }
        }
        else {
            int parameters = node.getFunction().getParameterCount();
            if (parameters != -1 && parameters != node.getChildCount() - 1) {
                throw new RuntimeException(node.getChild(0).toString() + " requires " + parameters + " parameters. ");
            }
            for(int i = 1;i < node.getChildCount();i++) {
                ((CalculationTreeNode) node.getChild(i)).accept(new ResolverVisitor(analysisItems, functionResolver));
            }
        }
    }

    public Value getResult() {
        return null;
    }
}
