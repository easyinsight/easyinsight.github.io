package com.easyinsight.analysis {
import flash.display.InteractiveObject;

public class AirMenuFactory implements IMenuFactory {
    public function AirMenuFactory() {
    }

    public function createListMenu(copyFunction:Function, individualRowFunction:Function, drilldownFunction:Function, rollupFunction:Function,
            interactiveObject:InteractiveObject):void {
        
    }

    public function createStandardMenu(drilldownFunction:Function, rollupFunction:Function, interactiveObject:InteractiveObject):void {
    }
}
}