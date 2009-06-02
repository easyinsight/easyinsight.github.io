package com.easyinsight.dashboard {
import flash.display.DisplayObject;

import flash.events.EventDispatcher;

import mx.core.Container;

public class AirWidgetItem extends EventDispatcher {

    public var name:String;

    public var _displayObject:Container;

    public function AirWidgetItem() {
    }

    public function get displayObject():Container {
        return _displayObject;
    }

    public function set displayObject(value:Container):void {
        _displayObject = value;
    }

    public function createDisplayObject():Container {
        return null;
    }

    public function refreshData():void {
    }}
}