package com.easyinsight.dashboard {
import com.easyinsight.skin.ImageDescriptor;

import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardImage")]
public class DashboardImage extends DashboardElement {

    public var imageDescriptor:ImageDescriptor;

    public function DashboardImage() {
        super();
    }
}
}