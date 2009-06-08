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

    @Column(name="rotation_angle")
    private double rotationAngle;

    @Column(name="elevation_angle")
    private double elevationAngle;

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
    public static final int MULTI_MEASURE_LINE_2D = 16;

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public double getElevationAngle() {
        return elevationAngle;
    }

    public void setElevationAngle(double elevationAngle) {
        this.elevationAngle = elevationAngle;
    }

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
    public AnalysisDefinitionState clone() throws CloneNotSupportedException {
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
                wsChartDefinition.setReportType(WSAnalysisDefinition.COLUMN);
                break;
            case ChartDefinitionState.COLUMN_3D:
                wsChartDefinition = new WS3DColumnChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.COLUMN3D);
                break;
            case ChartDefinitionState.BAR_2D:
                wsChartDefinition = new WSBarChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.BAR);
                break;
            case ChartDefinitionState.BAR_3D:
                wsChartDefinition = new WS3DBarChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.BAR3D);
                break;
            case ChartDefinitionState.PIE_2D:
                wsChartDefinition = new WSPieChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.PIE);
                break;
            case ChartDefinitionState.PIE_3D:
                wsChartDefinition = new WS3DPieChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.PIE3D);
                break;
            case ChartDefinitionState.LINE_2D:
                wsChartDefinition = new WSLineChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.LINE);
                break;
            case ChartDefinitionState.LINE_3D:
                wsChartDefinition = new WS3DLineChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.LINE3D);
                break;
            case ChartDefinitionState.AREA_2D:
                wsChartDefinition = new WSAreaChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.AREA);
                break;
            case ChartDefinitionState.AREA_3D:
                wsChartDefinition = new WS3DAreaChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.AREA3D);
                break;
            case ChartDefinitionState.PLOT_TYPE:
                wsChartDefinition = new WSPlotChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.PLOT);
                break;
            case ChartDefinitionState.BUBBLE_TYPE:
                wsChartDefinition = new WSBubbleChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.BUBBLE);
                break;
            case ChartDefinitionState.MULTI_MEASURE_LINE_2D:
                wsChartDefinition = new WSMultiMeasureLineChartDefinition();
                wsChartDefinition.setReportType(WSAnalysisDefinition.MULTIMEASURE);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        wsChartDefinition.setRotationAngle(rotationAngle);
        wsChartDefinition.setElevationAngle(elevationAngle);
        wsChartDefinition.setChartDefinitionID(definitionID);
        return wsChartDefinition;
    }
}
