package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 9:10:35 PM
 */
public abstract class WSChartDefinition extends WSGraphicDefinition {
    private Long chartDefinitionID;
    private LimitsMetadata limitsMetadata;

    public Long getChartDefinitionID() {
        return chartDefinitionID;
    }

    public void setChartDefinitionID(Long chartDefinitionID) {
        this.chartDefinitionID = chartDefinitionID;
    }

    public abstract int getChartType();

    public abstract int getChartFamily();

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
        /*LimitsResults limitsResults;
        if (limitsMetadata != null) {
            int count = dataSet.getRows().size();
            limitsResults = new LimitsResults(count > limitsMetadata.getNumber(), count, limitsMetadata.getNumber());             
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) getMeasures().get(0);
            dataSet.sort(analysisMeasure, limitsMetadata.isTop());
            dataSet.subset(limitsMetadata.getNumber());
        } else {
            limitsResults = super.applyLimits(dataSet);
        }
        return limitsResults;*/
        // TODO: fix
        return super.applyLimits(dataSet);
    }    
}
