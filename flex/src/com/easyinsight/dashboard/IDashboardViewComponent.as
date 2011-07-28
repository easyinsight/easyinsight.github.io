package com.easyinsight.dashboard {
import com.easyinsight.analysis.IRetrievable;

import mx.collections.ArrayCollection;

public interface IDashboardViewComponent extends IRetrievable {
    function refresh(filters:ArrayCollection):void;
    function updateAdditionalFilters(filters:ArrayCollection):void;
    function initialRetrieve():void;
}
}