/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/30/11
 * Time: 10:08 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.list {
import mx.skins.ProgrammaticSkin;

public class HeaderProgrammaticSkin extends ProgrammaticSkin {
    public function HeaderProgrammaticSkin() {
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        graphics.beginFill(0, 1.0);
        graphics.drawRect(0, 0, this.width, this.height);
        graphics.endFill();
    }
}
}
