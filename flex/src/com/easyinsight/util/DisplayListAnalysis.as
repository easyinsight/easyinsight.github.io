package com.easyinsight.util {
import flash.display.DisplayObject;
import flash.display.DisplayObjectContainer;
import flash.system.System;
import flash.utils.getQualifiedClassName;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.rpc.events.FaultEvent;
import mx.rpc.soap.LoadEvent;
import mx.rpc.soap.WebService;
import mx.utils.Base64Encoder;

public class DisplayListAnalysis {
    public function DisplayListAnalysis() {
    }

    public function analyze(displayObject:DisplayObject, visible:Boolean):DisplayInfo {
        var info:DisplayInfo = new DisplayInfo();
        info.className = getQualifiedClassName(displayObject).replace("::", ".");
        var count:int = 1;
        var visibleCount:int = (visible && displayObject.visible) ? 1 : 0;
        if (displayObject is DisplayObjectContainer) {
            var container:DisplayObjectContainer = DisplayObjectContainer(displayObject);
            for (var i:int = 0; i < container.numChildren; i++) {
                try {
                    var child:DisplayObject = container.getChildAt(i) as DisplayObject;
                    //if (child.visible) {
                        var childInfo:DisplayInfo = analyze(child, visible && displayObject.visible);
                        count += childInfo.childCount;
                        info.children.addItem(childInfo);
                        if (displayObject.visible) {
                            visibleCount += childInfo.visibleChildCount;
                        }
                    //}
                } catch(e:Error) {
                }
            }
        }
        info.childCount = count;
        info.cacheAsBitmap = displayObject.cacheAsBitmap;
        info.visibleChildCount = visibleCount;
        info.visible = displayObject.visible && visible;
        info.displayObject = displayObject;
        return info;
    }

    public function toXML(info:DisplayInfo):String {
        var xml:String = "<" + info.className + " visible=\"" + info.visible + "\" size=\"" + info.childCount + "\" " +
                         "visibleSize=\"" + info.visibleChildCount + "\" cacheAsBitmap=\"" + info.cacheAsBitmap + "\" x=\""+info.displayObject.x+"\" y=\""+info.displayObject.y+"\">\r\n";
        for each (var child:DisplayInfo in info.children) {
            xml += toXML(child);
        }
        xml += "</" + info.className + ">\r\n";
        return xml;
    }

    public static function traverse(displayObject:DisplayObject):void {
        var analysis:DisplayListAnalysis = new DisplayListAnalysis();
        analysis.createWebService();
        var rootInfo:DisplayInfo = analysis.analyze(displayObject, true);
        System.setClipboard(analysis.toXML(rootInfo));
        /*var rows:ArrayCollection = new ArrayCollection();
        analysis.createRows(rootInfo, rows);
        var dataRows:Array = [];
        for each (var info:DisplayInfo in rows) {
            var rowObj:Object = new Object();
            rowObj["stringPairs"] = [ newPair("ClassName", info.className), newPair("Visible", String(info.visible))];
            rowObj["numberPairs"] = [ newPair("Count", 1), newPair("Size", info.childCount), newPair("Visible Size", info.visibleChildCount) ];
            dataRows.push(rowObj);
        }
        analysis.webService.replaceRows("EI Display List", dataRows, true);*/
    }

    private function createRows(displayInfo:DisplayInfo, rows:ArrayCollection):void {
        rows.addItem(displayInfo);
        for each (var child:DisplayInfo in displayInfo.children) {
            createRows(child, rows);
        }
    }

    private static function newPair(key:String, value:Object):Object {
        return { key: key, value: value};
    }

    private var webService:WebService;

    public function onStart():void {
        var creds:String = "xrnnCqtaFhZN:IzxHGLKiErchVLdo";

        var encoder:Base64Encoder = new Base64Encoder();
        encoder.encode(creds);
        var encodedCreds:String = encoder.toString();

        var header:Object = new Object();
        header["Authorization"] = "Basic " + encodedCreds;
        header["Content-Type"] = "application/x-www-form-urlencoded";
        webService.httpHeaders = header;
        getSecuredWSDL();
    }

    private function createWebService():void {
        if (webService == null) {
            webService = new WebService();
            webService.replaceRows.resultFormat = "object";
            webService.addEventListener(FaultEvent.FAULT, onFault);
            onStart();
        }
    }

    private function onWsdlLoaded(event:LoadEvent):void {
    }

    public function getSecuredWSDL():void {
        webService.addEventListener(LoadEvent.LOAD, onWsdlLoaded);
        webService.loadWSDL("https://www.easy-insight.com/app/services/EIDataV2?wsdl");
    }

    private function onFault(event:FaultEvent):void {
        Alert.show(event.fault.message);
    }
}
}