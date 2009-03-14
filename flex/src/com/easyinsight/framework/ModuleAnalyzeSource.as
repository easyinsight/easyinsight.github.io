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

    public function startModuleLoading():void {
        moduleInfo = ModuleManager.getModule(getModuleName());
        moduleInfo.addEventListener(ModuleEvent.READY, moduleLoadHandler);
        moduleInfo.addEventListener(ModuleEvent.ERROR, moduleFailureHandler);
        moduleInfo.load();
    }

    protected function getModuleName():String {
        return null;
    }

    private function moduleLoadHandler(event:ModuleEvent):void {
        var dataAnalysis:DisplayObject = event.module.factory.create() as DisplayObject;
        applyProperties(dataAnalysis);
        dispatchEvent(new FullScreenModuleEvent(dataAnalysis));
    }

    public function createDirect():DisplayObject {
        return null;
    }

    protected function applyProperties(val:Object):void {

    }

    private function moduleFailureHandler(event:ModuleEvent):void {
        Alert.show(event.errorText);
    }
}
}