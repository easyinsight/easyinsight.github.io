package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: Sep 13, 2010
 * Time: 6:24:00 PM
 */
public interface IFilterProcessor {

    void createRow(DataSet resultDataSet, IRow row, FilterDefinition filterDefinition, boolean matched);
}
