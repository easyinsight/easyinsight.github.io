package com.easyinsight.analysis {
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;

import mx.controls.ColorPicker;
import mx.controls.LinkButton;
import mx.managers.PopUpManager;

public class MultiColorReportFormItem extends ReportFormItem {

    public var button:LinkButton;

    public function MultiColorReportFormItem(label:String = null, property:String = null, value:Object = null, report:Object = null, enabledProperty:String = null) {
        super(label, property, value, report, enabledProperty);
    }

    private var colors:ArrayCollection;

    protected override function createChildren():void {
        super.createChildren();

        button = new LinkButton();
        button.setStyle("left", 0);
        button.setStyle("paddingLeft", 0);
        button.label = "Edit...";
        button.addEventListener(MouseEvent.CLICK, onClick);

        if (this.value != null) colors = ArrayCollection(this.value);
        addChild(button);
    }

    private function onClick(event:MouseEvent):void {
        var window:MultiColorWindow = new MultiColorWindow();
        window.multiColors = colors;
        window.addEventListener(MultiColorEvent.MULTI_COLOR, onMultiColor, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onMultiColor(event:MultiColorEvent):void {
        colors = event.colors;
    }

    override protected function getValue():Object {
        return colors;
    }
}
}