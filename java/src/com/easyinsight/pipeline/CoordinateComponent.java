package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: Mar 26, 2010
 * Time: 8:22:12 PM
 */
public class CoordinateComponent implements IComponent {

    private AnalysisItem analysisZipCode;

    public CoordinateComponent(AnalysisItem analysisZipCode) {
        this.analysisZipCode = analysisZipCode;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {        
        new ZipGeocodeCache().blah(dataSet, analysisZipCode);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
