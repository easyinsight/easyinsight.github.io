package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;

import java.io.Serializable;

/**
* User: jamesboe
* Date: 12/16/14
* Time: 11:26 AM
*/
class ResultData implements Serializable {
    public DataResults dataResults;
    public DataSet dataSet;
    public EmbeddedResults results;
    public Throwable exception;

    public WSAnalysisDefinition report;

}
