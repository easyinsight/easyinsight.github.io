package com.easyinsight.skin {
import flash.display.Bitmap;
import flash.display.DisplayObject;

import mx.binding.utils.BindingUtils;
import mx.containers.Canvas;
import mx.controls.Image;
import mx.core.Container;

public class BackgroundImage extends Canvas {

    private var backgroundImage:Image;

    private var _backgroundImageSource:Object;

    private var _backgroundImageStyle:String = "100%";

    private var _skinBackgroundColor:uint;

    private var _centerCanvasBackgroundColor:uint;

    private var _centerCanvasBackgroundAlpha:Number;

    private var centerScreen:Container;

    public function BackgroundImage() {
        super();
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        if (backgroundImage == null) {
            backgroundImage = new Image();
            backgroundImage.percentHeight = 100;
            backgroundImage.percentWidth = 100;
            backgroundImage.maintainAspectRatio = false;
        }
        centerScreen = getChildAt(0) as Container;
        if (centerScreen.width == 0) centerScreen.width = 1000;
        centerScreen.percentHeight = 100;
        addChildAt(backgroundImage, 0);
        createBindings();
    }

    private function createBindings():void {
        BindingUtils.bindProperty(this, "backgroundImageSource", ApplicationSkin.instance(), "coreAppBackgroundImage");
        BindingUtils.bindProperty(this, "skinBackgroundColor", ApplicationSkin.instance(), "coreAppBackgroundColor");
        BindingUtils.bindProperty(this, "backgroundImageStyle", ApplicationSkin.instance(), "coreAppBackgroundSize");
        if (centerScreen != null) {
            BindingUtils.bindProperty(this, "centerCanvasBackgroundColor", ApplicationSkin.instance(), "centerCanvasBackgroundColor");
            BindingUtils.bindProperty(this, "centerCanvasBackgroundAlpha", ApplicationSkin.instance(), "centerCanvasBackgroundAlpha");
        }
    }

    protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (getChildren().length == 2) {
            var content:DisplayObject = getChildren()[1];
            content.x = unscaledWidth / 2 - (content.width / 2);
        }
    }

    public function set backgroundImageStyle(value:String):void {
        _backgroundImageStyle = value;
    }

    public function set centerCanvasBackgroundColor(value:uint):void {
        _centerCanvasBackgroundColor = value;
        centerScreen.setStyle("backgroundColor", value);
    }

    public function set centerCanvasBackgroundAlpha(value:Number):void {
        _centerCanvasBackgroundAlpha = value;
        centerScreen.setStyle("backgroundAlpha", value);
    }

    public function set backgroundImageSource(value:Object):void {
        if (_backgroundImageSource == value) return;
        _backgroundImageSource = value;
        if (value == null) {
            backgroundImage.source = null;
        } else {
            var bitmap:Bitmap = value as Bitmap;
            //var sourceData:BitmapData = bitmap.bitmapData;
            /*var targetData:BitmapData = new BitmapData(sourceData.width, sourceData.height);
            targetData.copyPixels(sourceData, new Rectangle(0, 0, sourceData.width, sourceData.height), new Point(0, 0));*/
            backgroundImage.source = new Bitmap(bitmap.bitmapData);
        }
    }

    public function set skinBackgroundColor(value:uint):void {
        if (_skinBackgroundColor == value) return;
        _skinBackgroundColor = value;
        setStyle("backgroundColor", value);
    }
}
}