package com.easyinsight.analysis.gauge;

import com.easyinsight.analysis.AnalysisDefinitionState;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.WSGaugeDefinition;
import com.easyinsight.core.XMLMetadata;
import nu.xom.Attribute;
import nu.xom.Element;

import javax.persistence.*;
import java.util.List;

/**
 * User: James Boe
 * Date: Mar 31, 2009
 * Time: 10:06:02 AM
 */
@Entity
@Table(name="gauge_report")
public class GaugeDefinitionState extends AnalysisDefinitionState {

    @Column(name="gauge_type")
    private int gaugeType;

    @Column(name="max_value")
    private double maxValue;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("gaugeDefinitionState");
        element.addAttribute(new Attribute("gaugeType", String.valueOf(gaugeType)));
        element.addAttribute(new Attribute("maxValue", String.valueOf(maxValue)));
        return element;
    }

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="gauge_report_id")
    private long gaugeDefinitionID;

    public long getGaugeDefinitionID() {
        return gaugeDefinitionID;
    }

    public void setGaugeDefinitionID(long gaugeDefinitionID) {
        this.gaugeDefinitionID = gaugeDefinitionID;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public int getGaugeType() {
        return gaugeType;
    }

    public void setGaugeType(int gaugeType) {
        this.gaugeType = gaugeType;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSGaugeDefinition wsGaugeDefinition = new WSGaugeDefinition();
        wsGaugeDefinition.setGaugeType(gaugeType);
        wsGaugeDefinition.setGaugeDefinitionID(gaugeDefinitionID);
        wsGaugeDefinition.setMaxValue(maxValue);
        return wsGaugeDefinition;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        GaugeDefinitionState state = (GaugeDefinitionState) super.clone(allFields);
        state.setGaugeDefinitionID(0);
        return state;
    }
}
