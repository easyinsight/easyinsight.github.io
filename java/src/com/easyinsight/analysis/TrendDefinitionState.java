package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSTrendDefinition;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import nu.xom.Element;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:58:50 PM
 */
@Entity
@PrimaryKeyJoinColumn(name="report_state_id")
@Table(name="trend_report")
public class TrendDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="trend_report_id")
    private long trendReportID;
    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        return element;
    }

    public void subclassFromXML(Element element, XMLImportMetadata xmlImportMetadata) {
    }

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSTrendDefinition trend = new WSTrendDefinition();
        trend.setTrendReportID(trendReportID);
        return trend;
    }

    public long getTrendReportID() {
        return trendReportID;
    }

    public void setTrendReportID(long trendReportID) {
        this.trendReportID = trendReportID;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        TrendDefinitionState trendState = (TrendDefinitionState) super.clone(allFields);
        trendState.setTrendReportID(0);
        return trendState;
    }
}
