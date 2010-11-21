package com.easyinsight.skin {
import flash.display.Bitmap;
import flash.events.Event;

public class ImageLoadEvent extends Event {

    public static const IMAGE_LOADED:String = "imageLoaded";

    public var bitmap:Bitmap;

    public function ImageLoadEvent(bitmap:Bitmap) {
        super(IMAGE_LOADED);
        this.bitmap = bitmap;
    }

    override public function clone():Event {
        return new ImageLoadEvent(bitmap);
    }
}
}