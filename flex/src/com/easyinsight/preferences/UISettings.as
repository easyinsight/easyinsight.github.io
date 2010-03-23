package com.easyinsight.preferences {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.UISettings")]
public class UISettings {

    public var visibilitySettings:ArrayCollection;

    public function UISettings() {
    }
}
}
