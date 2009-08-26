package com.easyinsight.tokens {

import flash.events.MouseEvent;

import flash.net.URLRequest;

import mx.containers.HBox;
import mx.controls.Button;

public class UnconfiguredTokenControls extends HBox{

    private var configureButton:Button;

    private var tokenSpecification:TokenSpecification;

    override public function set data(val:Object):void {
        this.tokenSpecification = val as TokenSpecification;
    }

    override public function get data():Object {
        return this.tokenSpecification;
    }
    
    public function UnconfiguredTokenControls() {
        super();
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        if (configureButton == null) {
            configureButton = new Button();
            configureButton.label = "Authorize";
            configureButton.addEventListener(MouseEvent.CLICK, onClick);
        }
        addChild(configureButton);
    }

    private function onClick(event:MouseEvent):void {
        flash.net.navigateToURL(new URLRequest(tokenSpecification.urlToConfigure), "_self");
    }
}
}