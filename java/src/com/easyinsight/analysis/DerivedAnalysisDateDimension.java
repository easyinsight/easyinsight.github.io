package com.easyinsight.analysis;

import com.easyinsight.calculations.*;
import com.easyinsight.core.ReportKey;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.Pipeline;
import nu.xom.Attribute;
import nu.xom.Element;
import org.antlr.runtime.RecognitionException;

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
            tree = CalculationHelper.createTree(derivationCode, true);

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
        markdown.append("Calculation: '''" + derivationCode + "'''\r\n\r\n");
        markdown.append("Apply before Aggregation: '''" + applyBeforeAggregation + "'''\r\n\r\n");
        markdown.append("Field uses:\r\n\r\n");
        for (AnalysisItem item : items) {
            markdown.append("#" + item.toDisplay() + "\r\n\r\n");
        }
        return markdown.toString();
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
        ResolverVisitor visitor;

        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> unqualifiedDisplayMap = new HashMap<String, List<AnalysisItem>>();

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

        try {
            tree = CalculationHelper.createTree(derivationCode, false);

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
            }
            visitor = new ResolverVisitor(keyMap, displayMap, unqualifiedDisplayMap, new FunctionFactory(), structure.getNamespaceMap());
            tree.accept(visitor);
            if (visitor.getWarnings() != null && structure.getInsightRequestMetadata() != null) {
                Collection<String> warnings = visitor.getWarnings();
                for (String warning : warnings) {
                    warning = "In calculating <b>" + toDisplay() + "</b>, " + warning;
                    structure.getInsightRequestMetadata().getWarnings().add(warning);
                }
            }
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
                    if (analysisItem1 instanceof AnalysisDateDimension) {
                        AnalysisDateDimension date = (AnalysisDateDimension) analysisItem1;
                        try {
                            if (getDateLevel() == AnalysisDateDimension.HOUR_LEVEL) {
                                date = (AnalysisDateDimension) date.clone();
                                date.setDateLevel(getDateLevel());
                                analysisItem1 = date;
                            } else if (getDateLevel() == AnalysisDateDimension.MINUTE_LEVEL) {
                                analysisItem1 = (AnalysisDateDimension) date.clone();
                                date.setDateLevel(getDateLevel());
                                analysisItem1 = date;
                            }
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    }
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
