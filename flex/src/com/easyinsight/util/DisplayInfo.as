package com.easyinsight.util {
import flash.display.DisplayObject;

import mx.collections.ArrayCollection;

public class DisplayInfo {

    public var childCount:int;
    public var visible:Boolean;
    public var visibleChildCount:int;
    public var children:ArrayCollection = new ArrayCollection();
    public var className:String;
    public var cacheAsBitmap:Boolean;
    public var displayObject:DisplayObject;

    public function DisplayInfo() {
    }
}
}