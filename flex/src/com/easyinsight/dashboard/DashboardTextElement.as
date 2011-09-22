package com.easyinsight.dashboard {
import com.easyinsight.skin.ImageDescriptor;

import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardText")]
public class DashboardTextElement extends DashboardElement {

    public var text:String;

    public function DashboardTextElement() {
        super();
    }
}
}