package com.easyinsight.genredata {
import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;

import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Label;

public class ReportThumbnailRenderer extends VBox{

    private var reportThumbnail:ReportThumbnail;
    private var _titleText:String;
    private var titleLabel:Label;
    private var imageRenderer:ReportImageRenderer;
    private var box:VBox;

    public function ReportThumbnailRenderer() {
        super();
        titleLabel = new Label();
        BindingUtils.bindProperty(titleLabel, "text", this, "titleText");
        imageRenderer = new ReportImageRenderer();
        setStyle("paddingLeft", 10);
        setStyle("paddingRight", 10);
        setStyle("paddingTop", 10);
        setStyle("paddingBottom", 10);
        setStyle("backgroundAlpha", 0);
        this.percentWidth = 100;
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {

    }

    [Bindable(event="titleTextChanged")]
    public function get titleText():String {
        return _titleText;
    }

    public function set titleText(value:String):void {
        if (_titleText == value) return;
        _titleText = value;
        dispatchEvent(new Event("titleTextChanged"));
    }

    override protected function createChildren():void {
        super.createChildren();
        box = new VBox();
        box.setStyle("borderStyle", "solid");
        box.setStyle("cornerRadius", 15);
        box.setStyle("dropShadowEnabled", "true");
        box.setStyle("backgroundAlpha", 1);
        box.setStyle("horizontalAlign", "center");
        box.setStyle("percentWidth", 100);
        addChild(box);
        var titleBox:HBox = new HBox();
        titleBox.percentWidth = 100;
        titleBox.setStyle("horizontalAlign", "center");
        titleBox.addChild(titleLabel);
        box.addChild(titleBox);
        box.addChild(imageRenderer);
    }

    override public function set data(val:Object):void {
        this.reportThumbnail = val as ReportThumbnail;
        imageRenderer.data = val;
        titleText = reportThumbnail.insightDescriptor.name;
    }

    override public function get data():Object {
        return this.reportThumbnail;
    }
}
}