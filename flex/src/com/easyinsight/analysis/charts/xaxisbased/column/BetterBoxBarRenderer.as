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
import mx.controls.Alert;
import mx.controls.Label;
import mx.core.IDataRenderer;
import mx.core.UIComponent;
import mx.graphics.IFill;
import mx.graphics.IStroke;
import mx.graphics.SolidColor;
import mx.styles.StyleManager;
import mx.utils.ColorUtil;

public class BetterBoxBarRenderer extends UIComponent implements IDataRenderer {
    public function BetterBoxBarRenderer() {
    }

    private var _chartItem:ChartItem;

    private var _labelFontSize:int;
    private var _labelFontColor:uint;
    private var _labelFontWeight:String;


    public function get labelFontSize():int {
        return _labelFontSize;
    }

    public function set labelFontSize(value:int):void {
        _labelFontSize = value;
    }

    public function get labelFontColor():uint {
        return _labelFontColor;
    }

    public function set labelFontColor(value:uint):void {
        _labelFontColor = value;
    }

    public function get labelFontWeight():String {
        return _labelFontWeight;
    }

    public function set labelFontWeight(value:String):void {
        _labelFontWeight = value;
    }

    public function set data(value:Object):void
    {
        if (_chartItem == value) return;
        // setData also is executed if there is a Legend Data
        // defined for the chart. We validate that only chartItems are
        // assigned to the chartItem class.
        if (value is LegendData)
            return;
        _chartItem = ChartItem(value);
    }

    public function get data():Object
    {
        return _chartItem;
    }

    private var label:Label;

    override protected function createChildren():void
    {
        super.createChildren();
        if (label == null)
        {
            label = new Label();
            label.truncateToFit = true;
            label.setStyle("fontSize", _labelFontSize);
            label.setStyle("textAlign", "center");
            label.setStyle("fontWeight", _labelFontWeight);
            addChild(label);
        }
    }

    override protected function updateDisplayList(unscaledWidth:Number,
                                                  unscaledHeight:Number):void
    {
        super.updateDisplayList(unscaledWidth, unscaledHeight);

        var fill:IFill;
        var state:String = "";

        if(data is ChartItem && data.hasOwnProperty('fill'))
        {
            state = data.currentState;
            fill = data.fill;
        }
        else
            fill = GraphicsUtilities.fillFromStyle(getStyle('fill'));

        var color:uint;
        var adjustedRadius:Number = 0;

        switch(state)
        {
            case ChartItem.FOCUSED:
            case ChartItem.ROLLOVER:
                if(StyleManager.isValidStyleValue(getStyle('itemRollOverColor')))
                    color = getStyle('itemRollOverColor');
                else
                    color = ColorUtil.adjustBrightness2(GraphicsUtilities.colorFromFill(fill),-20);
                fill = new SolidColor(color);
                adjustedRadius = getStyle('adjustedRadius');
                if(!adjustedRadius)
                    adjustedRadius = 0;
                break;
            case ChartItem.DISABLED:
                if(StyleManager.isValidStyleValue(getStyle('itemDisabledColor')))
                    color = getStyle('itemDisabledColor');
                else
                    color = ColorUtil.adjustBrightness2(GraphicsUtilities.colorFromFill(fill),20);
                fill = new SolidColor(GraphicsUtilities.colorFromFill(color));
                break;
            case ChartItem.FOCUSEDSELECTED:
            case ChartItem.SELECTED:
                if(StyleManager.isValidStyleValue(getStyle('itemSelectionColor')))
                    color = getStyle('itemSelectionColor');
                else
                    color = ColorUtil.adjustBrightness2(GraphicsUtilities.colorFromFill(fill),-30);
                fill = new SolidColor(color);
                adjustedRadius = getStyle('adjustedRadius');
                if(!adjustedRadius)
                    adjustedRadius = 0;
                break;
        }

        var stroke:IStroke = getStyle("stroke");

        var w:Number = stroke ? stroke.weight / 2 : 0;

        var rc:Rectangle = new Rectangle(w - adjustedRadius, w - adjustedRadius, width - 2 * w + adjustedRadius * 2, height - 2 * w + adjustedRadius * 2);

        var g:Graphics = graphics;
        g.clear();
        g.moveTo(rc.left,rc.top);
        if (stroke)
            stroke.apply(g);
        if (fill)
            fill.begin(g,rc);
        g.lineTo(rc.right,rc.top);
        g.lineTo(rc.right,rc.bottom);
        g.lineTo(rc.left,rc.bottom);
        g.lineTo(rc.left,rc.top);
        if (fill)
            fill.end(g);

        if (_chartItem == null) // _chartItem has no data
            return;

        var cs:BarSeries = _chartItem.element as BarSeries;
        var csi:BarSeriesItem = _chartItem as BarSeriesItem;

        // set the label
        label.text = csi.item[cs.xField].toString();
        label.width = label.maxWidth = label.measureText(label.text).width + 5;

        label.height = label.textHeight;
        var fontSize:int = label.getStyle("fontSize");
        label.y = unscaledHeight / 2 - fontSize / 2 - 3;
        var labelWidth:int = label.width + 10;

        // label's default y is 0. if the bar is too short we need to move it up a bit
        var barXpos:Number = csi.x;
        var minXpos:Number = csi.min;
        var barWidth:Number = barXpos - minXpos;
        var labelColor:uint = 0xFFFFFF; // white

        if (barWidth < labelWidth) // if no room for label
        {
            // nudge label up the amount of pixels missing
            label.x = barWidth + 3;

            labelColor = 0x222222; // label will appear on white background, so make it dark
        }
        else
        {
            // center the label vertically in the bar
            label.x = barWidth / 2 - labelWidth / 2;
            labelColor = 0x222222; // label will appear on white background, so make it dark
        }

        label.setStyle("color", labelColor);
    }
}
}
