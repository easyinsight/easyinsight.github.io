/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/30/11
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import flash.events.MouseEvent;

import mx.events.ItemClickEvent;

import spark.components.LabelItemRenderer;

import spark.components.supportClasses.ItemRenderer;

public class FilterListRenderer extends LabelItemRenderer {
    public function FilterListRenderer() {
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
