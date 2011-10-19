/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/11/11
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.display.Graphics;
import flash.events.Event;
import flash.events.MouseEvent;
import flash.geom.Rectangle;

import mx.charts.ChartItem;

import mx.charts.chartClasses.GraphicsUtilities;
import mx.controls.Alert;

import mx.core.IDataRenderer;
import mx.core.UIComponent;
import mx.graphics.IFill;
import mx.graphics.IStroke;
import mx.graphics.SolidColor;
import mx.styles.StyleManager;
import mx.utils.ColorUtil;

public class EICircleItemRenderer extends UIComponent implements IDataRenderer {
    private static var rcFill:Rectangle = new Rectangle();

    //--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------

	/**
	 *  Constructor.
	 */
	public function EICircleItemRenderer() {
		super();
	}

    private var _analysisItem:AnalysisItem;

    public function set analysisItem(value:AnalysisItem):void {
        _analysisItem = value;
        invalidateProperties();
    }

//--------------------------------------------------------------------------
    //
    //  Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
	//  data
    //----------------------------------

	/**
	 *  @private
	 *  Storage for the data property.
	 */
	private var _chartItem:Object;

	[Inspectable(environment="none")]

	/**
	 *  The chartItem that this itemRenderer displays.
	 *  This value is assigned by the owning series.
	 */
	public function get data():Object
	{
		return _chartItem;
	}

	/**
	 *  @private
	 */
	public function set data(value:Object):void
	{
		if (_chartItem == value)
			return;

		_chartItem = value;
        invalidateProperties();
	}

    override protected function commitProperties():void {
        super.commitProperties();
        if (_analysisItem != null && _chartItem != null && _chartItem is ChartItem) {
            var columnSeriesItem:ChartItem = _chartItem as ChartItem;
            var obj:Object = columnSeriesItem.item;
            new StandardContextWindow(_analysisItem, passThrough, this, obj);
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

		if(_chartItem is ChartItem && _chartItem.hasOwnProperty('fill'))
		{
		 	fill = _chartItem.fill;
		 	state = _chartItem.currentState;
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

		rcFill.right = unscaledWidth;
		rcFill.bottom = unscaledHeight;

		var g:Graphics = graphics;
		g.clear();
		if (stroke)
			stroke.apply(g);
		if (fill)
			fill.begin(g, rcFill);
		g.drawEllipse(w - adjustedRadius,w - adjustedRadius,unscaledWidth - 2 * w + adjustedRadius * 2, unscaledHeight - 2 * w + adjustedRadius * 2);

		if (fill)
			fill.end(g);
	}
}
}
