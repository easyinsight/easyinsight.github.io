package com.easyinsight.analysis;

import com.easyinsight.core.EIDescriptor;

/**
 * User: jamesboe
 * Date: 1/21/14
 * Time: 11:30 AM
 */
public class FilterSetDescriptor extends EIDescriptor {

    private long dataSourceID;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    @Override
    public int getType() {
        return FILTER_SET;
    }
}
