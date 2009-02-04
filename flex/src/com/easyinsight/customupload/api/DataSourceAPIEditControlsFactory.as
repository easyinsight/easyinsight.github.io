package com.easyinsight.customupload.api {
import flash.display.DisplayObject;
import mx.core.IFactory;
public class DataSourceAPIEditControlsFactory implements IFactory {

    private var parent:DisplayObject;

    public function DataSourceAPIEditControlsFactory(parent:DisplayObject) {
        this.parent = parent;
    }

    public function newInstance():* {
        return new DataSourceAPIEditControls(parent);
    }
}
}