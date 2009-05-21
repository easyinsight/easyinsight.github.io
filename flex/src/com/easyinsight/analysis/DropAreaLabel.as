package com.easyinsight.analysis {
import flash.events.MouseEvent;

import mx.controls.Label;
import mx.events.FlexEvent;

public class DropAreaLabel extends Label {

    private var _assigned:Boolean;
    private var _selected:Boolean;

    public function DropAreaLabel() {
        super();
    }

    [Bindable]
    public function get assigned():Boolean {
        return _assigned;
    }

    public function set assigned(val:Boolean):void {
        _assigned = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    [Bindable]
    public function get selected():Boolean {
        return _selected;
    }

    public function set selected(val:Boolean):void {
        _selected = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    private function onClick(event:MouseEvent):void {
        selected = !selected;
    }
}
}