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
import mx.charts.series.BarSeries;
import mx.charts.series.items.BarSeriesItem;
import mx.controls.Label;
import mx.core.IDataRenderer;
import mx.core.UIComponent;
import mx.formatters.Formatter;
import mx.graphics.IFill;
import mx.graphics.IStroke;
import mx.graphics.SolidColor;
import mx.styles.StyleManager;
import mx.utils.ColorUtil;

public class StackedBetterBoxBarRenderer extends BetterBoxRenderer {
    public function StackedBetterBoxBarRenderer() {
    }


    override protected function handle():void
    {


        var csi:BarSeriesItem = chartItem as BarSeriesItem;

        // set the label
        label.text = formatter.format(csi.xNumber);
        //label.text = cs.xField;
        label.width = label.measureText(label.text).width + 5;

        label.height = label.textHeight;
        var fontSize:int = label.getStyle("fontSize");
        label.y = unscaledHeight / 2 - fontSize / 2 - 3;
        var labelWidth:int = label.width + 10;

        // label's default y is 0. if the bar is too short we need to move it up a bit
        var barXpos:Number = csi.x;
        var minXpos:Number = csi.min;
        var barWidth:Number = barXpos - minXpos;
        var labelColor:uint = labelInsideFontColor; // white

        if (barWidth < labelWidth) // if no room for label
        {
            label.width = 0;
        }
        else
        {
            // center the label vertically in the bar
            label.x = barWidth / 2 - labelWidth / 2;
            //labelColor = 0x222222; // label will appear on white background, so make it dark
        }

        label.setStyle("color", labelColor);
    }
}
}
