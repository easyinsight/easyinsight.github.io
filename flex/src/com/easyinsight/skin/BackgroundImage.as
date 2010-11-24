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

    private var _applyCenterScreenLogic:Boolean = true;

    public function BackgroundImage() {
        super();
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    public function set applyCenterScreenLogic(value:Boolean):void {
        _applyCenterScreenLogic = value;
    }

    override protected function createChildren():void {
        super.createChildren();
        if (backgroundImage == null) {
            backgroundImage = new Image();
            backgroundImage.percentHeight = 100;
            backgroundImage.percentWidth = 100;
            backgroundImage.maintainAspectRatio = false;
        }
        if (_applyCenterScreenLogic) {
            centerScreen = getChildAt(0) as Container;
            if (centerScreen.width == 0) centerScreen.width = 1000;
            centerScreen.percentHeight = 100;
        }
        addChildAt(backgroundImage, 0);
        createBindings();
    }

    private function createBindings():void {
        if (_applyCenterScreenLogic) {
            BindingUtils.bindProperty(this, "backgroundImageSource", ApplicationSkin.instance(), "coreAppBackgroundImage");
            BindingUtils.bindProperty(this, "skinBackgroundColor", ApplicationSkin.instance(), "coreAppBackgroundColor");
            BindingUtils.bindProperty(this, "backgroundImageStyle", ApplicationSkin.instance(), "coreAppBackgroundSize");
        } else {
            BindingUtils.bindProperty(this, "backgroundImageSource", ApplicationSkin.instance(), "reportBackground");
            //BindingUtils.bindProperty(this, "backgroundImageStyle", ApplicationSkin.instance(), "reportBackgroundSize");
        }
        if (_applyCenterScreenLogic && centerScreen != null) {
            BindingUtils.bindProperty(this, "centerCanvasBackgroundColor", ApplicationSkin.instance(), "centerCanvasBackgroundColor");
            BindingUtils.bindProperty(this, "centerCanvasBackgroundAlpha", ApplicationSkin.instance(), "centerCanvasBackgroundAlpha");
        }
    }

    protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (_applyCenterScreenLogic) {
            if (getChildren().length == 2) {
                var content:DisplayObject = getChildren()[1];
                content.x = unscaledWidth / 2 - (content.width / 2);
            }
        }
    }

    public function set backgroundImageStyle(value:String):void {
        _backgroundImageStyle = value;
    }

    public function set centerCanvasBackgroundColor(value:uint):void {
        _centerCanvasBackgroundColor = value;
        if (centerScreen != null) {
            centerScreen.setStyle("backgroundColor", value);
        }
    }

    public function set centerCanvasBackgroundAlpha(value:Number):void {
        _centerCanvasBackgroundAlpha = value;
        if (centerScreen != null) {
            centerScreen.setStyle("backgroundAlpha", value);
        }
    }

    public function set backgroundImageSource(value:Object):void {
        if (_backgroundImageSource == value) return;
        _backgroundImageSource = value;
        if (value == null) {
            backgroundImage.source = null;
            backgroundImage.visible = false;
        } else {
            if (value is Bitmap) {
                var bitmap:Bitmap = value as Bitmap;
                backgroundImage.source = new Bitmap(bitmap.bitmapData);
            } else {
                backgroundImage.source = value;
            }
            backgroundImage.visible = true;
        }
    }

    public function set skinBackgroundColor(value:uint):void {
        if (_skinBackgroundColor == value) return;
        _skinBackgroundColor = value;
        setStyle("backgroundColor", value);
    }
}
}