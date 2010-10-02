package com.easyinsight.framework {
import mx.core.UIComponent;

public class DirectUIComponent extends PerspectiveFactoryResult {

    public var uiComponent:UIComponent;

    public function DirectUIComponent(uiComponent:UIComponent) {
        super();
        this.uiComponent = uiComponent;
    }
}
}