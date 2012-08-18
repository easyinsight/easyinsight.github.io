package com.easyinsight.analysis;

import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.Pipeline;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

import javax.persistence.*;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 12, 2010
 * Time: 11:52:27 AM
 */
@Entity
@Table(name="derived_analysis_date_dimension")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class DerivedAnalysisDateDimension extends AnalysisDateDimension {
    @Column(name="derivation_code")
    private String derivationCode;

    @Column(name="apply_before_aggregation")
    private boolean applyBeforeAggregation;

    @Transient
    transient private String pipelineName;

    public String getPipelineName() {
        if (pipelineName == null) {
            if (applyBeforeAggregation) {
                pipelineName = Pipeline.BEFORE;
            } else {
                pipelineName = Pipeline.AFTER;
            }
        }
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public boolean isApplyBeforeAggregation() {
        return applyBeforeAggregation;
    }

    public void setApplyBeforeAggregation(boolean applyBeforeAggregation) {
        this.applyBeforeAggregation = applyBeforeAggregation;
    }

    public String getDerivationCode() {
        return derivationCode;
    }

    public void setDerivationCode(String derivationCode) {
        this.derivationCode = derivationCode;
    }

    @Override
    public boolean isTimeshift() {
        return false;
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        CalculationTreeNode tree;
        ICalculationTreeVisitor visitor;
        CalculationsParser.startExpr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(derivationCode));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
            Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
        try {
            ret = parser.startExpr();
            tree = (CalculationTreeNode) ret.getTree();

            if (allItems != null) {
                for (AnalysisItem analysisItem : allItems) {
                    List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                    if (items == null) {
                        items = new ArrayList<AnalysisItem>(1);
                        keyMap.put(analysisItem.getKey().toKeyString(), items);
                    }
                    items.add(analysisItem);
                }

                for (AnalysisItem analysisItem : allItems) {
                    List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                    if (items == null) {
                        items = new ArrayList<AnalysisItem>(1);
                        displayMap.put(analysisItem.toDisplay(), items);
                    }
                    items.add(analysisItem);
                }
            }
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
            tree.accept(visitor);
        } catch (FunctionException fe) {
            throw new ReportException(new AnalysisItemFault(fe.getMessage() + " in the calculation of " + toDisplay() + ".", this));
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            if ("org.antlr.runtime.tree.CommonErrorNode cannot be cast to com.easyinsight.calculations.CalculationTreeNode".equals(e.getMessage())) {
                throw new ReportException(new AnalysisItemFault("Syntax error in the calculation of " + toDisplay() + ".", this));
            }
            LogClass.error("On calculating " + derivationCode, e);
            String message;
            if (e.getMessage() == null) {
                message = "Internal error";
            } else {
                message = e.getMessage();
            }
            throw new ReportException(new AnalysisItemFault(message + " in calculating " + derivationCode, this));
        }
        VariableListVisitor variableVisitor = new VariableListVisitor();
        tree.accept(variableVisitor);

        Set<KeySpecification> specs = variableVisitor.getVariableList();

        List<AnalysisItem> analysisItemList = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);

        if (!structure.onOrAfter(getPipelineName())) {
            return analysisItemList;
        }

        for (KeySpecification spec : specs) {
            AnalysisItem analysisItem;
            try {
                analysisItem = spec.findAnalysisItem(keyMap, displayMap);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            if (analysisItem != null) {
                analysisItemList.addAll(analysisItem.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure));
            }
        }

        return analysisItemList;
    }

    public Value calculate(IRow row, Collection<AnalysisItem> analysisItems, CalculationMetadata calculationMetadata) {
        CalculationTreeNode calculationTreeNode;
        ICalculationTreeVisitor visitor;
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(derivationCode));
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
            Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : analysisItems) {
                List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    keyMap.put(analysisItem.getKey().toKeyString(), items);
                }
                items.add(analysisItem);
            }
            Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : analysisItems) {
                List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    displayMap.put(analysisItem.toDisplay(), items);
                }
                items.add(analysisItem);
            }
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
            calculationTreeNode.accept(visitor);

            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(row, this, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
            return rowVisitor.getResult();
        } catch (FunctionException fe) {
            throw new ReportException(new AnalysisItemFault(fe.getMessage() + " in the calculation of " + toDisplay() + ".", this));
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + " in calculating " + derivationCode, e);
        }
    }

    public List<AnalysisItem> getDerivedItems() {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        return items;
    }

    @Override
    public int getType() {
        return super.getType() | AnalysisItemTypes.DERIVED_DATE;
    }

    @Override
    public boolean isDerived() {
        return true;
    }
}
