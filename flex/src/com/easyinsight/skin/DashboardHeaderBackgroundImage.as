package com.easyinsight.skin {
import flash.display.Bitmap;
import flash.display.DisplayObject;
import flash.geom.Rectangle;

import mx.binding.utils.BindingUtils;
import mx.binding.utils.ChangeWatcher;
import mx.containers.Canvas;
import mx.controls.Image;
import mx.core.Container;
import mx.graphics.BitmapFill;

public class DashboardHeaderBackgroundImage extends Canvas {

    private var backgroundImage:Image;

    private var _backgroundImageSource:Object;

    private var _backgroundImageStyle:String = "100%";

    private var _skinBackgroundColor:uint;

    private var _centerCanvasBackgroundColor:uint;

    private var _centerCanvasBackgroundAlpha:Number;

    private var centerScreen:Container;

    private var _applyCenterScreenLogic:Boolean = true;

    public function DashboardHeaderBackgroundImage() {
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
            /*if (centerScreen.width == 0) centerScreen.width = 100;*/
            centerScreen.percentHeight = 100;
        }
        addChildAt(backgroundImage, 0);
        createBindings();
    }

    protected function cleanupBindings():void {
        if (watcher1 != null) {
            watcher1.unwatch();
        }
        if (watcher2 != null) {
            watcher2.unwatch();
        }
        if (watcher3 != null) {
            watcher3.unwatch();
        }
        if (watcher4 != null) {
            watcher4.unwatch();
        }
        if (watcher5 != null) {
            watcher5.unwatch();
        }
        if (watcher6 != null) {
            watcher6.unwatch();
        }
    }

    private var watcher1:ChangeWatcher;
    private var watcher2:ChangeWatcher;
    private var watcher3:ChangeWatcher;
    private var watcher4:ChangeWatcher;
    private var watcher5:ChangeWatcher;
    private var watcher6:ChangeWatcher;

    private var _useBindings:Boolean = true;

    public function set useBindings(value:Boolean):void {
        _useBindings = value;
    }

    private function createBindings():void {
        if (_useBindings) {
            if (_applyCenterScreenLogic) {
                watcher1 = BindingUtils.bindProperty(this, "backgroundImageSource", ApplicationSkin.instance(), "coreAppBackgroundImage");
                watcher2 = BindingUtils.bindProperty(this, "skinBackgroundColor", ApplicationSkin.instance(), "coreAppBackgroundColor");
                watcher3 = BindingUtils.bindProperty(this, "backgroundImageStyle", ApplicationSkin.instance(), "coreAppBackgroundSize");
            } else {
                //watcher4 = BindingUtils.bindProperty(this, "backgroundImageSource", ApplicationSkin.instance(), "reportBackground");
                //BindingUtils.bindProperty(this, "backgroundImageStyle", ApplicationSkin.instance(), "reportBackgroundSize");
            }
            if (_applyCenterScreenLogic && centerScreen != null) {
                watcher5 = BindingUtils.bindProperty(this, "centerCanvasBackgroundColor", ApplicationSkin.instance(), "centerCanvasBackgroundColor");
                watcher6 = BindingUtils.bindProperty(this, "centerCanvasBackgroundAlpha", ApplicationSkin.instance(), "centerCanvasBackgroundAlpha");
            }
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
        if (_applyCenterScreenLogic) {
            if (getChildren().length == 2) {
                var margin:int = 200;
                var centerWidth:int = unscaledWidth - margin;
                if (centerWidth < _minCenterWidth) {
                    // if we're now down to 900, reduce margin by 100
                    margin -= (_minCenterWidth - centerWidth);
                    centerWidth = _minCenterWidth;
                }
                var content:DisplayObject = getChildren()[1];
                content.width = centerWidth;
                content.x = unscaledWidth / 2 - (centerWidth / 2);
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