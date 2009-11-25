package com.easyinsight.calculations;

import com.easyinsight.core.Value;
import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:36:20 PM
 */
public class ResolverVisitor implements ICalculationTreeVisitor {
    private Resolver variableResolver;
    private FunctionFactory functionResolver;
    private int aggregationType;

    public ResolverVisitor(Resolver r, FunctionFactory f) {
        variableResolver = r;
        functionResolver = f;
        aggregationType = 0;
    }

    public ResolverVisitor(Resolver r, FunctionFactory f, int aggregationType) {
        variableResolver = r;
        functionResolver = f;
        this.aggregationType = aggregationType;
    }


    private void visitChildren(CalculationTreeNode node) {
        for(int i = 0;i < node.getChildCount();i++) {
            if (node.getChild(i) instanceof CalculationTreeNode) {
                ((CalculationTreeNode) node.getChild(i)).accept(new ResolverVisitor(variableResolver, functionResolver));
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
            node.resolveVariableKey(variableResolver);
        else
            node.resolveVariableKey(variableResolver, aggregationType);
    }

    public void visit(FunctionNode node) {
        node.resolveFunction(functionResolver);
        if(node.getFunction() instanceof CastFunction) {
            CastFunction f = (CastFunction) node.getFunction();
            if(node.getChildCount() != 2 || !(node.getChild(1) instanceof VariableNode)) {
                throw new IllegalArgumentException();
            }
            else {
                ((CalculationTreeNode) node.getChild(1)).accept(new ResolverVisitor(variableResolver, functionResolver, f.getAggregationType()));
            }
        }
        else {
            for(int i = 1;i < node.getChildCount();i++) {
                ((CalculationTreeNode) node.getChild(i)).accept(new ResolverVisitor(variableResolver, functionResolver));
            }
        }
    }

    public Value getResult() {
        return null;
    }
}
