/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/2/12
 * Time: 9:20 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.Link;

import mx.charts.GridLines;

import mx.charts.chartClasses.ChartBase;

import mx.charts.events.ChartItemEvent;
import mx.graphics.Stroke;

public class ChartUtil {
    public function ChartUtil() {
    }

    public static function gridLine(direction:String):Array {
        var gridLines:GridLines = new GridLines();
        gridLines.setStyle("horizontalChangeCount", 1);
        gridLines.setStyle("verticalChangeCount", 1);
        gridLines.setStyle("horizontalStroke", new Stroke(0x333333, 1, .25));
        gridLines.setStyle("verticalStroke", new Stroke(0x333333, 1, .25));
        gridLines.setStyle("direction", direction);
        return [ gridLines ];
    }
    
    public static function axisStroke():Stroke {
        return new Stroke(0x666666, 2, 1);
    }

    public static function setup(dimensionAxisItem:AnalysisItem,  chart:ChartBase, onChartClick:Function):Link {
        if (dimensionAxisItem.links != null && dimensionAxisItem.links.length > 0) {
            var defaultLink:Link = null;
            for each (var link:Link in dimensionAxisItem.links) {
                if (link.defaultLink) {
                    defaultLink = link;
                    break;
                }
            }

            if (defaultLink) {
                chart.selectionMode = "single";
                chart.addEventListener(ChartItemEvent.ITEM_CLICK, onChartClick);
            } else {
                chart.selectionMode = "none";
                chart.removeEventListener(ChartItemEvent.ITEM_CLICK, onChartClick);
            }
            return defaultLink;
        }
        return null;
    }
}
}
