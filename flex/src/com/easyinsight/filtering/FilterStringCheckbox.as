/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.framework.User;
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
        this.width = 290;
        this.cbWidth = 290;
        this.moveY = 4;
    }


    override protected function isSelected():Boolean {
        if(!data) return false;
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
        if (User.getInstance() != null && User.getInstance().defaultFontFamily != null && User.getInstance().defaultFontFamily != "") {
            checkbox.setStyle("fontFamily", User.getInstance().defaultFontFamily);
        }
    }

    [Bindable("dataChange")]
    override public function set data(val:Object):void {
        if(this.data)
            this.data.removeEventListener("selectedChanged", onSelectedChanged);
        super.data = val;
        if (val != null && checkbox != null) {
            this.data.addEventListener("selectedChanged", onSelectedChanged, false, 0, true);
            checkbox.label = val.label;
        }
    }

    private function onSelectedChanged(event:Event):void {
        this.checkbox.selected = isSelected();
    }
}
}
