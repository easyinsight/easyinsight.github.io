/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/30/11
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.filtering.RollingDateRangeFilterDefinition;

import flash.events.MouseEvent;

import mx.events.FlexEvent;

import spark.components.IconItemRenderer;
import spark.components.RadioButton;
import spark.components.RadioButtonGroup;

public class FilterRadioButton extends IconItemRenderer {

    private var radioButton:RadioButton;

    private var _filterDefinition:RollingDateRangeFilterDefinition;

    private var _radioGroup:RadioButtonGroup;

    public function FilterRadioButton() {
        addEventListener(FlexEvent.INITIALIZE, init);
    }

    public function set filterDefinition(value:RollingDateRangeFilterDefinition):void {
        _filterDefinition = value;
    }

    public function set radioGroup(value:RadioButtonGroup):void {
        _radioGroup = value;
    }

    private function init(event:FlexEvent):void {
        radioButton = new RadioButton();
        radioButton.addEventListener(MouseEvent.CLICK, onChange);
        radioButton.group = _radioGroup;
        radioButton.x = this.parent.width - 70;
        addChild(radioButton);
    }

    private function onChange(event:MouseEvent):void {
        if (radioButton.selected) {
            _filterDefinition.interval = int(data.interval);
        }
    }

    override public function set data(val:Object):void {
        super.data = val;
        radioButton.selected = _filterDefinition.interval == int(data.interval);
    }
}
}
