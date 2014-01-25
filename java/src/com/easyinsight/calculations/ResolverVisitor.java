package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.UniqueKey;
import com.easyinsight.calculations.functions.CastFunction;
import com.easyinsight.core.Value;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:36:20 PM
 */
public class ResolverVisitor implements ICalculationTreeVisitor {
    private Resolver variableResolver;
    //private List<AnalysisItem> analysisItems;
    private Map<String, List<AnalysisItem>> keyItems;
    private Map<String, List<AnalysisItem>> displayItems;
    private Map<String, List<AnalysisItem>> unqualifiedDisplayItems;
    private Map<String, UniqueKey> uniqueKeyMap;
    private FunctionFactory functionResolver;
    private int aggregationType;
    private Collection<String> warnings = new LinkedHashSet<String>();

    public ResolverVisitor(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems, Map<String, List<AnalysisItem>> unqualifiedDisplayItems,
                           FunctionFactory f, Map<String, UniqueKey> uniqueKeyMap) {
        this.keyItems = keyItems;
        this.displayItems = displayItems;
        this.uniqueKeyMap = uniqueKeyMap;
        this.unqualifiedDisplayItems = unqualifiedDisplayItems;
        functionResolver = f;
        aggregationType = 0;
    }

    public ResolverVisitor(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems, Map<String, List<AnalysisItem>> unqualifiedDisplayItems,
                           FunctionFactory f, Map<String, UniqueKey> uniqueKeyMap, Collection<String> warnings) {
        this.keyItems = keyItems;
        this.displayItems = displayItems;
        this.uniqueKeyMap = uniqueKeyMap;
        this.unqualifiedDisplayItems = unqualifiedDisplayItems;
        functionResolver = f;
        aggregationType = 0;
        this.warnings = warnings;
    }

    public ResolverVisitor(Map<String, List<AnalysisItem>> keyItems, Map<String, List<AnalysisItem>> displayItems,
                           Map<String, List<AnalysisItem>> unqualifiedDisplayItems, FunctionFactory f, int aggregationType) {
        this.keyItems = keyItems;
        this.displayItems = displayItems;
        this.unqualifiedDisplayItems = unqualifiedDisplayItems;
        functionResolver = f;
        this.aggregationType = aggregationType;
    }


    private void visitChildren(CalculationTreeNode node) {
        for(int i = 0;i < node.getChildCount();i++) {
            if (node.getChild(i) instanceof CalculationTreeNode) {
                ((CalculationTreeNode) node.getChild(i)).accept(new ResolverVisitor(keyItems, displayItems, unqualifiedDisplayItems, functionResolver, uniqueKeyMap, warnings));
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

    public void visit(EqualsNode node) {
        visitChildren(node);
    }

    public void visit(NotEqualsNode node) {
        visitChildren(node);
    }

    public void visit(GreaterThanNode node) {
        visitChildren(node);
    }

    public void visit(GreaterThanEqualToNode node) {
        visitChildren(node);
    }

    public void visit(LessThanNode node) {
        visitChildren(node);
    }

    public void visit(LessThanEqualToNode node) {
        visitChildren(node);
    }

    public void visit(AndNode node) {
        visitChildren(node);
    }

    public void visit(OrNode node) {
        visitChildren(node);
    }

    public void visit(NotNode node) {
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
            node.resolveVariableKey(keyItems, displayItems, unqualifiedDisplayItems, uniqueKeyMap);
        else
            node.resolveVariableKey(keyItems, displayItems, aggregationType);
        if (node.getWarnings() != null) {
            warnings.addAll(node.getWarnings());
        }
    }

    public Collection<String> getWarnings() {
        return warnings;
    }

    public void visit(FunctionNode node) {
        node.resolveFunction(functionResolver);
        if (node.getFunction() == null) {
            throw new FunctionException("We couldn't resolve function " + node.getChild(0).getText().trim() + ".");
        }
        if(node.getFunction() instanceof CastFunction) {
            CastFunction f = (CastFunction) node.getFunction();
            if(node.getChildCount() != 2 || !(node.getChild(1) instanceof VariableNode)) {
                throw new FunctionException("Incorrect number of parameters passed to cast function.");
            }
            else {
                ((CalculationTreeNode) node.getChild(1)).accept(new ResolverVisitor(keyItems, displayItems, unqualifiedDisplayItems, functionResolver, f.getAggregationType()));
            }
        }
        else {
            int parameters = node.getFunction().getParameterCount();
            if (parameters != -1 && parameters != node.getChildCount() - 1) {
                throw new FunctionException(node.getChild(0).toString() + " requires " + parameters + " parameters.");
            }
            for(int i = 1;i < node.getChildCount();i++) {
                ((CalculationTreeNode) node.getChild(i)).accept(new ResolverVisitor(keyItems, displayItems, unqualifiedDisplayItems, functionResolver, uniqueKeyMap));
            }
        }
    }

    public Value getResult() {
        return null;
    }
}
