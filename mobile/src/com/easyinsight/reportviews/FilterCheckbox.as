/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/30/11
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.filtering.FilterValueDefinition;

import flash.events.MouseEvent;

import mx.events.FlexEvent;

import spark.components.CheckBox;
import spark.components.IconItemRenderer;

public class FilterCheckbox extends IconItemRenderer {

    private var checkbox:CheckBox;

    private var _filterDefinition:FilterValueDefinition;

    private var value:String;

    public function FilterCheckbox() {
        addEventListener(FlexEvent.INITIALIZE, init);
    }

    public function set filterDefinition(value:FilterValueDefinition):void {
        _filterDefinition = value;
    }

    private function init(event:FlexEvent):void {
        checkbox = new CheckBox();
        checkbox.addEventListener(MouseEvent.CLICK, onChange);
        checkbox.x = this.parent.width - 70;
        addChild(checkbox);
    }

    private function onChange(event:MouseEvent):void {
        if (checkbox.selected) {
            _filterDefinition.filteredValues.addItem(value);
        } else {
            _filterDefinition.filteredValues.removeItemAt(_filterDefinition.filteredValues.getItemIndex(value));
        }
    }

    override public function set data(val:Object):void {
        super.data = val;
        value = val as String;
        checkbox.selected = _filterDefinition.filteredValues.getItemIndex(value) != -1;
    }
}
}
