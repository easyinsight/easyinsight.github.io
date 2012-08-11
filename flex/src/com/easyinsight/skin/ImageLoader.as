package com.easyinsight.skin {
import flash.display.Bitmap;
import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.events.Event;

import flash.events.EventDispatcher;
import flash.utils.ByteArray;

import mx.rpc.events.FaultEvent;

import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class ImageLoader extends EventDispatcher {

    private var prefService:RemoteObject;

    public function ImageLoader() {
    }

    public function load(imageID:int, endpoint:String = null):void {
        prefService = new RemoteObject();
        if (endpoint != null) {
            prefService.endpoint = endpoint;
        }
        prefService.destination = "preferencesService";
        prefService.getImage.addEventListener(ResultEvent.RESULT, onBytes);
        prefService.getImage.addEventListener(FaultEvent.FAULT, onFault);
        prefService.getImage.send(imageID);
    }

    private function onFault(event:FaultEvent):void {
        trace("Fault = " + event.fault.faultString);
    }

    private function onBytes(event:ResultEvent):void {
        var byteArray:ByteArray = prefService.getImage.lastResult as ByteArray;
        if (byteArray != null) {
            var loader:Loader = new Loader();
            loader.contentLoaderInfo.addEventListener(Event.COMPLETE, onComplete);
            loader.loadBytes(byteArray);
        }
    }

    private function onComplete(event:Event):void {
        var loaderContent:LoaderInfo = event.currentTarget as LoaderInfo;
        var bitmap:Bitmap = Bitmap(loaderContent.loader.content);
        loaderContent.loader.removeEventListener(Event.COMPLETE, onComplete);
        dispatchEvent(new ImageLoadEvent(bitmap));
    }
}
}