package com.easyinsight.analysis;

import com.easyinsight.webservice.ShortAnalysisDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.AnalysisMeasure;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 9:10:35 PM
 */
public class WSChartDefinition extends WSGraphicDefinition {
    private int chartType;
    private int chartFamily;
    private Long chartDefinitionID;
    private LimitsMetadata limitsMetadata;

    public Long getChartDefinitionID() {
        return chartDefinitionID;
    }

    public void setChartDefinitionID(Long chartDefinitionID) {
        this.chartDefinitionID = chartDefinitionID;
    }

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }

    public int getChartFamily() {
        return chartFamily;
    }

    public void setChartFamily(int chartFamily) {
        this.chartFamily = chartFamily;
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

    public ShortAnalysisDefinition createShortAnalysisDefinition() {
        throw new UnsupportedOperationException();
    }

    public LimitsResults applyLimits(DataSet dataSet) {
        LimitsResults limitsResults;
        if (limitsMetadata != null) {
            int count = dataSet.getRows().size();
            limitsResults = new LimitsResults(count > limitsMetadata.getNumber(), count, limitsMetadata.getNumber());             
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) getMeasures().get(0);
            dataSet.sort(analysisMeasure, limitsMetadata.isTop());
            dataSet.subset(limitsMetadata.getNumber());
        } else {
            limitsResults = super.applyLimits(dataSet);
        }
        return limitsResults;
    }
}
