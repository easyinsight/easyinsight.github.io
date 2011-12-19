/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/18/11
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import flash.events.MouseEvent;

import mx.events.ItemClickEvent;

import spark.components.LabelItemRenderer;

public class TreeListRenderer extends LabelItemRenderer {
    public function TreeListRenderer() {
        super();
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        var e:ItemClickEvent = new ItemClickEvent(ItemClickEvent.ITEM_CLICK, true);
        e.item = data;
        e.index = itemIndex;
        dispatchEvent(e);
    }
}
}
