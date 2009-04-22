package com.easyinsight.framework {

import flash.display.DisplayObject;
import flash.events.EventDispatcher;
import mx.controls.Alert;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class ModuleAnalyzeSource extends EventDispatcher {

    private var moduleInfo:IModuleInfo;

    public function ModuleAnalyzeSource() {
    }

    // create the FrontScreen
    // configure with the module name we're assigned to
    // 

    public function createDirect():DisplayObject {
        return null;
    }

    private function moduleFailureHandler(event:ModuleEvent):void {
        Alert.show(event.errorText);
    }
}
}