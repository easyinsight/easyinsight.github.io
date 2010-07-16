package com.easyinsight.calculations;

import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.EmptyValue;

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
            if(!(node2.getResult() instanceof EmptyValue) && node1.getResult().toDouble() != null && node2.getResult().toDouble() != null)
                result = new NumericValue(result.toDouble() + node2.getResult().toDouble());
            else
                result = new EmptyValue();
        }
    }

    public void visit(SubtractNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row);

        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        result = node1.getResult();
        if(node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null) {
            result = new EmptyValue();
            return;
        }
        if(node.getChildCount() == 2) {
            EvaluationVisitor node2 = new EvaluationVisitor(row);
            ((CalculationTreeNode) node.getChild(1)).accept(node2);
            if(node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
                result = new EmptyValue();
            }
            else {
                result = new NumericValue(result.toDouble() - node2.getResult().toDouble());
            }
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
        if(node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null || node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
            result = new EmptyValue();
        }
        else {
            result = new NumericValue(node1.getResult().toDouble() * node2.getResult().toDouble());
        }
    }

    public void visit(DivideNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
                
        EvaluationVisitor node2 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if(node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null || node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
            result = new EmptyValue();
        }
        else {
            result = new NumericValue(node1.getResult().toDouble() / node2.getResult().toDouble());
        }
    }

    public void visit(ExponentNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if(node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null || node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
            result = new EmptyValue();
        }
        else {
            result = new NumericValue(Math.pow(node1.getResult().toDouble(), node2.getResult().toDouble()));
        }
    }

    public void visit(VariableNode node) {
        result = row.getValue(node.createAggregateKey());
    }

    public void visit(FunctionNode node) {
        IFunction f = node.getFunction();
        List<Value> params = new LinkedList<Value>();
        for(int i = 1;i < node.getChildCount();i++) {
            EvaluationVisitor subNode = new EvaluationVisitor(row);
            ((CalculationTreeNode) node.getChild(i)).accept(subNode);
            // TODO: Better handling of empty values in functions
            if(subNode.getResult() instanceof EmptyValue) {
                result = new EmptyValue();
                return;
            }
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
