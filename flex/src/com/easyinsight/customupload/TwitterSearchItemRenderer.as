package com.easyinsight.customupload {
import flash.events.Event;
import flash.events.TextEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
import mx.controls.TextInput;

public class TwitterSearchItemRenderer extends HBox {

    private var textInput:TextInput;
    private var deleteButton:Button;

    [Bindable]
    private var info:TwitterInfo;

    private var _dataString:String;

    [Bindable]
    override public function set data(value:Object):void {
        info = value as TwitterInfo;
        _dataString = info.search;
    }

    override public function get data():Object {
        return info;
    }

    public function TwitterSearchItemRenderer() {
        super();
        textInput = new TextInput();
        deleteButton = new Button();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(textInput);
        addChild(deleteButton);
        BindingUtils.bindProperty(textInput, "text", this, "dataString");
        textInput.addEventListener(TextEvent.TEXT_INPUT, onTextInput);
    }

    private function onTextInput(event:TextEvent):void {
        info.search = textInput.text + event.text;
        _dataString = info.search;
    }

    [Bindable(event="dataStringChanged")]
    public function get dataString():String {
        return _dataString;
    }

    public function set dataString(value:String):void {
        if (_dataString == value) return;
        _dataString = value;
        dispatchEvent(new Event("dataStringChanged"));
    }
}
}