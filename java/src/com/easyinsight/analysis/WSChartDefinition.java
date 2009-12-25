package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.MinMaxComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 9:10:35 PM
 */
public abstract class WSChartDefinition extends WSAnalysisDefinition {
    private LimitsMetadata limitsMetadata;

    private double rotationAngle;
    private double elevationAngle;

    public abstract int getChartType();

    public abstract int getChartFamily();

    private long chartDefinitionID;

    public long getChartDefinitionID() {
        return chartDefinitionID;
    }

    public void setChartDefinitionID(long chartDefinitionID) {
        this.chartDefinitionID = chartDefinitionID;
    }

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

    public LimitsMetadata getLimitsMetadata() {
        return limitsMetadata;
    }

    public void setLimitsMetadata(LimitsMetadata limitsMetadata) {
        this.limitsMetadata = limitsMetadata;
    }

    public String getDataFeedType() {
        return "Chart";
    }

    public LimitsResults applyLimits(DataSet dataSet) {
        LimitsResults limitsResults;
        if (limitsMetadata != null) {
            int count = dataSet.getRows().size();
            limitsResults = new LimitsResults(count >= limitsMetadata.getNumber(), count, limitsMetadata.getNumber());
            Map<String, AnalysisItem> structure = new HashMap<String, AnalysisItem>();
            createReportStructure(structure);
            AnalysisMeasure analysisMeasure = null;
            for (AnalysisItem analysisItem : structure.values()) {
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    analysisMeasure = (AnalysisMeasure) analysisItem;
                    break;
                }
            }
            if (analysisMeasure != null) {
                dataSet.sort(analysisMeasure, limitsMetadata.isTop());
                dataSet.subset(limitsMetadata.getNumber());
            }
        } else {
            limitsResults = super.applyLimits(dataSet);
        }
        return limitsResults;
    }

    @Override
    public List<IComponent> createComponents() {
        List<IComponent> components = super.createComponents();
        components.add(new MinMaxComponent());
        return components;
    }
}
