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

    public static final int COLUMN_FAMILY = 1;
    public static final int BAR_FAMILY = 2;
    public static final int PIE_FAMILY = 3;
    public static final int LINE_FAMILY = 4;
    public static final int PLOT_FAMILY = 5;
    public static final int BUBBLE_FAMILY = 6;
    public static final int AREA_FAMILY = 7;

    public static final int COLUMN_2D = 0;
    public static final int COLUMN_2D_STACKED = 1;
    public static final int COLUMN_3D = 2;
    public static final int COLUMN_3D_STACKED = 3;

    public static final int BAR_2D = 4;
    public static final int BAR_2D_STACKED = 5;
    public static final int BAR_3D = 6;
    public static final int BAR_3D_STACKED = 7;

    public static final int PIE_2D = 8;
    public static final int PIE_3D = 9;

    public static final int LINE_2D = 10;
    public static final int LINE_3D = 11;

    public static final int AREA_2D = 12;
    public static final int AREA_3D = 13;

    public static final int BUBBLE_TYPE = 14;
    public static final int PLOT_TYPE = 15;

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
        WSChartDefinition wsChartDefinition;
        switch (chartType) {
            case COLUMN_2D:
                wsChartDefinition = new WSColumnChartDefinition();
                break;
            case COLUMN_3D:
                wsChartDefinition = new WS3DColumnChartDefinition();
                break;
            case BAR_2D:
                wsChartDefinition = new WSBarChartDefinition();
                break;
            case BAR_3D:
                wsChartDefinition = new WS3DBarChartDefinition();
                break;
            case PIE_2D:
                wsChartDefinition = new WSPieChartDefinition();
                break;
            case PIE_3D:
                wsChartDefinition = new WS3DPieChartDefinition();
                break;
            case LINE_2D:
                wsChartDefinition = new WSLineChartDefinition();
                break;
            case LINE_3D:
                wsChartDefinition = new WS3DLineChartDefinition();
                break;
            case AREA_2D:
                wsChartDefinition = new WSAreaChartDefinition();
                break;
            case AREA_3D:
                wsChartDefinition = new WS3DAreaChartDefinition();
                break;
            case PLOT_TYPE:
                wsChartDefinition = new WSPlotChartDefinition();
                break;
            case BUBBLE_TYPE:
                wsChartDefinition = new WSBubbleChartDefinition();
                break;
            default:
                throw new UnsupportedOperationException();
        }

        wsChartDefinition.setChartDefinitionID(chartDefinitionID);
        return wsChartDefinition;
    }

    public AnalysisDefinition clone() throws CloneNotSupportedException {
        ChartDefinition chartDefinition = (ChartDefinition) super.clone();
        chartDefinition.setChartDefinitionID(null);
        return chartDefinition;
    }
}
