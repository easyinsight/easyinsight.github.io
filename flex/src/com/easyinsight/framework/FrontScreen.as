package com.easyinsight.framework {
import com.easyinsight.analysis.LoadingModuleDisplay;
import com.easyinsight.listing.IPerspective;
import flash.display.DisplayObject;
import mx.containers.Canvas;
import mx.controls.Alert;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;
public class FrontScreen extends Canvas {

    private var perspective:IPerspective;
    private var moduleInfo:IModuleInfo;
    private var moduleName:String;

    private var _loadingDisplay:LoadingModuleDisplay;

    public function FrontScreen() {
        super();
    }

    private function loadReportRenderer():void {
        moduleInfo = ModuleManager.getModule("/app/easyui-debug/" + moduleName);
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler);
        moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler);
        _loadingDisplay = new LoadingModuleDisplay();
        _loadingDisplay.moduleInfo = moduleInfo;
        addChild(_loadingDisplay);
        moduleInfo.load();
    }

    private function reportLoadHandler(event:ModuleEvent):void {
        perspective = moduleInfo.factory.create() as IPerspective;
        if (_loadingDisplay != null) {
            removeChild(_loadingDisplay);
            _loadingDisplay = null;
        }
        addChild(perspective as DisplayObject);
    }

    private function reportFailureHandler(event:ModuleEvent):void {
        Alert.show(event.errorText);
    }
}
}