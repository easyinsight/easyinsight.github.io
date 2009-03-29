package com.easyinsight.analysis;


import com.easyinsight.analysis.definitions.*;

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
        WSChartDefinition wsChartDefinition;
        switch (chartType) {
            case ChartDefinitionState.COLUMN_2D:
                wsChartDefinition = new WSColumnChartDefinition();
                break;
            case ChartDefinitionState.COLUMN_3D:
                wsChartDefinition = new WS3DColumnChartDefinition();
                break;
            case ChartDefinitionState.BAR_2D:
                wsChartDefinition = new WSBarChartDefinition();
                break;
            case ChartDefinitionState.BAR_3D:
                wsChartDefinition = new WS3DBarChartDefinition();
                break;
            case ChartDefinitionState.PIE_2D:
                wsChartDefinition = new WSPieChartDefinition();
                break;
            case ChartDefinitionState.PIE_3D:
                wsChartDefinition = new WS3DPieChartDefinition();
                break;
            case ChartDefinitionState.LINE_2D:
                wsChartDefinition = new WSLineChartDefinition();
                break;
            case ChartDefinitionState.LINE_3D:
                wsChartDefinition = new WS3DLineChartDefinition();
                break;
            case ChartDefinitionState.AREA_2D:
                wsChartDefinition = new WSAreaChartDefinition();
                break;
            case ChartDefinitionState.AREA_3D:
                wsChartDefinition = new WS3DAreaChartDefinition();
                break;
            case ChartDefinitionState.PLOT_TYPE:
                wsChartDefinition = new WSPlotChartDefinition();
                break;
            case ChartDefinitionState.BUBBLE_TYPE:
                wsChartDefinition = new WSBubbleChartDefinition();
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return wsChartDefinition;
    }
}
