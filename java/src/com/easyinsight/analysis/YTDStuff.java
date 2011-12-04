package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.ArrayList;
import java.util.List;

/**
* User: jamesboe
* Date: 12/3/11
* Time: 5:24 PM
*/
public class YTDStuff {
    private List<Value> intervals;
    List<YTDValue> values = new ArrayList<YTDValue>();

    YTDStuff(List<Value> intervals, List<YTDValue> values) {
        this.intervals = intervals;
        this.values = values;
    }

    public List<Value> getIntervals() {
        return intervals;
    }

    public List<YTDValue> getValues() {
        return values;
    }
}
