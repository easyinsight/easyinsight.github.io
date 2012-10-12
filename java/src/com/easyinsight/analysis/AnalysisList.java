package com.easyinsight.analysis;

import com.easyinsight.core.*;
import nu.xom.Attribute;
import nu.xom.Element;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * User: James Boe
 * Date: Jun 1, 2008
 * Time: 8:03:57 PM
 */
@Entity
@Table(name="analysis_list")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisList extends AnalysisDimension {

    //@Column(name="analysis_list_id")
    //private long analysisListID;
    @Column(name="delimiter")
    private String delimiter;
    @Column(name="expanded")
    private boolean expanded = false;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        element.addAttribute(new Attribute("delimiter", delimiter));
        element.addAttribute(new Attribute("expanded", String.valueOf(expanded)));
        return element;
    }

    @Override
    protected void subclassFromXML(Element fieldNode, XMLImportMetadata xmlImportMetadata) {
        super.subclassFromXML(fieldNode, xmlImportMetadata);
        setDelimiter(fieldNode.getAttribute("delimiter").getValue());
        setExpanded(Boolean.parseBoolean(fieldNode.getAttribute("expanded").getValue()));
    }

    public AnalysisList() {
    }

    public AnalysisList(Key key, String displayName, boolean expanded, String delimiter) {
        super(key, displayName);
        this.expanded = expanded;
        this.delimiter = delimiter;
    }

    public AnalysisList(Key key, boolean expanded, String delimiter) {
        super(key, true);
        this.expanded = expanded;
        this.delimiter = delimiter;
    }

    /*public long getAnalysisListID() {
        return analysisListID;
    }

    public void setAnalysisListID(long analysisListID) {
        this.analysisListID = analysisListID;
    }*/

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.LISTING;
    }

    public boolean isMultipleTransform() {
        return expanded;
    }

    public Value[] transformToMultiple(Value value) {
        Value[] values;
        if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            String[] tokens = stringValue.getValue().split(delimiter);
            values = new Value[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                values[i] = new StringValue(tokens[i].trim());
                values[i].setOriginalValue(stringValue);
            }
        } else {
            values = new Value[1];
            values[0] = new EmptyValue();
        }
        return values;
    }

    public boolean equals(Object o) {
        return this == o || o instanceof AnalysisList && super.equals(o);

    }

    @Override
    public AnalysisItemResultMetadata createResultMetadata() {
        return new AnalysisTagsResultMetadata();
    }
}
