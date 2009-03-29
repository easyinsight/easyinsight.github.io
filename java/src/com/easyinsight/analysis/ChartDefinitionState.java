package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.*;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Mar 28, 2009
 * Time: 5:24:03 PM
 */
@Entity
@Table(name="chart_report")
@PrimaryKeyJoinColumn(name="report_state_id")
public class ChartDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="chart_report_id")
    private long definitionID;

    @Column(name="chart_family")
    private int chartFamily;

    @Column(name="chart_type")
    private int chartType;

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

    public long getDefinitionID() {
        return definitionID;
    }

    public void setDefinitionID(long definitionID) {
        this.definitionID = definitionID;
    }

    public int getChartFamily() {
        return chartFamily;
    }

    public void setChartFamily(int chartFamily) {
        this.chartFamily = chartFamily;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChartDefinitionState chartDefinitionState = (ChartDefinitionState) super.clone();
        chartDefinitionState.definitionID = 0;
        return chartDefinitionState;
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
