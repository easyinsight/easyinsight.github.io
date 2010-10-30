package com.easyinsight.analysis {
import com.easyinsight.util.PNGEnc;

import com.easyinsight.util.ProgressAlert;

import flash.display.BitmapData;
import flash.display.DisplayObject;
import flash.net.URLRequest;
import flash.net.navigateToURL;
import flash.utils.ByteArray;

import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class PNGCreator {

    public function PNGCreator() {
        upload = new RemoteObject();
        upload.destination = "exportService";
        upload.exportToPNG.addEventListener(ResultEvent.RESULT, exported);
    }

    private var upload:RemoteObject;

    private function exported(event:ResultEvent):void {
        var url:URLRequest = new URLRequest("/app/image");
        navigateToURL(url, "_blank");
    }

    public function exportPNG(renderable:DisplayObject, parent:UIComponent, reportName:String):void {
        var bd:BitmapData = new BitmapData(renderable.width, renderable.height);
        bd.draw(renderable);
        var ba:ByteArray = PNGEnc.encode(bd);
        ProgressAlert.alert(parent, "Generating the PNG image...", null, upload.exportToPNG);
        upload.exportToPNG.send(reportName, ba);
    }
}
}