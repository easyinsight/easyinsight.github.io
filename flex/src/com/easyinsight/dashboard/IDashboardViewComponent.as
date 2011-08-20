package com.easyinsight.dashboard {
import com.easyinsight.analysis.IRetrievable;

import mx.collections.ArrayCollection;

public interface IDashboardViewComponent extends IRetrievable {
    function refresh():void;
    function updateAdditionalFilters(filterMap:Object):void;
    function initialRetrieve():void;
}
}