package com.easyinsight.util {
import mx.core.Container;

public interface IAsyncScreen {
    function getContainer():Container;
    function refreshData():void;
    function canRefresh():Boolean;
}
}