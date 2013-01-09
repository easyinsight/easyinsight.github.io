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
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class GridCheckbox extends UIComponent implements IListItemRenderer {


    protected var checkbox:CheckBox;

    public function GridCheckbox() {
        super();
    }

    private function onChange(event:Event):void {
        updateSelectedOnObject(checkbox.selected);
    }

    protected function isSelected():Boolean {
        return false;
    }

    protected function updateSelectedOnObject(selected:Boolean):void {
    }

    override protected function createChildren():void {
        super.createChildren();
        if (checkbox == null) {
            checkbox = new CheckBox();
            checkbox.addEventListener(Event.CHANGE, onChange);
            addChild(checkbox);
        }
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (checkbox != null) {
            checkbox.move(8,0);
            checkbox.setActualSize(16, 16);
        }
    }

    private var dataVal:Object;

    [Bindable("dataChange")]
    public function set data(val:Object):void {
        dataVal = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
        if (checkbox != null) {
            checkbox.selected = isSelected();
        }
    }

    public function get data():Object {
        return dataVal;
    }
}
}
