package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.PipelineData;

import java.util.Set;

/**
 * User: jamesboe
 * Date: 12/4/11
 * Time: 4:59 PM
 */
public class ExtendedDataSet {
    private DataSet dataSet;
    private PipelineData pipelineData;
    private Set<AnalysisItem> reportItems;

    public ExtendedDataSet(DataSet dataSet, PipelineData pipelineData, Set<AnalysisItem> reportItems) {
        this.dataSet = dataSet;
        this.pipelineData = pipelineData;
        this.reportItems = reportItems;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public PipelineData getPipelineData() {
        return pipelineData;
    }

    public Set<AnalysisItem> getReportItems() {
        return reportItems;
    }
}
