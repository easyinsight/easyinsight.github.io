package com.easyinsight.analysis;

import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.core.Key;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 12, 2010
 * Time: 11:52:27 AM
 */
@Entity
@Table(name="derived_analysis_dimension")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class DerivedAnalysisDimension extends AnalysisDimension {
    @Column(name="derivation_code")
    private String derivationCode;

    public String getDerivationCode() {
        return derivationCode;
    }

    public void setDerivationCode(String derivationCode) {
        this.derivationCode = derivationCode;
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, int criteria) {
        CalculationTreeNode tree;
        ICalculationTreeVisitor visitor;
        CalculationsParser.startExpr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(derivationCode));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        try {
            ret = parser.startExpr();
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
            }
            tree = (CalculationTreeNode) ret.getTree();
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory());
            tree.accept(visitor);
        } catch (FunctionException fe) {
            throw new ReportException(new AnalysisItemFault(fe.getMessage() + " in the calculation of " + toDisplay() + ".", this));
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + " in calculating " + derivationCode, e);
        }
        VariableListVisitor variableVisitor = new VariableListVisitor();
        tree.accept(variableVisitor);

        Set<KeySpecification> specs = variableVisitor.getVariableList();

        List<AnalysisItem> analysisItemList = new ArrayList<AnalysisItem>();

        analysisItemList.add(this);

        if (!includeFilters) return analysisItemList;

        for (KeySpecification spec : specs) {
            AnalysisItem analysisItem = null;
            try {
                analysisItem = spec.findAnalysisItem(allItems);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            if (analysisItem != null) {
                analysisItemList.addAll(analysisItem.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria));
            }
        }

        return analysisItemList;
    }

    public List<AnalysisItem> getDerivedItems() {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        return items;
    }

    @Override
    public int getType() {
        return super.getType() | AnalysisItemTypes.DERIVED_DIMENSION;
    }

    @Override
    public boolean isDerived() {
        return true;
    }

    public boolean isCalculated() {
        return true;
    }
}
