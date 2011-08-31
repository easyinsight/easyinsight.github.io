package com.easyinsight.dashboard {

public interface IDashboardViewComponent {
    function refresh():void;
    function updateAdditionalFilters(filterMap:Object):void;
    function initialRetrieve():void;
}
}