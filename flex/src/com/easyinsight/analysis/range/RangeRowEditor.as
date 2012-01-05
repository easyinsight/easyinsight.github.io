package com.easyinsight.analysis.range {
import com.easyinsight.skin.ImageConstants;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.TextInput;
import mx.validators.NumberValidator;
import mx.validators.Validator;

public class RangeRowEditor extends HBox {

    private var minValueInput:TextInput;
    private var maxValueInput:TextInput;
    private var deleteButton:Button;
    private var validators:Array;
    private var minValidator:NumberValidator;
    private var maxValidator:NumberValidator;

    private var rangeOption:RangeOption;



    public function RangeRowEditor() {
        super();
        minValueInput = new TextInput();
        minValueInput.width = 100;
        minValidator = new NumberValidator();
        minValidator.source = minValueInput;
        minValidator.property = "text";
        maxValueInput = new TextInput();
        maxValueInput.width = 100;
        maxValidator = new NumberValidator();
        maxValidator.property = "text";
        maxValidator.source = maxValueInput;
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        validators = [ minValidator, maxValidator ];
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

    public function validate():Array {
        return Validator.validateAll(validators);
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