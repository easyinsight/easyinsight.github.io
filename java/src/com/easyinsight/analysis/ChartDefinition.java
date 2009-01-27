package com.easyinsight.analysis;

import com.easyinsight.AnalysisItemFactory;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 10:11:36 PM
 */
@Entity
@Table(name="chart_definition")
@PrimaryKeyJoinColumn(name="analysis_id")
public class ChartDefinition extends GraphicDefinition {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="chart_definition_id")
    private Long chartDefinitionID;

    @Column(name="chart_family")
    private int chartFamily;

    @Column(name="chart_type")
    private int chartType;

    @OneToOne
    @JoinColumn(name="limits_metadata_id")
    private LimitsMetadata limitsMetadata;

    public LimitsMetadata getLimitsMetadata() {
        return limitsMetadata;
    }

    public void setLimitsMetadata(LimitsMetadata limitsMetadata) {
        this.limitsMetadata = limitsMetadata;
    }

    public Long getChartDefinitionID() {
        return chartDefinitionID;
    }

    public void setChartDefinitionID(Long chartDefinitionID) {
        this.chartDefinitionID = chartDefinitionID;
    }

    public int getChartFamily() {
        return chartFamily;
    }

    public void setChartFamily(int chartFamily) {
        this.chartFamily = chartFamily;
    }

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSChartDefinition wsChartDefinition = new WSChartDefinition();
        wsChartDefinition.setChartType(chartType);
        wsChartDefinition.setChartFamily(chartFamily);
        wsChartDefinition.setDimensions(AnalysisItemFactory.fromAnalysisFields(getDimensions()));
        wsChartDefinition.setMeasures(AnalysisItemFactory.fromAnalysisFields(getMeasures()));
        wsChartDefinition.setChartDefinitionID(chartDefinitionID);
        return wsChartDefinition;
    }

    public AnalysisDefinition clone() throws CloneNotSupportedException {
        ChartDefinition chartDefinition = (ChartDefinition) super.clone();
        chartDefinition.setChartDefinitionID(null);
        return chartDefinition;
    }
}
