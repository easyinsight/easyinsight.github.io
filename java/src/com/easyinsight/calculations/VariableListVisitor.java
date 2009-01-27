package com.easyinsight.calculations;

import com.easyinsight.core.Value;
import com.easyinsight.core.Key;

import java.util.Set;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Aug 3, 2008
 * Time: 10:14:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class VariableListVisitor implements ICalculationTreeVisitor {
    private Set<Key> variableList = new HashSet<Key>();

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
        variableList.add(node.getVariableKey());
    }

    public void visit(FunctionNode node) {
        visitChildren(node);
    }

    public Value getResult() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    public Set<Key> getVariableList() {
        return variableList;
    }

    private void visitChildren(CalculationTreeNode node) {

        for(int i = 0;i < node.getChildCount();i++) {
            VariableListVisitor v = new VariableListVisitor();
            if (node.getChild(i) instanceof CalculationTreeNode) {
                ((CalculationTreeNode) node.getChild(i)).accept(v);
            }
            variableList.addAll(v.getVariableList());
        }
    }
}
