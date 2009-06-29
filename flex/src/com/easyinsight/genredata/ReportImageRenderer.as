package com.easyinsight.genredata {

import flash.display.Bitmap;
import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.containers.Box;
import mx.controls.Image;

public class ReportImageRenderer extends Box {

    private var reportThumbnail:ReportThumbnail;

    private var image:Image;

    private var _imageSource:Bitmap;

    public function ReportImageRenderer() {
        super();
        image = new Image();
        image.maxWidth = 400;
        image.maxHeight = 300;
        BindingUtils.bindProperty(image, "source", this, "imageSource");
        this.width = 400;
        this.height = 300;
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
        reportThumbnail = val as ReportThumbnail;
        if (reportThumbnail != null) {
            if (reportThumbnail.image != null) {
                var loader:Loader = new Loader();
                loader.contentLoaderInfo.addEventListener(Event.COMPLETE, onComplete);
                loader.loadBytes(reportThumbnail.image);
            } else {
                imageSource = null;
            }
        }
    }

    override public function get data():Object {
        return reportThumbnail;
    }
}
}