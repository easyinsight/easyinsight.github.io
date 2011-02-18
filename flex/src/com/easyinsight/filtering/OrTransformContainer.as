/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/17/11
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import mx.events.DragEvent;
import mx.managers.DragManager;

public class OrTransformContainer extends TransformContainer {

    public var orFilter:OrFilter;
    public var parentFilter:OrFilterCanvas;

    public function OrTransformContainer() {
        minHeight = 30;
        minWidth = 80;
        setStyle("borderStyle", "solid");
        setStyle("borderThickness", 1);
        setStyle("borderColor", 0x999999);
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("paddingLeft", 5);
        setStyle("paddingRight", 5);
        setStyle("paddingTop", 5);
        setStyle("paddingBottom", 5);
    }

    override protected function addFilter(filter:IFilter):void {
        super.addFilter(filter);
        if (!_loadingFromReport) {
            orFilter.filters.addItem(filter.filterDefinition);
        }
    }

    override protected function getBorderColor():uint {
        return 0x999999;
    }

    override protected function dragOverHandler(event:DragEvent):void {
        DragManager.showFeedback(DragManager.MOVE);
    }

    override protected function dragDropHandler(event:DragEvent):void {
        super.dragDropHandler(event);
    }

    override protected function createChildren():void {
        super.createChildren();
        if (orFilter != null) {
            for each (var filter:FilterDefinition in orFilter.filters) {
                addFilterDefinition(filter);
            }
        }
        if (_loadingFromReport) {
            _loadingFromReport = false;
        } else {
            parentFilter.dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, orFilter, null, parentFilter));
        }
    }
}
}
