package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 7:34:27 AM
 */
public class LinkDecorationComponent implements IComponent {


    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
            if (analysisItem.getUrlPattern() != null && analysisItem.getUrlPattern().length() > 0) {
                // compile the pattern
                // retrieve the value for each row, annotate
                for (IRow row : dataSet.getRows()) {

                }
            }

        }

        return dataSet;
    }

    public void decorate(ListDataResults listDataResults) {

    }
}
