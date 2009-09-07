package com.easyinsight.pseudocontext {

import flash.events.MouseEvent;

import mx.containers.TitleWindow;
import mx.containers.VBox;
import mx.controls.Label;

public class PseudoContextWindow extends TitleWindow {

    private var items:Array;

    public function PseudoContextWindow() {
        super();
    }

    override protected function createChildren():void {
        super.createChildren();
        var box:VBox = new VBox();
        for (var i:int = 0; i < items.length; i++) {
            var item:PseudoContextItem = items[i];
            var label:Label = new Label();
            label.text = item.label;
            label.addEventListener(MouseEvent.CLICK, item.click);
            box.addChild(label);
        }
        addChild(box);
    }
}
}