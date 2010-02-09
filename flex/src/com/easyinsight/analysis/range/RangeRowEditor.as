package com.easyinsight.analysis.range {
import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.TextInput;

public class RangeRowEditor extends HBox {

    private var minValueInput:TextInput;
    private var maxValueInput:TextInput;
    private var deleteButton:Button;

    private var rangeOption:RangeOption;

    [Bindable]
    [Embed(source="../../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    public function RangeRowEditor() {
        super();
        minValueInput = new TextInput();
        minValueInput.width = 100;
        maxValueInput = new TextInput();
        maxValueInput.width = 100;
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new RangeEvent(RangeEvent.DELETE_RANGE, rangeOption));
    }

    protected override function commitProperties():void {
        super.commitProperties();
        minValueInput.text = String(rangeOption.rangeMinimum);
        maxValueInput.text = String(rangeOption.rangeMaximum);
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(minValueInput);
        addChild(maxValueInput);
        addChild(deleteButton);
    }

    public function save():void {
        rangeOption.rangeMinimum = Number(minValueInput.text);
        rangeOption.rangeMaximum = Number(maxValueInput.text);
    }

    override public function set data(val:Object):void {
        this.rangeOption = val as RangeOption;
    }

    override public function get data():Object {
        return this.rangeOption;
    }
}
}