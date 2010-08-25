package com.easyinsight.preferences {
[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.UIVisibilitySetting")]
public class UIVisibilitySetting {

    public var key:String;
    public var selected:Boolean;

    public function UIVisibilitySetting() {
    }
}
}
