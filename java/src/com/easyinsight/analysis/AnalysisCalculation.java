package com.easyinsight.analysis;

import com.easyinsight.calculations.*;
import com.easyinsight.core.ReportKey;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.logging.LogClass;

import javax.persistence.*;

import com.easyinsight.pipeline.Pipeline;
import nu.xom.Attribute;
import nu.xom.Element;
import org.antlr.runtime.RecognitionException;
import org.json.JSONException;
import org.json.JSONObject;

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

    public String getPipelineName() {
        String name;
        if (applyBeforeAggregation) {
            name = Pipeline.BEFORE;
        } else {
            name = Pipeline.AFTER;
        }
        return name;
    }

    @Override
    public int actualType() {
        return AnalysisItemTypes.CALCULATION;
    }

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

    // for every field...

    // what fields come into
    // what calculations were applied
    // what filters were applied

    public String toMarkdown(long dataSourceID, List<AnalysisItem> reportItems, WSAnalysisDefinition report, EIConnection conn, Feed feed) {
        List<AnalysisItem> allItems = new ArrayList<AnalysisItem>(feed.getFields());
        allItems.addAll(reportItems);
        CalculationTreeNode tree;
        ICalculationTreeVisitor visitor;

        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> unqualifiedDisplayMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, UniqueKey> map = new NamespaceGenerator().generate(dataSourceID, report != null ? report.getAddonReports() : new ArrayList<AddonReport>(), conn);
        try {
            tree = CalculationHelper.createTree(calculationString, true);

            KeyDisplayMapper mapper = KeyDisplayMapper.create(allItems);
            keyMap = mapper.getKeyMap();
            displayMap = mapper.getDisplayMap();
            unqualifiedDisplayMap = mapper.getUnqualifiedDisplayMap();

            if (report != null && report.getFilterDefinitions() != null) {
                for (FilterDefinition filter : report.getFilterDefinitions()) {
                    filter.calculationItems(displayMap);
                }
            }
            visitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), map);
            tree.accept(visitor);
        } catch (RecognitionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        VariableListVisitor variableVisitor = new VariableListVisitor();
        tree.accept(variableVisitor);

        // which fields were used?

        Set<AnalysisItem> items = variableVisitor.getVariableList();
        StringBuilder markdown = new StringBuilder();
        markdown.append(super.toMarkdown(dataSourceID, reportItems, report, conn, feed));
        markdown.append("Calculation: '''" + calculationString + "'''\r\n\r\n");
        markdown.append("Apply before Aggregation: '''" + applyBeforeAggregation + "'''\r\n\r\n");
        markdown.append("Field uses:\r\n\r\n");
        for (AnalysisItem item : items) {
            markdown.append("#" + item.toDisplay() + "\r\n\r\n");
        }
        return markdown.toString();
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.CALCULATION;
    }

    /*@Transient
    private transient CalculationTreeNode tree;

    @Transient
    private transient Set<KeySpecification> specs;*/

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {

        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> unqualifiedDisplayMap = new HashMap<String, List<AnalysisItem>>();
        if (allItems != null) {
            boolean avoidNamespaceCollisions = false;
            if (structure != null) {
                if (structure.getInsightRequestMetadata() != null) {
                    avoidNamespaceCollisions = structure.getInsightRequestMetadata().isAvoidKeyDisplayCollisions();
                }
            }
            KeyDisplayMapper mapper = KeyDisplayMapper.create(allItems, avoidNamespaceCollisions);
            keyMap = mapper.getKeyMap();
            displayMap = mapper.getDisplayMap();
            unqualifiedDisplayMap = mapper.getUnqualifiedDisplayMap();
            for (FilterDefinition filter : structure.getFilters()) {
                filter.calculationItems(displayMap);
            }

        }




        List<AnalysisItem> analysisItemList = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);

        if (structure.isNoCalcs()) {
            return analysisItemList;
        }

        if (!structure.onOrAfter(structure.getInsightRequestMetadata().getDerived(this))) {
            return analysisItemList;
        }

        if (getKey() instanceof ReportKey) {
            return analysisItemList;
        }

        CalculationTreeNode tree;

        ResolverVisitor visitor;

        try {
            tree = CalculationHelper.createTree(calculationString, false);

            visitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), structure.getNamespaceMap());
            tree.accept(visitor);
            if (visitor.getWarnings() != null && structure.getInsightRequestMetadata() != null) {
                Collection<String> warnings = visitor.getWarnings();
                for (String warning : warnings) {
                    warning = "In calculating <b>" + toDisplay() + "</b>, " + warning;
                    structure.getInsightRequestMetadata().getWarnings().add(warning);
                }
            }

        }  catch (FunctionException fe) {
            LogClass.error(fe.getMessage() + " in the calculation of " + toDisplay() + ".");
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

    protected List<AnalysisItem> measureFilters(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> items;
        if (structure.onOrAfter(structure.getInsightRequestMetadata().getDerived(this))) {
            items = new ArrayList<AnalysisItem>();
            for (FilterDefinition filterDefinition : getFilters()) {
                items.addAll(filterDefinition.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure));
            }
        } else {
            items = super.measureFilters(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);
        }
        return items;
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
        element.addAttribute(new Attribute("applyBeforeAggregation", String.valueOf(applyBeforeAggregation)));
        element.addAttribute(new Attribute("recalculateSummary", String.valueOf(recalculateSummary)));
        Element calculation = new Element("calculation");
        calculation.appendChild(calculationString);
        element.appendChild(calculation);
        return element;
    }

    @Override
    protected void subclassFromXML(Element fieldNode, XMLImportMetadata xmlImportMetadata) {
        super.subclassFromXML(fieldNode, xmlImportMetadata);
        calculationString = fieldNode.query("calculation").get(0).getValue();
        applyBeforeAggregation = Boolean.parseBoolean(fieldNode.getAttribute("applyBeforeAggregation").getValue());
        recalculateSummary = Boolean.parseBoolean(fieldNode.getAttribute("recalculateSummary").getValue());
    }

    @Override
    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = super.toJSON(md);
        jo.put("calculation", getCalculationString());
        return jo;
    }
}
