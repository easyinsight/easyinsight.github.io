package com.easyinsight.customupload.api {
import flash.events.MouseEvent;

import flash.system.System;

import mx.containers.HBox;
import mx.controls.Button;

public class CopyAPIButton extends HBox{

    [Embed(source="../../../../../assets/clipboard.png")]
    private var copyIcon:Class;

    private var apiDescriptor:DataSourceAPIDescriptor;

    private var copyButton:Button;

    public function CopyAPIButton() {
        super();
        this.percentWidth = 100;
        setStyle("horizontalAlign", "center");
        copyButton = new Button();
        copyButton.toolTip = "Copy API Key to Clipboard";
        copyButton.addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        System.setClipboard(apiDescriptor.apiKey);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(copyButton);
    }

    override public function set data(val:Object):void {
        this.apiDescriptor = val as DataSourceAPIDescriptor;
    }

    override public function get data():Object {
        return this.apiDescriptor;
    }
}
}