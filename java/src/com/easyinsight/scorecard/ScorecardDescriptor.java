package com.easyinsight.scorecard;

import com.easyinsight.core.EIDescriptor;

/**
 * User: jamesboe
 * Date: Feb 24, 2010
 * Time: 7:25:45 AM
 */
public class ScorecardDescriptor extends EIDescriptor {

    private long groupID;

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    @Override
    public int getType() {
        return EIDescriptor.SCORECARD;
    }
}
