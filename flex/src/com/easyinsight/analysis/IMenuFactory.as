package com.easyinsight.analysis {
import flash.display.InteractiveObject;

public interface IMenuFactory {
    function createListMenu(copyFunction:Function, individualRowFunction:Function, drilldownFunction:Function, rollupFunction:Function,
            interactiveObject:InteractiveObject):void;
    function createStandardMenu(drilldownFunction:Function, rollupFunction:Function, interactiveObject:InteractiveObject):void;
}
}