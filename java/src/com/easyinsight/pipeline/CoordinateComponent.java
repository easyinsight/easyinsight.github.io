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

    private AnalysisZipCode analysisZipCode;
    private AnalysisLatitude analysisLatitude;
    private AnalysisLongitude analysisLongitude;

    public CoordinateComponent(AnalysisZipCode analysisZipCode, AnalysisLatitude analysisLatitude, AnalysisLongitude analysisLongitude) {
        this.analysisZipCode = analysisZipCode;
        this.analysisLatitude = analysisLatitude;
        this.analysisLongitude = analysisLongitude;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {        
        new ZipGeocodeCache().blah(dataSet, analysisZipCode, analysisLongitude, analysisLatitude);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
