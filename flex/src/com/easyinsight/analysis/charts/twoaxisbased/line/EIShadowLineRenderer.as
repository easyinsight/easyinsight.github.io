/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/11/11
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts.twoaxisbased.line {
import flash.filters.DropShadowFilter;
import flash.ui.ContextMenu;

import mx.charts.chartClasses.GraphicsUtilities;

import mx.core.IDataRenderer;
import mx.core.UIComponent;
import mx.graphics.IStroke;

public class EIShadowLineRenderer extends UIComponent implements IDataRenderer {

    private static var FILTERS:Array = [ new DropShadowFilter() ];

    public function EIShadowLineRenderer() {
        super();
        filters = FILTERS;
        var menu:ContextMenu = new ContextMenu();
        menu.hideBuiltInItems();
        this.contextMenu = menu;
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
	private var _lineSegment:Object;

	[Inspectable(environment="none")]

	/**
	 *  The chart item that this renderer represents.
	 *  ShadowLineRenderers assume that this value
	 *  is an instance of LineSeriesItem.
	 *  This value is assigned by the owning series.
	 */
	public function get data():Object
	{
		return _lineSegment;
	}

	/**
	 *  @private
	 */
	public function set data(value:Object):void
	{
		_lineSegment = value;

		invalidateDisplayList();
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

		var stroke:IStroke = getStyle("lineStroke");
		var form:String = getStyle("form");

		graphics.clear();

		GraphicsUtilities.drawPolyLine(graphics, _lineSegment.items,
									   _lineSegment.start, _lineSegment.end + 1,
									   "x","y",
									   stroke,form);
	}
}
}
