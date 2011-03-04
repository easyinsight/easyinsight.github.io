package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: Sep 13, 2010
 * Time: 6:45:52 PM
 */
public class DefaultFilterProcessor implements IFilterProcessor {
    public void createRow(DataSet resultDataSet, IRow row, FilterDefinition filterDefinition, boolean matched) {
        if (matched) {
            IRow newRow = resultDataSet.createRow();
            newRow.addValues(row);
        }
    }
}
