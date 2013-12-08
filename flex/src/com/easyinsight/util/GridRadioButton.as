/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/14/12
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.Event;

import mx.controls.CheckBox;
import mx.controls.RadioButton;
import mx.controls.RadioButtonGroup;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class GridRadioButton extends UIComponent implements IListItemRenderer {
    protected var moveX:int = 8;
    protected var moveY:int = 0;
    protected var cbWidth:int = 16;
    protected var cbHeight:int = 16;

    protected var radioButton:RadioButton;

    public var radioGroup:RadioButtonGroup;

    public function GridRadioButton() {
        super();
    }

    private function onChange(event:Event):void {
        updateSelectedOnObject(radioButton.selected);
    }

    protected function isSelected():Boolean {
        return false;
    }

    protected function updateSelectedOnObject(selected:Boolean):void {
    }

    override protected function createChildren():void {
        super.createChildren();
        if (radioButton == null) {
            radioButton = new RadioButton();
            radioButton.group = radioGroup;
            radioButton.addEventListener(Event.CHANGE, onChange);
            addChild(radioButton);
        }
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (radioButton != null) {
            radioButton.move(moveX, moveY);
            radioButton.setActualSize(cbWidth, cbHeight);
        }
    }

    private var dataVal:Object;

    [Bindable("dataChange")]
    public function set data(val:Object):void {
        dataVal = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
        if (radioButton != null) {
            radioButton.selected = isSelected();
        }
    }

    public function get data():Object {
        return dataVal;
    }
}
}
