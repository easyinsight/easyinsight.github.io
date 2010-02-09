package com.easyinsight.analysis;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: jboe
 * Date: Jan 29, 2008
 * Time: 10:08:55 AM
 */
public class ExplicitRange {


    private List<RangeOption> ranges = new ArrayList<RangeOption>();


    public ExplicitRange(List<RangeOption> ranges) {
        this.ranges = ranges;

    }

    public RangeOption getRange(double value) {
        RangeOption matchedOption = null;

            for (RangeOption rangeOption : ranges) {
                if (value >= rangeOption.getRangeMinimum() &&
                        value < rangeOption.getRangeMaximum()) {
                    matchedOption = rangeOption;
                }
            }

        return matchedOption;
    }   
}