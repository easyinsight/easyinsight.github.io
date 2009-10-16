package com.easyinsight.util {
import mx.containers.VBox;
import mx.controls.Label;

public class LoadingWindow extends VBox{

    public function LoadingWindow() {
        super();
        setStyle("backgroundColor", 0xFFFFFF);
    }

    override protected function createChildren():void {
        super.createChildren();
        var label:Label = new Label();
        label.text = "Retrieving data...";
        addChild(label);
    }
}
}