package com.easyinsight.solutions {
import flash.display.Bitmap;
import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.containers.Box;
import mx.controls.Image;

public class SolutionImageRenderer extends Box {

    private var solution:Solution;

    private var image:Image;

    private var _imageSource:Bitmap;

    public function SolutionImageRenderer() {
        super();
        image = new Image();
        image.maxWidth = 200;
        image.maxHeight = 100;
        BindingUtils.bindProperty(image, "source", this, "imageSource");
        this.width = 200;
        this.height = 100;
        this.setStyle("horizontalAlign", "center");
        this.setStyle("verticalAlign", "middle");
    }

    [Bindable(event="imageSourceChanged")]
    public function get imageSource():Bitmap {
        return _imageSource;
    }

    public function set imageSource(value:Bitmap):void {
        if (_imageSource == value) return;
        _imageSource = value;
        dispatchEvent(new Event("imageSourceChanged"));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    private function onComplete(event:Event):void {
        var loaderContent:LoaderInfo = event.currentTarget as LoaderInfo;
        imageSource = Bitmap(loaderContent.loader.content);
        loaderContent.loader.removeEventListener(Event.COMPLETE, onComplete);
    }

    override public function set data(val:Object):void {
        solution = val as Solution;
        if (solution.image != null) {
            var loader:Loader = new Loader();
            loader.contentLoaderInfo.addEventListener(Event.COMPLETE, onComplete);
            loader.loadBytes(solution.image);
        } else {
            imageSource = null;
        }
    }

    override public function get data():Object {
        return solution;
    }
}
}