package com.easyinsight.framework {
import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.controls.Image;
import mx.controls.Label;

[Style(name="rolloverColor", type="uint", format="Color", inherit="yes")]

public class CorePageButton extends HBox {

    private var _text:String;
    private var _image:Class;
    private var _messageListener:EIMessageListener;

    private var baseColor:uint;

    private var buttonLabel:Label;
    private var buttonIcon:Image;

    private var mousedOver:Boolean;

    public function CorePageButton() {
        super();
        setStyle("verticalAlign", "middle");
        addEventListener(MouseEvent.MOUSE_OVER, onMouseOver);
    }

    public function set messageListener(value:EIMessageListener):void {
        if (value == _messageListener) {
            return;
        }
        _messageListener = value;
        onMessageListener(value);
    }

    protected function onMessageListener(value:EIMessageListener):void {
    }

    private function onMouseOver(event:MouseEvent):void {
        mousedOver = true;
        removeEventListener(MouseEvent.MOUSE_OVER, onMouseOver);
        addEventListener(MouseEvent.MOUSE_OUT, onMouseOut);
        baseColor = getStyle("backgroundColor");
        setStyle("backgroundColor", getStyle("rolloverColor"));
    }

    private function onMouseOut(event:MouseEvent):void {
        mousedOver = false;
        removeEventListener(MouseEvent.MOUSE_OUT, onMouseOut);        
        addEventListener(MouseEvent.MOUSE_OVER, onMouseOver);
        setStyle("backgroundColor", baseColor);
    }

    private function onMouseClick(event:MouseEvent):void {

    }

    [Bindable(event="textChanged")]
    public function get text():String {
        return _text;
    }

    public function set text(value:String):void {
        if (_text == value) return;
        _text = value;
        dispatchEvent(new Event("textChanged"));
    }

    [Bindable(event="imageChanged")]
    public function get image():Class {
        return _image;
    }

    public function set image(value:Class):void {
        if (_image == value) return;
        _image = value;
        dispatchEvent(new Event("imageChanged"));
    }

    override protected function createChildren():void {
        super.createChildren();
        if (buttonLabel == null) {
            buttonLabel = new Label();
            BindingUtils.bindProperty(buttonLabel, "text", this, "text");
        }
        if (buttonIcon == null) {
            buttonIcon = new Image();
            BindingUtils.bindProperty(buttonIcon, "source", this, "image");
        }
        addChild(buttonIcon);
        addChild(buttonLabel);
    }
}
}