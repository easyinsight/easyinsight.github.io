package com.easyinsight.tour {
import flash.display.DisplayObjectContainer;

public class SetNoteProperty extends TutorialElement {
    public function SetNoteProperty(anchor:String, property:String, value:Object) {
        this.anchor = anchor;
        this.property = property;
        this.value = value;
    }

    private var _property:String;

    private var _value:Object;

    private var _previousValue:Object;

    public function get property():String {
        return _property;
    }

    public function set property(value:String):void {
        _property = value;
    }

    public function get value():Object {
        return _value;
    }

    public function set value(value:Object):void {
        _value = value;
    }

    override public function forwardExecute():void {
        var component:DisplayObjectContainer = findComponent(anchor);
        _previousValue = component[_property];
        component[_property] = value;
    }

    override public function backwardExecute():void {
        var component:DisplayObjectContainer = findComponent(anchor);
        component[_property] = _previousValue;
    }
}
}