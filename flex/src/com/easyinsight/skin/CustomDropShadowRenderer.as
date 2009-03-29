package com.easyinsight.skin {
import com.easyinsight.analysis.charts.ChartDrilldownEvent;
import flash.display.Graphics;
import flash.events.ContextMenuEvent;
import flash.geom.Rectangle;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;
import mx.charts.ChartItem;
import mx.charts.chartClasses.GraphicsUtilities;
import mx.core.IDataRenderer;
import mx.core.UIComponent;
import mx.graphics.BitmapFill;
import mx.graphics.IFill;
import mx.graphics.IStroke;
import mx.skins.ProgrammaticSkin;

public class CustomDropShadowRenderer extends UIComponent implements IDataRenderer {

    private static var rcFill:Rectangle = new Rectangle();


    private var _selectedFill:Class;

    private var _rolloverFill:Class;

    public function set selectedFill(val:Class):void {
        _selectedFill = val;
        bgFill = new BitmapFill();
        bgFill.source = _selectedFill;
    }

    public function set rolloverFill(val:Class):void {
        _rolloverFill = val;
        darkWaterFill = new BitmapFill();
        darkWaterFill.source = _rolloverFill;
    }

    private var bgFill:BitmapFill;
    private var darkWaterFill:BitmapFill;

    public function CustomDropShadowRenderer() {
        super();
        contextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        var drilldownContextItem:ContextMenuItem = new ContextMenuItem("Drilldown", true);
        drilldownContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onDrilldown);
        var rollupContextItem:ContextMenuItem = new ContextMenuItem("Rollup", true);
        rollupContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onRollup);
        contextMenu.customItems = [ drilldownContextItem, rollupContextItem ];
    }

    private function onDrilldown(event:ContextMenuEvent):void {
        dispatchEvent(new ChartDrilldownEvent(ChartDrilldownEvent.DRILLDOWN, _data as ChartItem));
    }

    private function onRollup(event:ContextMenuEvent):void {
        dispatchEvent(new ChartDrilldownEvent(ChartDrilldownEvent.ROLLUP, _data as ChartItem));
    }

    /**
	 *  @private
	 *  Storage for the data property.
	 */
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
		 	fill = _data.fill;
		 	state = _data.currentState;
		}
		else
		 	fill = GraphicsUtilities.fillFromStyle(getStyle('fill'));

		var adjustedRadius:Number = 0;
		
		switch(state)
		{
			case ChartItem.FOCUSED:
			case ChartItem.ROLLOVER:
				fill = bgFill;
				adjustedRadius = getStyle('adjustedRadius');
				if(!adjustedRadius)
					adjustedRadius = 0;
				break;
			/*case ChartItem.DISABLED:
				if(StyleManager.isValidStyleValue(getStyle('itemDisabledColor')))
					color = getStyle('itemDisabledColor');
				else
					color = ColorUtil.adjustBrightness2(GraphicsUtilities.colorFromFill(fill),20);
				fill = new SolidColor(GraphicsUtilities.colorFromFill(color));
				break;*/
			case ChartItem.FOCUSEDSELECTED:
			case ChartItem.SELECTED:
				fill = darkWaterFill;
				adjustedRadius = getStyle('adjustedRadius');
				if(!adjustedRadius)
					adjustedRadius = 0;
				break;
		}

		var stroke:IStroke = getStyle("stroke");

		var w:Number = stroke ? stroke.weight / 2 : 0;

		rcFill.left = rcFill.left - adjustedRadius;
		rcFill.top = rcFill.top - adjustedRadius;
		rcFill.right = unscaledWidth;
		rcFill.bottom = unscaledHeight;

		var g:Graphics = graphics;
		g.clear();
		g.moveTo(w - adjustedRadius, w - adjustedRadius);
		if (stroke)
			stroke.apply(g);
		if (fill)
			fill.begin(g, rcFill);
		g.lineTo(unscaledWidth - w + 2 * adjustedRadius, w - adjustedRadius);
		g.lineTo(unscaledWidth - w + 2 * adjustedRadius, unscaledHeight + 2 * adjustedRadius - w);
		g.lineTo(w - adjustedRadius, unscaledHeight + 2 * adjustedRadius - w);
		g.lineTo(w - adjustedRadius, w - adjustedRadius);
		if (fill)
			fill.end(g);
	}
}
}