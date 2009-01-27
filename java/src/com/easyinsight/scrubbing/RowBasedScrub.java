package com.easyinsight.scrubbing;

import com.easyinsight.core.Key;

import java.util.List;

/**
 * User: James Boe
 * Date: Jul 30, 2008
 * Time: 11:59:02 AM
 */
public class RowBasedScrub extends DataScrub {
    private Key targetKey;
    private String targetValue;
    private List<ScrubPair> where;

    public Key getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(Key targetKey) {
        this.targetKey = targetKey;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public List<ScrubPair> getWhere() {
        return where;
    }

    public void setWhere(List<ScrubPair> where) {
        this.where = where;
    }

    public IScrubber createScrubber() {
        throw new UnsupportedOperationException();
    }
}
