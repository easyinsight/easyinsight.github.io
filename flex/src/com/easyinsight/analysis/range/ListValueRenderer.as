package com.easyinsight.analysis.range {
import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.TextInput;
import mx.events.CollectionEvent;

public class ListValueRenderer extends HBox {

    private var range:RangeOption;
    private var _rangeOptions:ArrayCollection;

    private var rangeValueInput:TextInput;
    private var deleteButton:Button;



    public function ListValueRenderer() {
        super();
        rangeValueInput = new TextInput();
        
        deleteButton = new Button();
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(rangeValueInput);
        addChild(deleteButton);
    }

    [Bindable(event="rangeOptionsChanged")]
    public function get rangeOptions():ArrayCollection {
        return _rangeOptions;
    }

    public function set rangeOptions(value:ArrayCollection):void {
        if (_rangeOptions == value) return;
        _rangeOptions = value;
        _rangeOptions.addEventListener(CollectionEvent.COLLECTION_CHANGE, onChange);
        dispatchEvent(new Event("rangeOptionsChanged"));
    }

    private function onChange(event:CollectionEvent):void {
        var index:int = _rangeOptions.getItemIndex(range);

    }

    override public function set data(val:Object):void {
        range = val as RangeOption;
    }

    override public function get data():Object {
        return range;
    }
}
}