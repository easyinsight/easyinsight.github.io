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

    override public function createEditorComponent():UIComponent {
        var comp:DashboardImageEditorComponent = new DashboardImageEditorComponent();
        comp.image = this;
        return comp;
    }

    override public function createViewComponent():UIComponent {
        var comp:DashboardImageViewComponent = new DashboardImageViewComponent();
        comp.dashboardImage = this;
        return comp;
    }
}
}