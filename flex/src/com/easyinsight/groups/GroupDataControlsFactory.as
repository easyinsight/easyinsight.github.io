package com.easyinsight.groups {
import mx.core.IFactory;

public class GroupDataControlsFactory implements IFactory {

    private var _groupAdmin:Boolean;

    public function set groupAdmin(value:Boolean):void {
        _groupAdmin = value;
    }

    public function GroupDataControlsFactory() {
    }

    public function newInstance():* {
        if (_groupAdmin) return new GroupAdminMyDataIconControls();
        else return new GroupMyDataIconControls();
    }
}
}