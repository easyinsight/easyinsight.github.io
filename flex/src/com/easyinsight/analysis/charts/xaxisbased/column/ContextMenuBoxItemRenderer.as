/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/11/11
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.display.Graphics;
import flash.events.Event;
import flash.geom.Rectangle;

import mx.charts.ChartItem;
import mx.charts.chartClasses.GraphicsUtilities;

import mx.core.IDataRenderer;
import mx.core.UIComponent;
import mx.graphics.IFill;
import mx.graphics.IStroke;
import mx.graphics.SolidColor;
import mx.styles.StyleManager;
import mx.utils.ColorUtil;

public class ContextMenuBoxItemRenderer extends UIComponent implements IDataRenderer {
    public function ContextMenuBoxItemRenderer() {
    }

    private var _stackItem:AnalysisItem;

    private var _report:AnalysisDefinition;

    public function set report(value:AnalysisDefinition):void {
        _report = value;
        invalidateProperties();
    }

    public function set stackItem(value:AnalysisItem):void {
        _stackItem = value;
        invalidateProperties();
    }

    private var _seriesField:String;

    public function set seriesField(value:String):void {
        _seriesField = value;
    }

    private var _data:Object;

	[Inspectable(environment="none")]

   	/**
	 *  The chartItem that this itemRenderer is displaying.
	 *  This value is assigned by the owning series
	 */
	public function get data():Object
	{
		return _data;
	}

	/**
	 *  @private
	 */
	public function set data(value:Object):void
	{
		if (_data == value)
			return;
		_data = value;
        invalidateProperties();
	}


    override protected function commitProperties():void {
        super.commitProperties();
        if (_stackItem != null && _data != null && _data is ChartItem) {
            var columnSeriesItem:ChartItem = _data as ChartItem;
            var obj:Object = columnSeriesItem.item;
            new StandardContextWindow(_stackItem, passThrough, this, obj, _report, true, null, null, null, _seriesField);
        }
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

	//--------------------------------------------------------------------------
    //
    //  Overridden methods
    //
    //--------------------------------------------------------------------------

	/**
	 *  @private
	 */
	override protected function updateDisplayList(unscaledWidth:Number,
												  unscaledHeight:Number):void
	{
		super.updateDisplayList(unscaledWidth, unscaledHeight);

		var fill:IFill;
		var state:String = "";

		if(_data is ChartItem && _data.hasOwnProperty('fill'))
		{
			state = _data.currentState;
			fill = _data.fill;
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
	}
}
}
