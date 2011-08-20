package com.easyinsight.dashboard {

import flash.display.Bitmap;
import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.events.Event;

import flash.utils.ByteArray;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.controls.Image;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DashboardImageViewComponent extends Box implements IDashboardViewComponent  {

    public var dashboardImage:DashboardImage;

    public var prefs:RemoteObject;

    private var image:Image;

    public function DashboardImageViewComponent() {
        super();
        percentWidth = 100;
        percentHeight = 100;
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
    }

    protected override function createChildren():void {
        super.createChildren();
        image = new Image();
        addChild(image);
        prefs = new RemoteObject();
        prefs.destination = "preferencesService";
        prefs.getImage.addEventListener(ResultEvent.RESULT, gotImageBytes);
        prefs.getImage.send(dashboardImage.imageDescriptor.id);
    }

    private function gotImageBytes(event:ResultEvent):void {
        var bytes:ByteArray = prefs.getImage.lastResult as ByteArray;
        var loader:Loader = new Loader();
        loader.contentLoaderInfo.addEventListener(Event.COMPLETE, onComplete);
        loader.loadBytes(bytes);
    }

    private function onComplete(event:Event):void {
        var loaderContent:LoaderInfo = event.currentTarget as LoaderInfo;
        image.source = Bitmap(loaderContent.loader.content);
        loaderContent.loader.removeEventListener(Event.COMPLETE, onComplete);
    }

    public function refresh():void {
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
    }

    public function updateAdditionalFilters(filters:Object):void {
    }

    public function activeStatus(status:Boolean):void {
    }

    public function initialRetrieve():void {
    }
}
}