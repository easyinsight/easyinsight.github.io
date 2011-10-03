/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {

import com.easyinsight.analysis.LoadingModuleDisplay;
import com.easyinsight.analysis.list.ListModule;
import com.easyinsight.analysis.verticallist.VerticalListModule;

import flash.events.Event;

import flash.events.EventDispatcher;

import flash.system.ApplicationDomain;

import mx.controls.Alert;
import mx.core.Container;

import mx.events.ModuleEvent;

import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class ReportModuleLoader extends EventDispatcher {

    private var moduleInfo:IModuleInfo;

    private var _loadingDisplay:LoadingModuleDisplay;
    private var container:Container;

    public function ReportModuleLoader() {
    }

    private var inline:Boolean = false;
    private var moduleName:String;

    public function loadReportRenderer(_reportRendererModule:String, container:Container):void {
        if (_reportRendererModule == "ListModule.swf" || _reportRendererModule == "VerticalListModule.swf") {
            inline = true;
            moduleName = _reportRendererModule;
            dispatchEvent(new Event("moduleLoaded"));
        } else {
            this.container = container;
            moduleInfo = ModuleManager.getModule(Constants.instance().prefix + "/app/"+Constants.instance().buildPath+"/" + _reportRendererModule);
            moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler);
            moduleInfo.addEventListener(ModuleEvent.ERROR, reportFailureHandler);
            _loadingDisplay = new LoadingModuleDisplay();
            _loadingDisplay.moduleInfo = moduleInfo;
            container.addChild(_loadingDisplay);
            moduleInfo.load(ApplicationDomain.currentDomain);
        }
    }

    private function reportLoadHandler(event:ModuleEvent):void {
        if (_loadingDisplay != null) {
            container.removeChild(_loadingDisplay);
            _loadingDisplay.moduleInfo = null;
            _loadingDisplay = null;
        }
        if (moduleInfo != null) {
            moduleInfo.removeEventListener(ModuleEvent.READY, reportLoadHandler);
            moduleInfo.removeEventListener(ModuleEvent.ERROR, reportFailureHandler);
        }
        dispatchEvent(new Event("moduleLoaded"));
    }

    public function create():Object {
        if (inline) {
            if (moduleName == "ListModule.swf") {
                return new ListModule();
            } else if (moduleName == "VerticalListModule.swf") {
                return new VerticalListModule();
            }
        }
        return moduleInfo.factory.create();
    }

    private function reportFailureHandler(event:ModuleEvent):void {
        if (event.errorText != "SWF is not a loadable module") {
            Alert.show(event.errorText);
        }
    }
}
}
