package com.easyinsight.dashboard {
import com.easyinsight.analysis.IRetrievable;

import mx.collections.ArrayCollection;

public interface IDashboardViewComponent extends IRetrievable {
    function updateAdditionalFilters(filterMap:Object):void;
    function initialRetrieve():void;

    function reportCount():ArrayCollection;

    function obtainPreferredSizeInfo():SizeInfo;

    function toggleFilters(showFilters:Boolean):void;
}
}