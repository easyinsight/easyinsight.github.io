package com.easyinsight.analysis;

import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.Pipeline;
import nu.xom.Attribute;
import nu.xom.Element;
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

    public String getPipelineName() {
        String pipelineName;
        if (applyBeforeAggregation) {
            pipelineName = Pipeline.BEFORE;
        } else {
            pipelineName = Pipeline.AFTER;
        }
        return pipelineName;
    }

    @Override
    public int actualType() {
        return AnalysisItemTypes.DERIVED_DATE;
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

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        CalculationTreeNode tree;
        ICalculationTreeVisitor visitor;

        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();

        List<AnalysisItem> analysisItemList = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);

        if (!structure.onOrAfter(structure.getInsightRequestMetadata().getDerived(this))) {
            return analysisItemList;
        }

        try {
            tree = CalculationHelper.createTree(derivationCode);

            if (allItems != null) {
                KeyDisplayMapper mapper = KeyDisplayMapper.create(allItems);
                keyMap = mapper.getKeyMap();
                displayMap = mapper.getDisplayMap();
            }
            visitor = new ResolverVisitor(keyMap, displayMap, new FunctionFactory(), structure.getNamespaceMap());
            tree.accept(visitor);
        } catch (FunctionException fe) {
            LogClass.error("On calculating " + derivationCode, fe);
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

        for (AnalysisItem analysisItem : variableVisitor.getVariableList()) {
            if (analysisItem != null) {
                for (AnalysisItem analysisItem1 : analysisItem.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure)) {
                    if (structure.getInsightRequestMetadata().getPipelines(analysisItem).isEmpty()) {
                        structure.getInsightRequestMetadata().getPipelines(analysisItem).add(structure.getInsightRequestMetadata().getDerived(this));
                    }
                    analysisItemList.add(analysisItem1);
                }
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
        return super.getType() | AnalysisItemTypes.DERIVED_DATE;
    }

    @Override
    public boolean isDerived() {
        return true;
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        element.addAttribute(new Attribute("applyBeforeAggregation", String.valueOf(applyBeforeAggregation)));
        Element calculation = new Element("calculation");
        calculation.appendChild(derivationCode);
        element.appendChild(calculation);
        return element;
    }

    @Override
    protected void subclassFromXML(Element fieldNode, XMLImportMetadata xmlImportMetadata) {
        super.subclassFromXML(fieldNode, xmlImportMetadata);
        derivationCode = fieldNode.query("calculation").get(0).getValue();
        applyBeforeAggregation = Boolean.parseBoolean(fieldNode.getAttribute("applyBeforeAggregation").getValue());
    }
}
