package com.easyinsight.analysis;

import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.pipeline.Pipeline;
import nu.xom.Attribute;
import nu.xom.Element;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 11:08:13 PM
 */
@Entity
@Table(name="analysis_measure")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisMeasure extends AnalysisItem {
    @Column(name="aggregation")
    private int aggregation;

    @Column(name="row_count_field")
    private boolean rowCountField;

    @Column(name="underline")
    private boolean underline;

    @Column(name="fp_precision")
    private int precision;

    @Column(name="min_precision")
    private int minPrecision;

    @OneToOne(cascade=CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="currency_grouping")
    private AnalysisItem currencyField;

    public AnalysisItem getCurrencyField() {
        return currencyField;
    }

    public void setCurrencyField(AnalysisItem currencyField) {
        this.currencyField = currencyField;
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        element.addAttribute(new Attribute("aggregation", String.valueOf(aggregation)));
        element.addAttribute(new Attribute("rowCountField", String.valueOf(rowCountField)));
        element.addAttribute(new Attribute("underline", String.valueOf(underline)));
        element.addAttribute(new Attribute("precision", String.valueOf(precision)));
        element.addAttribute(new Attribute("minPrecision", String.valueOf(minPrecision)));
        return element;
    }

    @Override
    protected void subclassFromXML(Element fieldNode, XMLImportMetadata xmlImportMetadata) {
        super.subclassFromXML(fieldNode, xmlImportMetadata);
        setAggregation(Integer.parseInt(fieldNode.getAttribute("aggregation").getValue()));
        setPrecision(Integer.parseInt(fieldNode.getAttribute("precision").getValue()));
        setMinPrecision(Integer.parseInt(fieldNode.getAttribute("minPrecision").getValue()));
        setRowCountField(Boolean.parseBoolean(fieldNode.getAttribute("rowCountField").getValue()));
        setUnderline(Boolean.parseBoolean(fieldNode.getAttribute("underline").getValue()));
    }

    public AnalysisMeasure() {
    }

    public int getType() {
        return AnalysisItemTypes.MEASURE;
    }

    @Override
    public int actualType() {
        return AnalysisItemTypes.MEASURE;
    }

    public AnalysisMeasure(Key key, int aggregation) {
        super(key);
        this.aggregation = aggregation;
    }

    public AnalysisMeasure(String key, int aggregation) {
        super(key);
        this.aggregation = aggregation;
    }

    public AnalysisMeasure(Key key, String displayName, int aggregation) {
        super(key, displayName);
        this.aggregation = aggregation;
    }

    public AnalysisMeasure(Key key, String displayName, int aggregation, boolean highIsGood) {
        this(key, displayName, aggregation);
        this.aggregation = aggregation;
        setHighIsGood(highIsGood);
    }

    public AnalysisMeasure(Key key, String displayName, int aggregation, boolean highIsGood, int formattingType) {
        this(key, displayName, aggregation, highIsGood);
        this.aggregation = aggregation;
        FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
        formattingConfiguration.setFormattingType(formattingType);
        setFormattingConfiguration(formattingConfiguration);
    }

    @Override
    public void afterLoad(boolean optimized) {
        super.afterLoad(optimized);
        if (currencyField != null) {
            currencyField = (AnalysisItem) Database.deproxy(currencyField);
            currencyField.afterLoad();
        }
    }

    @Override
    public void beforeSave() {
        super.beforeSave();
        if (currencyField != null) {
            currencyField.beforeSave();
        }
    }

    public void reportSave(Session session) {
        super.reportSave(session);
        if (currencyField != null && currencyField.getAnalysisItemID() == 0) {
            currencyField.reportSave(session);
            session.save(currencyField);
        }
    }

    @Override
    public void updateIDs(ReplacementMap replacementMap) {
        super.updateIDs(replacementMap);
        if (getCurrencyField() != null) {
            setCurrencyField(replacementMap.getField(getCurrencyField()));
        }
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> analysisItems = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);
        if (currencyField != null) {
            if (structure.getInsightRequestMetadata().getPipelines(currencyField).isEmpty()) {
                structure.getInsightRequestMetadata().getPipelines(currencyField).add(Pipeline.BEFORE);
            }
            analysisItems.add(currencyField);
        }
        return analysisItems;
    }

    public int getMinPrecision() {
        return minPrecision;
    }

    public void setMinPrecision(int minPrecision) {
        this.minPrecision = minPrecision;
    }

    public boolean isRowCountField() {
        return rowCountField;
    }

    public void setRowCountField(boolean rowCountField) {
        this.rowCountField = rowCountField;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    private transient AggregateMeasureKey cachedMeasureKey;

    @Override
    public AggregateKey createAggregateKey() {
        // in case of filters, how do we do this...
        if (cachedMeasureKey == null) {
            cachedMeasureKey = new AggregateMeasureKey(getKey(), getType(), aggregation, toDisplay(), getFilters());
        }
        return cachedMeasureKey;
    }

    @Override
    public AnalysisItem clone() throws CloneNotSupportedException {
        AnalysisMeasure measure = (AnalysisMeasure) super.clone();
        measure.cachedAggregateKey = null;
        measure.cachedMeasureKey = null;
        return measure;
    }

    private transient AggregateKey cachedAggregateKey;

    public AggregateKey createAggregateKey(boolean measure) {
        if (measure) {
            if (cachedAggregateKey == null) {
                cachedAggregateKey = new AggregateKey(getKey(), getType(), getFilters());
            }
            return cachedAggregateKey;
        } else {
            return super.createAggregateKey();
        }
    }

    @Override
    protected String getQualifiedSuffix() {
        return getType() + ":" + aggregation + ":" + toDisplay();
    }

    public Value transformValue(Value value, InsightRequestMetadata insightRequestMetadata, boolean timezoneShift) {
        if (getAggregation() == AggregationTypes.COUNT_DISTINCT) {
            return value;
        }
        Value result;
        if (value == null) {
            result = new EmptyValue();
        } else if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            NumericValue numericValue = new NumericValue();
            numericValue.setValue(stringValue.getValue());
            result = numericValue;
        } else  {
            result = value;
        }
        return result;
    }

    public int getAggregation() {
        return aggregation;
    }

    public void setAggregation(int aggregation) {
        this.aggregation = aggregation;
    }

    public AnalysisItemResultMetadata createResultMetadata() {
        return new AnalysisMeasureResultMetadata();
    }

    public int getQueryAggregation() {
        return aggregation;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisMeasure)) return false;
        if (!super.equals(o)) return false;

        AnalysisMeasure that = (AnalysisMeasure) o;

        return aggregation == that.aggregation;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + aggregation;
        return result;
    }
}
