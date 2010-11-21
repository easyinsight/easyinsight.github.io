package com.easyinsight.analysis {
import com.easyinsight.filtering.FilterRawData;

public interface ISelectableReportRenderer extends IReportRenderer {
    function set selectionEnabled(mode:Boolean):void;
    function createFilterRawData():FilterRawData;
}
}