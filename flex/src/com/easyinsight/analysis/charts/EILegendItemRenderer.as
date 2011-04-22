/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 4/20/11
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts {

import flash.display.Graphics;
import flash.geom.Rectangle;
import mx.charts.ChartItem;
import mx.charts.chartClasses.GraphicsUtilities;
import mx.core.IDataRenderer;
import mx.graphics.IFill;
import mx.graphics.IStroke;
import mx.skins.ProgrammaticSkin;
import mx.graphics.SolidColor;
import mx.utils.ColorUtil;
import mx.styles.StyleManager;

/**
 *  A simple chart itemRenderer implementation
 *  that fills a rectangular area.
 *  This class is the default itemRenderer for ColumnSeries and BarSeries objects.
 *  It can be used as an itemRenderer for ColumnSeries, BarSeries, AreaSeries,
 *  LineSeries, PlotSeries, and BubbleSeries objects.
 *  This class renders its area on screen using the <code>fill</code> and <code>stroke</code> styles
 *  of its associated series.
 */
public class EILegendItemRenderer extends ProgrammaticSkin implements IDataRenderer
{

	//--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------

	/**
	 *  Constructor.
	 */
	public function EILegendItemRenderer()
	{
		super();
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
    private var _fill:IFill;


    public function set fill(value:IFill):void {
        _fill = value;
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

		var fill:IFill = _fill;
		var state:String = "";

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

    public function get data():Object {
        return null;
    }

    public function set data(value:Object):void {
    }
}
}
