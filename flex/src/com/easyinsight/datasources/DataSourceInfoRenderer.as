package com.easyinsight.datasources {
import mx.containers.HBox;
import mx.controls.Label;

public class DataSourceInfoRenderer extends HBox{

    private var dataSourceInfo:DataSourceInfo;

    private var dataSourceLabel:Label;

    public function DataSourceInfoRenderer() {
        super();
        dataSourceLabel = new Label();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(dataSourceLabel);
    }

    override public function set data(val:Object):void {
        this.dataSourceInfo = val as DataSourceInfo;
        dataSourceLabel.text = dataSourceInfo.dataSourceName;
    }

    override public function get data():Object {
        return this.dataSourceInfo;
    }
}
}