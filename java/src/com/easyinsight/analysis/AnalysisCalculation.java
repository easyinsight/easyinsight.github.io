package com.easyinsight.analysis;

import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.logging.LogClass;

import javax.persistence.*;

import com.easyinsight.pipeline.CleanupComponent;
import nu.xom.Element;
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

    @Column(name="apply_before_aggregation")
    private boolean applyBeforeAggregation;

    @Column(name="recalculate_summary")
    private boolean recalculateSummary;

    @Column(name="cached_calculation")
    private boolean cachedCalculation;

    @Override
    public boolean persistable() {
        return cachedCalculation;
    }

    public boolean isCachedCalculation() {
        return cachedCalculation;
    }

    public void setCachedCalculation(boolean cachedCalculation) {
        this.cachedCalculation = cachedCalculation;
    }

    public boolean isRecalculateSummary() {
        return recalculateSummary;
    }

    public void setRecalculateSummary(boolean recalculateSummary) {
        this.recalculateSummary = recalculateSummary;
    }

    private transient CalculationTreeNode calculationTreeNode;

    public String getCalculationString() {
        return calculationString;
    }

    public void setCalculationString(String calculationString) {
        this.calculationString = calculationString;
    }

    public String toKeySQL() {
        if (!isCachedCalculation()) {
            throw new RuntimeException("Attempt made to retrieve SQL for a derived analysis item.");
        }
        return getKey().toBaseKey().toSQL();
    }

    public boolean isApplyBeforeAggregation() {
        return applyBeforeAggregation;
    }

    public void setApplyBeforeAggregation(boolean applyBeforeAggregation) {
        this.applyBeforeAggregation = applyBeforeAggregation;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.CALCULATION;
    }

    /*@Transient
    private transient CalculationTreeNode tree;

    @Transient
    private transient Set<KeySpecification> specs;*/

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, int criteria, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        if (isCachedCalculation() && !structure.isOnStorage()) {
            return super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria, analysisItemSet, structure);
        }
        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
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
            if (structure.getReport() != null && structure.getReport().getFilterDefinitions() != null) {
                for (FilterDefinition filter : structure.getReport().getFilterDefinitions()) {
                    filter.calculationItems(displayMap);
                }
            }
        }
        CalculationTreeNode tree;
        Set<KeySpecification> specs;

            ICalculationTreeVisitor visitor;
            CalculationsParser.startExpr_return ret;

            CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(calculationString));
            CommonTokenStream tokes = new CommonTokenStream();
            tokes.setTokenSource(lexer);
            CalculationsParser parser = new CalculationsParser(tokes);
            parser.setTreeAdaptor(new NodeFactory());

            try {
                ret = parser.startExpr();
                tree = (CalculationTreeNode) ret.getTree();

                visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
                tree.accept(visitor);
            }  catch (FunctionException fe) {
                throw new ReportException(new AnalysisItemFault(fe.getMessage() + " in the calculation of " + toDisplay() + ".", this));
            } catch (ReportException re) {
                throw re;
            } catch (Exception e) {
                if ("org.antlr.runtime.tree.CommonErrorNode cannot be cast to com.easyinsight.calculations.CalculationTreeNode".equals(e.getMessage())) {
                    throw new ReportException(new AnalysisItemFault("Syntax error in the calculation of " + toDisplay() + ".", this));
                }
                LogClass.error("On calculating " + calculationString, e);
                String message;
                if (e.getMessage() == null) {
                    message = "Internal error";
                } else {
                    message = e.getMessage();
                }
                throw new ReportException(new AnalysisItemFault(message + " in calculating " + calculationString, this));
            }

            VariableListVisitor variableVisitor = new VariableListVisitor();
            tree.accept(variableVisitor);

            specs = variableVisitor.getVariableList();



        List<AnalysisItem> analysisItemList = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria, analysisItemSet, structure);
        //analysisItemList.add(this);

        if (!includeFilters && isApplyBeforeAggregation()) return analysisItemList;

        if (!isApplyBeforeAggregation() && !hasCriteria(criteria, CleanupComponent.AGGREGATE_CALCULATIONS)) return analysisItemList;

        for (KeySpecification spec : specs) {
            AnalysisItem analysisItem;
            try {
                analysisItem = spec.findAnalysisItem(keyMap, displayMap);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            if (analysisItem != null) {
                analysisItemList.addAll(analysisItem.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria, analysisItemSet, structure));
            }
        }

        /*if (getFilters() != null && getFilters().size() > 0) {
            List<AnalysisItem> copyItems = new ArrayList<AnalysisItem>();
            for (AnalysisItem analysisItem : analysisItemList) {
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE) && !analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                    try {
                        AnalysisItem clone = analysisItem.clone();
                        clone.getFilters().addAll(getFilters());
                        copyItems.add(clone);
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    copyItems.add(analysisItem);
                }
            }
            analysisItemList = copyItems;
        }*/
        
        return analysisItemList;
    }

    protected List<AnalysisItem> measureFilters(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, int criteria, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> items;
        if (criteria == CleanupComponent.AGGREGATE_CALCULATIONS && !this.isApplyBeforeAggregation()) {
            items = new ArrayList<AnalysisItem>();
            for (FilterDefinition filterDefinition : getFilters()) {
                items.addAll(filterDefinition.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria, analysisItemSet, structure));
            }
        } else {
            items = super.measureFilters(allItems, insightItems, getEverything, includeFilters, criteria, analysisItemSet, structure);
        }
        return items;
    }

    private CalculationTreeNode evalString(String s) {
        CalculationTreeNode calculationTreeNode;        
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

    public List<AnalysisItem> getDerivedItems() {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        return items;
    }

    @Override
    public boolean isDerived() {
        return !cachedCalculation;
    }

    public boolean blocksDBAggregation() {
        return applyBeforeAggregation && !cachedCalculation;
    }

    public boolean isCalculated() {
        return !applyBeforeAggregation;
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        Element calculation = new Element("calculation");
        calculation.appendChild(calculationString);
        element.appendChild(calculation);
        return element;
    }
}
