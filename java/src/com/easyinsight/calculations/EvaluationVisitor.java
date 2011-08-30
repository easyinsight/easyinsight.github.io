package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;

import java.util.Date;
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

    private boolean emptyAsZero = false;

    public EvaluationVisitor(IRow r, AnalysisItem analysisItem) {
        row = r;
        this.analysisItem = analysisItem;
        if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
            AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
            emptyAsZero = analysisCalculation.getFormattingConfiguration().getFormattingType() != FormattingConfiguration.MILLISECONDS &&
                    analysisCalculation.getFormattingConfiguration().getFormattingType() != FormattingConfiguration.SECONDS;
        }
    }

    public void visit(CalculationTreeNode node) {

    }

    public void visit(NumberNode node) {
        result = node.getNumber();
    }

    public void visit(StringNode node) {
        result = node.getString();
    }

    public void visit(AddNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem);

        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        result = node1.getResult();
        if(node.getChildCount() == 2) {
            EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem);
            ((CalculationTreeNode) node.getChild(1)).accept(node2);
            Value result2 = node2.getResult();
            if (result.type() == Value.STRING || result2.type() == Value.STRING) {
                result = new StringValue(minusQuotes(result) + minusQuotes(result2));
            } else if (node1.getResult().type() == Value.DATE && node2.getResult().type() == Value.NUMBER ||
                    node1.getResult().type() == Value.NUMBER && node2.getResult().type() == Value.DATE ||
                    node1.getResult().type() == Value.DATE && node2.getResult().type() == Value.DATE) {
                result = new DateValue(new Date((long) (node1.getResult().toDouble() + node2.getResult().toDouble())));
            } else if (!(node2.getResult() instanceof EmptyValue) && node1.getResult().toDouble() != null && node2.getResult().toDouble() != null) {
                result = new NumericValue(result.toDouble() + node2.getResult().toDouble());
            } else {
                if (emptyAsZero) {
                    result = new NumericValue(result.toDouble() + node2.getResult().toDouble());
                } else {
                    result = new EmptyValue();
                }
            }
        }
    }

    public String minusQuotes(Value value) {
        String string = value.toString();
        if (string.length() >= 2) {
            char first = string.charAt(0);
            char end = string.charAt(string.length() - 1);
            if (first == '"' && end == '"') {
                if (string.length() == 2) {
                    return "";
                }
                return string.substring(1, string.length() - 1);
            }
        }
        return string;
    }

    public void visit(SubtractNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem);

        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        result = node1.getResult();
        
        if(node1.getResult() instanceof EmptyValue) {
            result = new EmptyValue();
            return;
        }
        if(node.getChildCount() == 2) {
            EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem);
            ((CalculationTreeNode) node.getChild(1)).accept(node2);
            Value result2 = node2.getResult();
            if (result.type() == Value.NUMBER && result2.type() == Value.NUMBER) {
                result = new NumericValue(result.toDouble() - result2.toDouble());
            } else if (result.type() == Value.DATE && result2.type() == Value.NUMBER) {
                DateValue dateValue = (DateValue) result;
                long time = dateValue.getDate().getTime();
                long delta = (long) (time - result2.toDouble());
                result = new NumericValue(delta);
            } else if (result.type() == Value.DATE && result2.type() == Value.DATE) {
                DateValue dateValue = (DateValue) result;
                DateValue dateValue2 = (DateValue) result2;
                long delta = dateValue.getDate().getTime() - dateValue2.getDate().getTime();
                result = new NumericValue(delta);
            } else if (result.type() == Value.NUMBER && result2.type() == Value.DATE) {
                DateValue dateValue = (DateValue) result2;
                long delta = (long) (result.toDouble() - dateValue.getDate().getTime());
                result = new NumericValue(delta);
            } else if(node2.getResult().type() == Value.EMPTY || node1.getResult().type() == Value.EMPTY) {
                if (emptyAsZero) {
                    result = new NumericValue(result.toDouble() - node2.getResult().toDouble());
                } else {
                    result = new EmptyValue();
                }
            } else {
                result = new NumericValue(result.toDouble() - node2.getResult().toDouble());
            }
        }
        else {
            result = new NumericValue(-result.toDouble());
        }
    }

    public void visit(MultiplyNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);

        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if(node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null || node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
            if (emptyAsZero) {
                result = new NumericValue(node1.getResult().toDouble() * node2.getResult().toDouble());
            } else {
                result = new EmptyValue();
            }
        }
        else {
            result = new NumericValue(node1.getResult().toDouble() * node2.getResult().toDouble());
        }
    }

    public void visit(DivideNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);

        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if(node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null || node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
            if (emptyAsZero) {
                result = new NumericValue(node1.getResult().toDouble() / node2.getResult().toDouble());
            } else {
                result = new EmptyValue();
            }
        }
        else {
            if (node2.getResult().toDouble() == 0) {
                result = new EmptyValue();
            } else {
                result = new NumericValue(node1.getResult().toDouble() / node2.getResult().toDouble());
            }
        }
    }

    public void visit(ExponentNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if(node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null || node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
            if (emptyAsZero) {
                result = new NumericValue(Math.pow(node1.getResult().toDouble(), node2.getResult().toDouble()));
            } else {
                result = new EmptyValue();
            }
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
            EvaluationVisitor subNode = new EvaluationVisitor(row, analysisItem);
            ((CalculationTreeNode) node.getChild(i)).accept(subNode);
            // TODO: Better handling of empty values in functions
            if(subNode.getResult() instanceof EmptyValue) {
                result = new EmptyValue();
                if (!(f instanceof FirstValueFunction) && !(f instanceof IfNotNull)) {
                    return;
                }
                //return;
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
    private AnalysisItem analysisItem;

    public void setRow(IRow r) {
        row = r;
    }
}
