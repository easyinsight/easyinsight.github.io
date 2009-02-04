package com.easyinsight.customupload.api {
import mx.containers.HBox;
import mx.controls.Image;
public class UncheckedAPICheckbox extends HBox {

    [Bindable]
    [Embed(source="../../../../../assets/check_x16.png")]
    private var yes:Class;

    [Bindable]
    [Embed(source="../../../../../assets/sign_forbidden.png")]
    private var no:Class;

    private var image:Image;

    private var apiDescriptor:DataSourceAPIDescriptor;

    public function UncheckedAPICheckbox() {
        super();
        image = new Image();
        this.percentWidth = 100;
        this.setStyle("horizontalAlign", "center");
    }


    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }

    override public function set data(val:Object):void {
        this.apiDescriptor = val as DataSourceAPIDescriptor;
        if (apiDescriptor.uncheckedEnabled) {
            image.source = yes;
        } else {
            image.source = no;
        }
    }

    override public function get data():Object {
        return this.apiDescriptor;
    }
}
}