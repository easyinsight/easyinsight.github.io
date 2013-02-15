package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSLineChartDefinition;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.Arrays;

/**
 * User: jamesboe
 * Date: 2/15/13
 * Time: 9:25 AM
 */
public class DateTimeAxisMax extends Function {
    public Value evaluate() {
        WSAnalysisDefinition report = calculationMetadata.getReport();
        if (!(report instanceof WSLineChartDefinition)) {
            return new EmptyValue();
        }
        String fieldName = minusQuotes(params.get(0)).toString().toLowerCase();
        AnalysisItem fieldToAdd = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (analysisItem.toDisplay().toLowerCase().equals(fieldName)) {
                fieldToAdd = analysisItem;
            }
        }
        if (fieldToAdd == null) {
            throw new FunctionException("Could not find a field by the name of " + fieldName);
        }
        WSListDefinition temp = new WSListDefinition();
        temp.setColumns(Arrays.asList(fieldToAdd));
        temp.setFilterDefinitions(report.getFilterDefinitions());
        temp.setDataFeedID(report.getDataFeedID());
        DataSet dataSet = DataService.listDataSet(temp, calculationMetadata.getInsightRequestMetadata(), calculationMetadata.getConnection());
        if (dataSet.getRows().size() > 0) {
            IRow row = dataSet.getRows().get(0);
            Value value = row.getValue(fieldToAdd.createAggregateKey());
            if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                WSLineChartDefinition lineChartDefinition = (WSLineChartDefinition) report;
                lineChartDefinition.setXAxisMaximum(dateValue.getDate());
            }
        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 1;
    }
}
