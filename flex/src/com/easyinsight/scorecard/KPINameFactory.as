package com.easyinsight.scorecard {
import flash.events.Event;
import flash.events.EventDispatcher;

import mx.binding.utils.BindingUtils;
import mx.core.IFactory;

public class KPINameFactory extends EventDispatcher implements IFactory {

    private var _contextMenuAvailable:Boolean;

    public function KPINameFactory() {
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

    public function newInstance():* {
        var kpiNameRenderer:KPINameRenderer = new KPINameRenderer();
        BindingUtils.bindProperty(kpiNameRenderer, "contextMenuAvailable", this, "contextMenuAvailable");
        return kpiNameRenderer;
    }
}
}