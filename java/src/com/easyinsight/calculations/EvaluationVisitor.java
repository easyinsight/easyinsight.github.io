package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import org.antlr.runtime.tree.Tree;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    private CalculationMetadata calculationMetadata;

    public EvaluationVisitor(@Nullable IRow r, @Nullable AnalysisItem analysisItem, CalculationMetadata calculationMetadata) {
        row = r;
        this.calculationMetadata = calculationMetadata;
        this.analysisItem = analysisItem;
        if (analysisItem != null && analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
//            AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
            /*emptyAsZero = analysisCalculation.getFormattingConfiguration().getFormattingType() != FormattingConfiguration.MILLISECONDS &&
                    analysisCalculation.getFormattingConfiguration().getFormattingType() != FormattingConfiguration.SECONDS;*/
            emptyAsZero = true;
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

    public void visit(EqualsNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if (equalsNodes(node1, node2))
            result = new NumericValue(1);
        else
            result = new NumericValue(0);

    }

    private boolean equalsNodes(EvaluationVisitor node1, EvaluationVisitor node2) {

        if (node1.getResult().type() == Value.STRING && node2.getResult().type() == Value.STRING) {
            return Function.minusQuotes(node1.getResult()).equals(Function.minusQuotes(node2.getResult()));
        } else return node1.getResult().equals(node2.getResult());
    }

    public void visit(NotEqualsNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if (equalsNodes(node1, node2))
            result = new NumericValue(0);
        else
            result = new NumericValue(1);
    }

    public void visit(GreaterThanNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if (greaterThanNodes(node1, node2))
            result = new NumericValue(1);
        else
            result = new NumericValue(0);
    }

    private boolean greaterThanNodes(EvaluationVisitor node1, EvaluationVisitor node2) {
        Value compare1 = Function.minusQuotes(node1.getResult());
        Value compare2 = Function.minusQuotes(node2.getResult());
        if (compare1.type() == Value.DATE || compare2.type() == Value.DATE) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();

            if (compare1.type() == Value.DATE) {
                DateValue dateValue = (DateValue) compare1;
                cal1.setTime(dateValue.getDate());
            } else {
                cal1.setTimeInMillis(compare1.toDouble().longValue());
            }

            if (compare2.type() == Value.DATE) {
                DateValue dateValue = (DateValue) compare2;
                cal2.setTime(dateValue.getDate());
            } else {
                cal2.setTimeInMillis(compare2.toDouble().longValue());
            }

            if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR) ||
                    (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR))) {
                return true;
            } else {
                return false;
            }
        } else {
            if (compare1.toDouble() > compare2.toDouble()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void visit(GreaterThanEqualToNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if (greaterThanNodes(node1, node2) || equalsNodes(node1, node2))
            result = new NumericValue(1);
        else
            result = new NumericValue(0);
    }

    public void visit(LessThanNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if (greaterThanNodes(node1, node2) || equalsNodes(node1, node2))
            result = new NumericValue(0);
        else
            result = new NumericValue(1);
    }

    public void visit(LessThanEqualToNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if (greaterThanNodes(node1, node2))
            result = new NumericValue(0);
        else
            result = new NumericValue(1);
    }

    public void visit(AndNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);

        if (node1.getResult().toDouble() > 0 && node2.getResult().toDouble() > 0)
            result = new NumericValue(1);
        else
            result = new NumericValue(0);
    }

    public void visit(OrNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);

        if (node1.getResult().toDouble() > 0 || node2.getResult().toDouble() > 0)
            result = new NumericValue(1);
        else
            result = new NumericValue(0);
    }

    public void visit(NotNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        if(node1.getResult().toDouble() > 0)
            result = new NumericValue(0);
        else
            result = new NumericValue(1);
    }

    public void visit(AddNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);

        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        result = node1.getResult();
        if (node.getChildCount() == 2) {
            EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
            ((CalculationTreeNode) node.getChild(1)).accept(node2);
            Value result2 = node2.getResult();
            if (result.type() == Value.STRING || result2.type() == Value.STRING) {
                result = new StringValue(Function.minusQuotes(result).toString() + Function.minusQuotes(result2).toString());
            } else if (result.type() == Value.DATE && result2.type() == Value.NUMBER) {
                DateValue dateValue = (DateValue) result;
                NumericValue result2Value = (NumericValue) result2;
                if (result2Value.getCalendarType() > 0) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(result2Value.getCalendarType(), result2Value.getCalendarValue());
                    result = new DateValue(cal.getTime());
                } else {
                    long time = dateValue.getDate().getTime();
                    long delta = (long) (time + result2.toDouble());
                    result = new NumericValue(delta);
                }
            } else if (result.type() == Value.DATE && result2.type() == Value.DATE) {
                DateValue dateValue = (DateValue) result;
                DateValue dateValue2 = (DateValue) result2;
                long delta = dateValue.getDate().getTime() + dateValue2.getDate().getTime();
                result = new NumericValue(delta);
            } else if (result.type() == Value.NUMBER && result2.type() == Value.DATE) {
                DateValue dateValue = (DateValue) result2;
                long delta = (long) (result.toDouble() + dateValue.getDate().getTime());
                result = new NumericValue(delta);
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

    public void visit(SubtractNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);

        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        result = node1.getResult();

        if (node1.getResult() instanceof EmptyValue) {
            result = new EmptyValue();
            return;
        }
        if (node.getChildCount() == 2) {
            EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
            ((CalculationTreeNode) node.getChild(1)).accept(node2);
            Value result2 = node2.getResult();
            if (result.type() == Value.NUMBER && result2.type() == Value.NUMBER) {
                result = new NumericValue(result.toDouble() - result2.toDouble());
            } else if (result.type() == Value.DATE && result2.type() == Value.NUMBER) {
                DateValue dateValue = (DateValue) result;
                NumericValue result2Value = (NumericValue) result2;
                if (result2Value.getCalendarType() > 0) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(result2Value.getCalendarType(), -result2Value.getCalendarValue());
                    result = new DateValue(cal.getTime());
                } else {
                    long time = dateValue.getDate().getTime();
                    long delta = (long) (time - result2.toDouble());
                    result = new NumericValue(delta);
                }
            } else if (result.type() == Value.DATE && result2.type() == Value.DATE) {
                DateValue dateValue = (DateValue) result;
                DateValue dateValue2 = (DateValue) result2;
                long delta = dateValue.getDate().getTime() - dateValue2.getDate().getTime();
                result = new NumericValue(delta);
            } else if (result.type() == Value.NUMBER && result2.type() == Value.DATE) {
                DateValue dateValue = (DateValue) result2;
                long delta = (long) (result.toDouble() - dateValue.getDate().getTime());
                result = new NumericValue(delta);
            } else if (node2.getResult().type() == Value.EMPTY || node1.getResult().type() == Value.EMPTY) {
                if (emptyAsZero) {
                    result = new NumericValue(result.toDouble() - node2.getResult().toDouble());
                } else {
                    result = new EmptyValue();
                }
            } else {
                result = new NumericValue(result.toDouble() - node2.getResult().toDouble());
            }
        } else {
            result = new NumericValue(-result.toDouble());
        }
    }

    public void visit(MultiplyNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);

        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if (node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null || node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
            if (emptyAsZero) {
                result = new NumericValue(node1.getResult().toDouble() * node2.getResult().toDouble());
            } else {
                result = new EmptyValue();
            }
        } else {
            result = new NumericValue(node1.getResult().toDouble() * node2.getResult().toDouble());
        }
    }

    public void visit(DivideNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);

        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if (node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null || node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
            if (emptyAsZero) {
                result = new NumericValue(node1.getResult().toDouble() / node2.getResult().toDouble());
            } else {
                result = new EmptyValue();
            }
        } else {
            if (node2.getResult().toDouble() == 0) {
                result = new EmptyValue();
            } else {
                result = new NumericValue(node1.getResult().toDouble() / node2.getResult().toDouble());
            }
        }
    }

    public void visit(ExponentNode node) {
        EvaluationVisitor node1 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(0)).accept(node1);
        EvaluationVisitor node2 = new EvaluationVisitor(row, analysisItem, calculationMetadata);
        ((CalculationTreeNode) node.getChild(1)).accept(node2);
        if (node1.getResult() instanceof EmptyValue || node1.getResult().toDouble() == null || node2.getResult() instanceof EmptyValue || node2.getResult().toDouble() == null) {
            if (emptyAsZero) {
                result = new NumericValue(Math.pow(node1.getResult().toDouble(), node2.getResult().toDouble()));
            } else {
                result = new EmptyValue();
            }
        } else {
            result = new NumericValue(Math.pow(node1.getResult().toDouble(), node2.getResult().toDouble()));
        }
    }

    public void visit(VariableNode node) {
        if (row == null) {
            result = new EmptyValue();
        } else {
            //if (node.ready()) {
            result = row.getValue(node.createAggregateKey());
            /*} else {
                if (analysisItem == null) {
                    throw new ReportException(new GenericReportFault("A calculation depending on " + node.toDisplay() + " is running before " + node.toDisplay() + " was actually calculated."));
                } else {
                    throw new ReportException(new AnalysisItemFault(analysisItem.toDisplay() + " depends on " + node.toDisplay() + ", which hasn't yet been calculated.", analysisItem));
                }
            }*/
        }
    }

    public void visit(FunctionNode node) {
        IFunction f = node.getFunction();
        f.setCalculationMetadata(calculationMetadata);
        if (f.onDemand()) {
            f.setFunctionNode(node);
            f.setAnalysisItem(analysisItem);
            f.setRow(row);
            result = f.evaluate();
        } else {
            List<Value> params = new LinkedList<Value>();
            for (int i = 1; i < node.getChildCount(); i++) {
                EvaluationVisitor subNode = new EvaluationVisitor(row, analysisItem, calculationMetadata);
                ((CalculationTreeNode) node.getChild(i)).accept(subNode);
                // TODO: Better handling of empty values in functions
                /*if(subNode.getResult() instanceof EmptyValue) {
                    result = new EmptyValue();
                    if (!(f instanceof FirstValueFunction) && !(f instanceof IfNotNull) && !(f instanceof GreaterThan) && !(f instanceof IsOnly) && !(f instanceof EqualTo)) {
                        return;
                    }
                    //return;
                }*/
                params.add(subNode.getResult());
            }
            f.setParameters(params);

            result = f.evaluate();
        }
        f.clearParams();
    }

    private Value result;

    public Value getResult() {
        return result;
    }

    @Nullable
    private IRow row;
    private Map<Key, List<Value>> columnSlicedData;
    @Nullable
    private AnalysisItem analysisItem;
}
