package com.easyinsight.detail {

import flash.events.MouseEvent;

import flash.system.System;

import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.TextArea;

public class DetailRenderer extends HBox {

    private var detailValue:Label;
    private var detailArea:TextArea;
    private var copyButton:Button;
    [Embed(source="../../../../assets/clipboard.png")]
    private var copyIcon:Class;

    private var _value:String;

    public function set value(val:String):void {
        _value = val;
    }

    public function DetailRenderer() {
    }

    private function copy(event:MouseEvent):void {
        System.setClipboard(_value);
    }

    override protected function createChildren():void {
        super.createChildren();
        if (copyButton == null) {
            copyButton = new Button();
            copyButton.setStyle("icon", copyIcon);
            copyButton.toolTip = "Copy to Clipboard";
            copyButton.addEventListener(MouseEvent.CLICK, copy);
        }
        addChild(copyButton);
        if (detailValue == null) {
            detailValue = new Label();
            detailValue.setStyle("fontFamily", "Lucida Grande");
            detailValue.setStyle("fontWeight", "normal");
        }
        if (_value.length > 40) {
            detailArea = new TextArea();
            detailArea.setStyle("borderStyle", "none");
            detailArea.setStyle("backgroundAlpha", 0);
            detailArea.setStyle("percentWidth", 100);
            detailArea.text = _value;
            detailArea.selectable = false;
            addChild(detailArea);
        } else {
            detailValue.text = _value;
            addChild(detailValue);            
        }
    }


}
}