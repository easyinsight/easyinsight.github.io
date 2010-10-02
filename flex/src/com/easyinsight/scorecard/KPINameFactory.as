package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPIIconRenderer;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.binding.utils.BindingUtils;
import mx.core.IFactory;

public class KPINameFactory extends EventDispatcher implements IFactory {

    private var _contextMenuAvailable:Boolean;

    private var _rendererClass:String;

    public function KPINameFactory(rendererClass:String) {
        this._rendererClass = rendererClass;
    }

    [Bindable(event="contextMenuAvailableChanged")]
    public function get contextMenuAvailable():Boolean {
        return _contextMenuAvailable;
    }

    public function set contextMenuAvailable(value:Boolean):void {
        if (_contextMenuAvailable == value) return;
        _contextMenuAvailable = value;
        dispatchEvent(new Event("contextMenuAvailableChanged"));
    }

    public function set rendererClass(value:String):void {
        _rendererClass = value;
    }

    public function newInstance():* {
        var renderer:*;
        if (_rendererClass == "name") {
            renderer = new KPINameRenderer();
        } else if (_rendererClass == "icon") {
            renderer = new KPIIconRenderer();
        } else if (_rendererClass == "value") {
            renderer = new KPIValueRenderer();
        } else if (_rendererClass == "time") {
            renderer = new KPITimeRenderer();
        } else if (_rendererClass == "change") {
            renderer = new KPIChangeRenderer();
        } else if (_rendererClass == "status") {
            renderer = new KPIStatusRenderer();
        }
        BindingUtils.bindProperty(renderer, "contextMenuAvailable", this, "contextMenuAvailable");
        return renderer;
    }
}
}