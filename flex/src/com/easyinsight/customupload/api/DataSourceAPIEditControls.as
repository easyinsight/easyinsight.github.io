package com.easyinsight.customupload.api {
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;
import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;
public class DataSourceAPIEditControls extends HBox{

    private var apiDescriptor:DataSourceAPIDescriptor;

    [Bindable]
    [Embed(source="../../../../../assets/app_enterprise_x16.png")]
    private var serviceIcon:Class;

    private var button:Button;

    private var parentObj:DisplayObject;

    public function DataSourceAPIEditControls(parentObj:DisplayObject) {
        super();
        this.parentObj = parentObj;
        button = new Button();
        button.setStyle("icon", serviceIcon);
        button.addEventListener(MouseEvent.CLICK, editDataSource);
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(button);
    }

    private function editDataSource(event:MouseEvent):void {
        var window:DataSourceAPIEdit = new DataSourceAPIEdit();
        window.dataSourceAPIDescriptor = apiDescriptor;
        PopUpManager.addPopUp(window, parentObj, true);
        PopUpUtil.centerPopUp(window);
    }

    override public function set data(val:Object):void {
        this.apiDescriptor = val as DataSourceAPIDescriptor;
    }

    override public function get data():Object {
        return this.apiDescriptor;
    }
}
}