package com.easyinsight.preferences {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.UISettings")]
public class UISettings {

    public var visibilitySettings:ArrayCollection;
    public var useCustomScorecard:Boolean;
    public var reportSharing:Boolean;
    public var publicSharing:Boolean;
    public var marketplace:Boolean;

    public function UISettings() {
    }
}
}
