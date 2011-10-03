/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/30/11
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.filtering.FilterValueDefinition;

import flash.events.MouseEvent;

import mx.events.FlexEvent;
import mx.events.ItemClickEvent;

import spark.components.CheckBox;
import spark.components.IconItemRenderer;
import spark.components.LabelItemRenderer;
import spark.components.supportClasses.ItemRenderer;

public class FilterListEntry extends LabelItemRenderer {

    public function FilterListEntry() {
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
