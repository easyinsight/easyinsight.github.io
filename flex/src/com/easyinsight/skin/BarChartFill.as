package com.easyinsight.skin {
import flash.display.Graphics;
import flash.geom.Rectangle;
import mx.charts.ChartItem;
import mx.core.IDataRenderer;
import mx.graphics.BitmapFill;
import mx.skins.ProgrammaticSkin;
public class BarChartFill extends ProgrammaticSkin implements IDataRenderer {

    [Bindable]
    [Embed(source="osx/appleDesktop.jpg")]
    private var backgroundDesktop:Class;

    private var _chartItem:ChartItem;

    private var fill:BitmapFill;

    public function BarChartFill() {
        super();
        fill = new BitmapFill();
        fill.source = backgroundDesktop;
    }

    public function get data():Object {
        return _chartItem;
    }

    public function set data(value:Object):void  {
        if (_chartItem == value)
            return;
        _chartItem = ChartItem(value);

    }


    override protected function updateDisplayList(unscaledWidth:Number,
                                                  unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);

        var rc:Rectangle = new Rectangle(0, 0, width , height );
        var g:Graphics = graphics;
        //bgFill.originX = (fill * 20);
        fill.begin(g, rc);
        fill.end(g);
    }
}
}