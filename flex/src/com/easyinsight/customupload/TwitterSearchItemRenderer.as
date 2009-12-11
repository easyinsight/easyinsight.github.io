package com.easyinsight.customupload {
import flash.events.TextEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.TextInput;

public class TwitterSearchItemRenderer extends HBox {

    [Bindable]
    public var dataString:String;
    private var textInput:TextInput;

    [Bindable]
    override public function set data(value:Object):void {
        dataString = value as String;
    }

    override public function get data():Object {
        return dataString;
    }

    public function TwitterSearchItemRenderer() {
        super();
        textInput = new TextInput();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(textInput);
        BindingUtils.bindProperty(textInput, "text", this, "data");
        textInput.addEventListener(TextEvent.TEXT_INPUT, onTextInput);
    }

    private function onTextInput(event:TextEvent):void {
        dataString = textInput.text;
    }
}
}