package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.logging.LogClass;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.util.*;

/**
 * User: James Boe
 * Date: Jul 12, 2008
 * Time: 12:35:33 AM
 */
@Entity
@Table(name="analysis_calculation")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisCalculation extends AnalysisMeasure {

    @Column(name="calculation_string")
    private String calculationString;

    private transient CalculationTreeNode calculationTreeNode;

    public String getCalculationString() {
        return calculationString;
    }

    public void setCalculationString(String calculationString) {
        this.calculationString = calculationString;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.CALCULATION;
    }

    public void preHandleData(DataSet dataSet) {
        calculationTreeNode = evalString(calculationString);
        
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, List<AnalysisItem> insightItems) {
        Map<Key, AnalysisItem> map = new HashMap<Key, AnalysisItem>();
        for (AnalysisItem analysisItem : allItems) {
            map.put(analysisItem.getKey(), analysisItem);
        }
        Resolver resolver = new Resolver(allItems);
        CalculationTreeNode tree;
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(calculationString));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        try {
            ret = parser.expr();
            tree = (CalculationTreeNode) ret.getTree();
            visitor = new ResolverVisitor(resolver, new FunctionFactory());
            tree.accept(visitor);
        } catch (RecognitionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        VariableListVisitor variableVisitor = new VariableListVisitor();
        tree.accept(variableVisitor);
        Set<AnalysisItem> analysisItems = new HashSet<AnalysisItem>();
        Set<Key> keys = variableVisitor.getVariableList();
        for (Key key : keys) {
            AnalysisItem analysisItem = map.get(key);
            boolean alreadyInInsight = false;
            for (AnalysisItem insightItem : insightItems) {
                if (insightItem.getKey().equals(analysisItem.getKey())) {
                    alreadyInInsight = true;
                }
            }
            if (!alreadyInInsight) analysisItems.add(analysisItem);
        }
        return new ArrayList<AnalysisItem>(analysisItems);
    }

    public Set<AnalysisItem> getNeededKeys(List<AnalysisItem> fields) {
        Map<Key, AnalysisItem> map = new HashMap<Key, AnalysisItem>();
        for (AnalysisItem analysisItem : fields) {
            map.put(analysisItem.getKey(), analysisItem);
        }
        Resolver resolver = new Resolver(fields);
        CalculationTreeNode tree;
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(calculationString));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        try {
            ret = parser.expr();
            tree = (CalculationTreeNode) ret.getTree();
            visitor = new ResolverVisitor(resolver, new FunctionFactory());
            tree.accept(visitor);
        } catch (RecognitionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        VariableListVisitor variableVisitor = new VariableListVisitor();
        tree.accept(variableVisitor);
        Set<AnalysisItem> analysisItems = new HashSet<AnalysisItem>();
        Set<Key> keys = variableVisitor.getVariableList();
        for (Key key : keys) {
            AnalysisItem analysisItem = map.get(key);
            analysisItems.add(analysisItem);
        }
        return analysisItems;
    }

    private CalculationTreeNode evalString(String s) {
        CalculationTreeNode calculationTreeNode;
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(s));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        try {
            ret = parser.expr();
            calculationTreeNode = (CalculationTreeNode) ret.getTree();
            //visitor = new ResolverVisitor(r, new FunctionFactory());
            //calculationTreeNode.accept(visitor);
        } catch (RecognitionException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return calculationTreeNode;
    }

    public Value createValue(IRow row) {
        ICalculationTreeVisitor visitor = new EvaluationVisitor(row);
        calculationTreeNode.accept(visitor);
        return visitor.getResult();
    }

    public Value calculate(DataSet dataSet, IRow row) {
        CalculationTreeNode calculationTreeNode;
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(calculationString));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        Resolver r = new Resolver();
        for (Key key : row.getKeys()) {
            r.addKey(key);
        }
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
            visitor = new ResolverVisitor(r, new FunctionFactory());
            calculationTreeNode.accept(visitor);
        } catch (RecognitionException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }

        ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(row);
        try {
            calculationTreeNode.accept(rowVisitor);
            return new NumericValue(rowVisitor.getResult().toDouble());
        } catch (Exception e) {
            LogClass.error(e);
            return new EmptyValue();
        }
    }

    public List<AnalysisItem> getDerivedItems() {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        return items;
    }
}
