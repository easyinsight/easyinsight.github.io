package com.easyinsight.scorecard;

import com.easyinsight.core.EIDescriptor;

/**
 * User: jamesboe
 * Date: Feb 24, 2010
 * Time: 7:25:45 AM
 */
public class ScorecardDescriptor extends EIDescriptor {

    private long dataSourceID;

    public ScorecardDescriptor() {
    }

    public ScorecardDescriptor(String name, long id, String urlKey, long dataSourceID) {
        super(name, id, urlKey);
        this.dataSourceID = dataSourceID;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    @Override
    public int getType() {
        return EIDescriptor.SCORECARD;
    }
}
