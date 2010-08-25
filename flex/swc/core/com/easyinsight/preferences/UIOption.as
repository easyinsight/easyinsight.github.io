package com.easyinsight.preferences {
import flash.events.Event;
import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

public class UIOption extends EventDispatcher {

    public var key:String;
    public var label:String;
    public var children:ArrayCollection = new ArrayCollection();
    public var parent:UIOption;

    private var _selected:Boolean = true;
    private var _enabled:Boolean;


    [Bindable(event="enabledChanged")]
    public function get enabled():Boolean {
        return _enabled;
    }

    public function set enabled(value:Boolean):void {
        if (_enabled == value) return;
        _enabled = value;
        dispatchEvent(new Event("enabledChanged"));
    }

    [Bindable(event="selectedChanged")]
    public function get selected():Boolean {
        return _selected;
    }

    public function set selected(value:Boolean):void {
        if (_selected == value) return;
        _selected = value;
        dispatchEvent(new Event("selectedChanged"));
    }

    public function UIOption(key:String, label:String, children:Array) {
        this.key = key;
        this.label = label;
        this.children = new ArrayCollection(children);
        for each (var child:UIOption in children) {
            child.parent = this;
        }
    }
}
}
