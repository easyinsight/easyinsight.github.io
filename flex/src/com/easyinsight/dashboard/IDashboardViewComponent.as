package com.easyinsight.dashboard {
import mx.collections.ArrayCollection;

public interface IDashboardViewComponent {
    function refresh():void;
    function updateAdditionalFilters(filterMap:Object):void;
    function initialRetrieve():void;

    function reportCount():ArrayCollection;
}
}