package com.easyinsight.scorecard;

import com.easyinsight.core.EIDescriptor;

/**
 * User: jamesboe
 * Date: Feb 24, 2010
 * Time: 7:25:45 AM
 */
public class ScorecardDescriptor extends EIDescriptor {

    private long groupID;
    private int groupRole;
    private String groupName;
    private Integer order = Integer.MAX_VALUE;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupRole() {
        return groupRole;
    }

    public void setGroupRole(int groupRole) {
        this.groupRole = groupRole;
    }

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
