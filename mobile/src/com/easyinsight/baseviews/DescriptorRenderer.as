/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/10/11
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.baseviews {
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.solutions.DataSourceDescriptor;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.MouseEvent;

import mx.events.ItemClickEvent;

import spark.components.IconItemRenderer;

public class DescriptorRenderer extends IconItemRenderer {

    [Embed(source="../../../../assets/chart_column.png")]
    private var chartIcon:Class;

    [Embed(source="../../../../assets/data_blue_x16.png")]
    private var dataIcon:Class;

    [Embed(source="../../../../assets/presentation_chart.png")]
    private var dashboardIcon:Class;

    [Embed(source="../../../../assets/arrow2_up_green.png")]
    private var scorecardIcon:Class;

    public function DescriptorRenderer() {
        addEventListener(MouseEvent.CLICK, onClick);
        this.labelField = "name";
        this.iconFunction = myIconFunction;
    }

    private function myIconFunction(item:Object):Object {
        var eiDesc:EIDescriptor = item as EIDescriptor;
        if (eiDesc == null) {
            return null;
        }
        if (eiDesc.getType() == EIDescriptor.REPORT) {
            return chartIcon;
        } else if (eiDesc.getType() == EIDescriptor.DATA_SOURCE) {
            return dataIcon;
        } else if (eiDesc.getType() == EIDescriptor.SCORECARD) {
            return scorecardIcon;
        } else if (eiDesc.getType() == EIDescriptor.DASHBOARD) {
            return dashboardIcon;
        }
        return null;
    }

    private function onClick(event:MouseEvent):void {
        var e:ItemClickEvent = new ItemClickEvent(ItemClickEvent.ITEM_CLICK, true);
        e.item = data;
        e.index = itemIndex;
        dispatchEvent(e);
    }
}
}
