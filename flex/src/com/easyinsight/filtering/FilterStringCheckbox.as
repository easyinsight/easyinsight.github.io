/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.util.GridCheckbox;

import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.containers.Canvas;

import mx.containers.HBox;
import mx.controls.Alert;

import mx.controls.CheckBox;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;

public class FilterStringCheckbox extends GridCheckbox {


    public function FilterStringCheckbox() {
        super();
        this.width = 140;
        this.cbWidth = 140;
        this.moveY = 4;
    }


    override protected function isSelected():Boolean {
        return data.selected;
    }


    override protected function updateSelectedOnObject(selected:Boolean):void {
        data.selected = selected;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(checkbox);
    }

    [Bindable("dataChange")]
    override public function set data(val:Object):void {
        super.data = val;
        checkbox.label = val.label;
    }


    override public function set enabled(value:Boolean):void {
        if(checkbox && checkbox.parent)
            checkbox.enabled = value;
        super.enabled = value;
    }
}
}
