package com.easyinsight.dashboard {

import flash.display.Bitmap;
import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.events.Event;
import flash.utils.ByteArray;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.controls.Image;
import mx.controls.TextArea;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DashboardTextViewComponent extends Box implements IDashboardViewComponent  {

    public var dashboardText:DashboardTextElement;

    private var textArea:TextArea;

    public function DashboardTextViewComponent() {
        super();
        percentWidth = 100;
        percentHeight = 100;
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
    }

    protected override function createChildren():void {
        super.createChildren();
        textArea = new TextArea();
        textArea.editable = false;
        textArea.text = dashboardText.text;
        addChild(textArea);
    }

    public function refresh():void {
    }

    public function updateAdditionalFilters(filters:Object):void {
    }

    public function initialRetrieve():void {
    }

    public function reportCount():ArrayCollection {
        return new ArrayCollection();
    }
}
}