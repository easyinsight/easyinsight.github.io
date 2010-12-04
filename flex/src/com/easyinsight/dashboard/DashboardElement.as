package com.easyinsight.dashboard {
import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardElement")]
public class DashboardElement {

    public function DashboardElement() {
    }

    public function createEditorComponent():UIComponent {
        return null;
    }

    public function createViewComponent():UIComponent {
        return null;
    }
}
}