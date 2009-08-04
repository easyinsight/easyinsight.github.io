package com.easyinsight.util {
import mx.containers.Canvas;

public class AntiScrollCanvas extends Canvas{
    public function AntiScrollCanvas() {
        super();
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number): void {
if (verticalScrollBar ) unscaledWidth -= verticalScrollBar .measuredWidth;
if (horizontalScrollBar ) unscaledHeight -= horizontalScrollBar.measuredHeight;
super.updateDisplayList(unscaledWidth, unscaledHeight);
} 
}
}