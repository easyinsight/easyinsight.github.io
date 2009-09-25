package com.easyinsight.calculations;

import com.easyinsight.core.Value;
import org.antlr.runtime.tree.CommonErrorNode;

import java.util.List;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Sep 20, 2009
 * Time: 12:04:36 PM
 */
public class ValidationVisitor implements ICalculationTreeVisitor {

    private List<String> errors = new LinkedList<String>();
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
        visitChildren(node);
    }

    public void visit(FunctionNode node) {
        visitChildren(node);
    }

    public Value getResult() {
        return null;
    }

    private void visitChildren(CalculationTreeNode node) {
        for(int i = 0;i < node.getChildCount();i++) {
            VariableListVisitor v = new VariableListVisitor();
            if (node.getChild(i) instanceof CalculationTreeNode) {
                ((CalculationTreeNode) node.getChild(i)).accept(v);
            } else if(node.getChild(i) instanceof CommonErrorNode) {
                errors.add(((CommonErrorNode)node.getChild(i)).trappedException.toString());
            }
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
