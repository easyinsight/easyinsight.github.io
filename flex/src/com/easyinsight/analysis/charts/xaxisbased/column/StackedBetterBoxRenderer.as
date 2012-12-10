/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/7/12
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts.xaxisbased.column {
import flash.display.Graphics;
import flash.geom.Rectangle;

import mx.charts.ChartItem;
import mx.charts.chartClasses.GraphicsUtilities;
import mx.charts.chartClasses.LegendData;
import mx.charts.series.ColumnSeries;
import mx.charts.series.items.ColumnSeriesItem;
import mx.controls.Label;
import mx.core.IDataRenderer;
import mx.core.UIComponent;
import mx.formatters.Formatter;
import mx.graphics.IFill;
import mx.graphics.IStroke;
import mx.graphics.SolidColor;
import mx.styles.StyleManager;
import mx.utils.ColorUtil;

public class StackedBetterBoxRenderer extends BetterBoxRenderer {
    public function StackedBetterBoxRenderer() {
    }


    override protected function handle():void {
        var csi:ColumnSeriesItem = chartItem as ColumnSeriesItem;

        // set the label
        label.text = formatter.format(csi.yNumber);

        label.width = label.maxWidth = unscaledWidth;

        label.height = label.textHeight;
        var labelHeight:int = label.textHeight + 3;

        // label's default y is 0. if the bar is too short we need to move it up a bit
        var barYpos:Number = csi.y;
        var minYpos:Number = csi.min;
        var barHeight:Number = minYpos - barYpos;
        var labelColor:uint = labelInsideFontColor;

        if (barHeight < labelHeight) // if no room for label
        {
            label.height = 0;
        }
        else
        {
            // center the label vertically in the bar
            label.y = barHeight / 2 - labelHeight / 2;
        }

        label.setStyle("color", labelColor);
    }
}
}
