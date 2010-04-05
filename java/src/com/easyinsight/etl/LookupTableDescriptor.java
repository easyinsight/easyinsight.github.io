package com.easyinsight.etl;

import com.easyinsight.core.EIDescriptor;

/**
 * User: jamesboe
 * Date: Apr 4, 2010
 * Time: 6:22:57 PM
 */
public class LookupTableDescriptor extends EIDescriptor {

    private long dataSourceID;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    @Override
    public int getType() {
        return EIDescriptor.LOOKUP_TABLE;
    }
}
