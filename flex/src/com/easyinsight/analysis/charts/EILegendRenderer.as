/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/2/12
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts {
import mx.charts.LegendItem;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;

public class EILegendRenderer extends UIComponent implements IListItemRenderer {
    public function EILegendRenderer() {
    }
    
    public function set data(legendData:Object):void {
        if (newItem != null) {
            removeChild(newItem);
        }
        if (legendData == null) {
            return;
        }

        var c:Class = EILegendItem;
        newItem = new c();

        newItem.marker = legendData.marker;

        if (legendData.label != "")
            newItem.label = legendData.label;

        if (legendData.element)
            newItem.element = legendData.element;

        if ("fill" in legendData)
            newItem.setStyle("fill",legendData["fill"]);

        newItem.markerAspectRatio = legendData.aspectRatio;
        newItem.legendData = legendData;
        newItem.percentWidth = 100;
        newItem.percentHeight = 100;

        addChild(newItem);

        newItem.setStyle("backgroundColor", 0xEEEEFF);
    }
    
    private var newItem:LegendItem;

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (newItem != null) {
            newItem.setActualSize(unscaledWidth, newItem.getExplicitOrMeasuredHeight());
        }
    }

    public function get data():Object {
        return null;
    }
}
}
