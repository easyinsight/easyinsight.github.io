package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 9:37:40 AM
 */
public class TemporalComponent implements IComponent {

    private TemporalAnalysisMeasure temporalAnalysisMeasure;

    public TemporalComponent(TemporalAnalysisMeasure temporalAnalysisMeasure) {
        this.temporalAnalysisMeasure = temporalAnalysisMeasure;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {        
        List<AnalysisItem> list = new ArrayList<AnalysisItem>(pipelineData.getReportItems());
        TemporalTransform temporalTransform = dataSet.temporalTransform(list, temporalAnalysisMeasure);
        DataSet listSet = temporalTransform.aggregate();
        pipelineData.getReportItems().remove(temporalAnalysisMeasure.getAnalysisDimension());
        return listSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
