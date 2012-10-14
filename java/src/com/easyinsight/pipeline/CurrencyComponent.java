package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: 9/24/12
 * Time: 5:20 PM
 */
public class CurrencyComponent implements IComponent {

    private AnalysisMeasure rootMeasure;
    private AnalysisDimension currency;
    private String targetCurrency;

    public CurrencyComponent(AnalysisMeasure rootMeasure, AnalysisDimension currency, String targetCurrency) {
        this.rootMeasure = rootMeasure;
        this.currency = currency;
        this.targetCurrency = targetCurrency;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(rootMeasure);
            double doubleValue = value.toDouble();
            String currencyCode = row.getValue(currency).toString();
            row.addValue(rootMeasure.createAggregateKey(), CurrencyRetrieval.instance().convertCurrency(currencyCode, doubleValue, targetCurrency));
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
