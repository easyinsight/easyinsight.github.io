package com.easyinsight.framework {
import com.easyinsight.analysis.AnalysisCloseEvent;
import com.easyinsight.analysis.LoadingModuleDisplay;
import com.easyinsight.listing.AnalyzeSource;
import com.easyinsight.listing.IModulePerspective;
import com.easyinsight.listing.IPerspective;

import flash.display.DisplayObject;
import flash.events.Event;
import flash.events.EventDispatcher;

import mx.containers.Canvas;
import mx.controls.Alert;
import mx.core.UIComponent;
import mx.events.ModuleEvent;
import mx.modules.IModuleInfo;
import mx.modules.ModuleManager;

public class ModulePerspective extends Canvas implements IPerspective, IModulePerspective, AnalyzeSource {


    private var perspectiveType:int;
    public var actualModule:UIComponent;

    public var properties:Object;

    public function ModulePerspective(perspectiveInfo:PerspectiveInfo) {
        percentWidth = 100;
        percentHeight = 100;
        this.perspectiveType = perspectiveInfo.perspectiveType;
        this.properties = perspectiveInfo.properties;
    }

    private function fromPerspectiveType(type:int):void {
        var result:PerspectiveFactoryResult = PerspectiveFactoryHolder.factory.fromType(type);
        if (result is DirectUIComponent) {
            inline(DirectUIComponent(result).uiComponent);
        } else {
            var mod:ModuleUIComponent = ModuleUIComponent(result);
            loadModule(mod.module, mod.label);
        }
    }

    private function inline(obj:Object):void {
        if (properties != null) {
            for (var p:String in properties) {
                obj[p] = properties[p];
            }
        }
        addListeners(obj as EventDispatcher);
        addChild(obj as DisplayObject);
        actualModule = obj as UIComponent;
    }

    private var moduleInfo:IModuleInfo;

    protected override function createChildren():void {
        super.createChildren();
        fromPerspectiveType(perspectiveType);
    }

    private function loadModule(name:String, message:String):void {
        moduleInfo = ModuleManager.getModule("/app/"+Constants.instance().buildPath+"/" + name + ".swf");
        moduleInfo.addEventListener(ModuleEvent.READY, reportLoadHandler, false, 0, true);
        moduleInfo.addEventListener(ModuleEvent.ERROR, moduleFailHandler, false, 0, true);
        _loadingDisplay = new LoadingModuleDisplay();
        _loadingDisplay.message = message;
        _loadingDisplay.moduleInfo = moduleInfo;
        addChild(_loadingDisplay);
        moduleInfo.load();
    }

    private function addListeners(eventDispatcher:EventDispatcher):void {
        eventDispatcher.addEventListener(AnalysisCloseEvent.ANALYSIS_CLOSE, passThrough, false, 0, true);
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    private var _loadingDisplay:LoadingModuleDisplay;

    private function moduleFailHandler(event:ModuleEvent):void {
        Alert.show("Module loading problem - " + event.errorText);
    }

    public function gotFocus():void {
        if (actualModule != null && actualModule is IPerspective) {
            IPerspective(actualModule).gotFocus();
        }
    }

    public function cleanup():void {
        if (actualModule != null && actualModule is IPerspective) {
            IPerspective(actualModule).cleanup();
            actualModule.removeEventListener(AnalysisCloseEvent.ANALYSIS_CLOSE, passThrough);
        }
    }

    private function reportLoadHandler(event:ModuleEvent):void {
        if (moduleInfo != null) {
            moduleInfo.removeEventListener(ModuleEvent.READY, reportLoadHandler);
            moduleInfo.removeEventListener(ModuleEvent.ERROR, moduleFailHandler);
            actualModule = moduleInfo.factory.create() as UIComponent;
            moduleInfo = null;
        }
        if (properties != null) {
            for (var p:String in properties) {
                actualModule[p] = properties[p];
            }
        }
        if (_loadingDisplay != null) {
            removeChild(_loadingDisplay);
            _loadingDisplay.moduleInfo = null;
            _loadingDisplay = null;
        }
        addListeners(actualModule as EventDispatcher);
        addChild(actualModule as DisplayObject);
        if (actualModule is IPerspective) {
            IPerspective(actualModule).gotFocus();
        }
    }

    public function createAnalysisPopup():IPerspective {
        return this;
    }
}
}