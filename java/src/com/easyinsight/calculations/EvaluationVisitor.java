package com.easyinsight.calculations;

import com.easyinsight.IRow;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Key;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jun 28, 2008
 * Time: 9:04:29 PM
 */
public class EvaluationVisitor implements ICalculationTreeVisitor {
    public EvaluationVisitor() {
        result = null;
        row = null;
    }

    public EvaluationVisitor(IRow r) {
        row = r;
    }

    public void visit(CalculationTreeNode node) {

    }

    public void visit(NumberNode node) {
        result = node.getNumber();
    }

    public void visit(AddNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row);

        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        result = node1.getResult();
        if(node.getChildCount() == 2) {
            EvaluationVisitor node2 = new EvaluationVisitor(row);
            ((CalculationTreeNode) node.getChild(1)).accept(node2);
            result = new NumericValue(result.toDouble() + node2.getResult().toDouble());
        }
    }

    public void visit(SubtractNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row);

        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        result = node1.getResult();
        if(node.getChildCount() == 2) {
            EvaluationVisitor node2 = new EvaluationVisitor(row);
            ((CalculationTreeNode) node.getChild(1)).accept(node2);
            result = new NumericValue(result.toDouble() - node2.getResult().toDouble());
        }
        else {
            result = new NumericValue(-result.toDouble());
        }
    }

    public void visit(MultiplyNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);

        EvaluationVisitor node2 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);

        result = new NumericValue(node1.getResult().toDouble() * node2.getResult().toDouble());
    }

    public void visit(DivideNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
                
        EvaluationVisitor node2 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        result = new NumericValue(node1.getResult().toDouble() / node2.getResult().toDouble());
    }

    public void visit(ExponentNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        result = new NumericValue(Math.pow(node1.getResult().toDouble(), node2.getResult().toDouble()));
    }

    public void visit(VariableNode node) {
        result = row.getValue(node.getVariableKey());
    }

    public void visit(FunctionNode node) {
        IFunction f = node.getFunction();
        List<Value> params = new LinkedList<Value>();
        for(int i = 1;i < node.getChildCount();i++) {
            EvaluationVisitor subNode = new EvaluationVisitor(row);
            ((CalculationTreeNode) node.getChild(i)).accept(subNode);
            params.add(subNode.getResult());
        }
        f.setParameters(params);
        result = f.evaluate(); 
    }

    private Value result;

    public Value getResult() {
        return result;
    }

    private IRow row;
    private Map<Key, List<Value>> columnSlicedData;

    public void setRow(IRow r) {
        row = r;
    }
}
