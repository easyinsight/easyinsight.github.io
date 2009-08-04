package com.easyinsight.util {
import mx.containers.Canvas;
import mx.containers.VBox;

public class AntiScrollVBox extends VBox{
    public function AntiScrollVBox() {
        super();
    }

    override public function get measuredWidth():Number{
        var out:Number = width;
        if (verticalScrollBar){
            out -= verticalScrollBar.width;
        }
        return out;
    }
}
}