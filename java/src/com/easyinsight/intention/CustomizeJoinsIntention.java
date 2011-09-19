package com.easyinsight.intention;

import com.easyinsight.analysis.JoinOverride;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 12:16 PM
 */
public class CustomizeJoinsIntention extends Intention {

    private List<JoinOverride> joinOverrides;

    public CustomizeJoinsIntention(List<JoinOverride> joinOverrides) {
        this.joinOverrides = joinOverrides;
    }

    public CustomizeJoinsIntention() {
    }

    public List<JoinOverride> getJoinOverrides() {
        return joinOverrides;
    }

    public void setJoinOverrides(List<JoinOverride> joinOverrides) {
        this.joinOverrides = joinOverrides;
    }
}
