/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/7/13
 * Time: 10:01 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import mx.containers.Canvas;

public class HeaderCanvas extends Canvas {
    public function HeaderCanvas() {
        styleName = "dashboardStackGradientBackground";
    }

    /*override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        graphics.beginFill(0x0);
        graphics.drawRoundRectComplex(0, 0, unscaledWidth, unscaledHeight, 8, 8, 0, 0);
        graphics.endFill();
    }*/
}
}
