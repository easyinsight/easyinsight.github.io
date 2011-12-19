/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/15/11
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import flash.events.MouseEvent;

import spark.components.LabelItemRenderer;

public class MobileContextMenuItemRenderer extends LabelItemRenderer {
    public function MobileContextMenuItemRenderer() {
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private var mobileContextItem:MobileContextMenuItem;

    override public function set data(val:Object):void {
        mobileContextItem = val as MobileContextMenuItem;
    }

    private function onClick(event:MouseEvent):void {
        mobileContextItem.dispatchEvent(new MobileContextMenuEvent());
    }
}
}
