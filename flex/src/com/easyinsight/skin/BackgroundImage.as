package com.easyinsight.skin {
import flash.display.DisplayObject;
import flash.geom.Rectangle;
import mx.containers.Canvas;

import mx.core.Container;
import mx.graphics.BitmapFill;

public class BackgroundImage extends Canvas {

    [Bindable]
    [Embed(source="../../../../assets/bgnoise.png")]
    private var bgNoise:Class;

    private var centerScreen:Container;

    private var _applyCenterScreenLogic:Boolean = true;

    public function BackgroundImage() {
        super();
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    public function set applyCenterScreenLogic(value:Boolean):void {
        _applyCenterScreenLogic = value;
    }

    private var fill:BitmapFill;

    override protected function createChildren():void {
        super.createChildren();
        if (fill == null) {
            fill = new BitmapFill();
            fill.source = bgNoise;
            fill.repeat = true;
        }
        if (_applyCenterScreenLogic) {
            centerScreen = getChildAt(0) as Container;
            centerScreen.percentHeight = 100;
        }
    }

    private var _minCenterWidth:int = 1000;

    public function get minCenterWidth():int {
        return _minCenterWidth;
    }

    public function set minCenterWidth(value:int):void {
        _minCenterWidth = value;
    }

    protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var rc:Rectangle = new Rectangle(0, 0, unscaledWidth, unscaledHeight);
        fill.begin(graphics, rc);
        graphics.lineTo(rc.right,rc.top);
        graphics.lineTo(rc.right,rc.bottom);
        graphics.lineTo(rc.left,rc.bottom);
        graphics.lineTo(rc.left,rc.top);
        fill.end(graphics);
        if (_applyCenterScreenLogic) {
            if (getChildren().length == 1) {
                var margin:int = 200;
                var centerWidth:int = unscaledWidth - margin;
                if (centerWidth < _minCenterWidth) {
                    // if we're now down to 900, reduce margin by 100
                    margin -= (_minCenterWidth - centerWidth);
                    centerWidth = _minCenterWidth;
                }
                var content:DisplayObject = getChildren()[0];
                content.width = centerWidth;
                content.x = unscaledWidth / 2 - (centerWidth / 2);
            }
        }
    }
}
}