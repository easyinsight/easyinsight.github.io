package com.easyinsight.framework {
public class ModuleUIComponent extends PerspectiveFactoryResult {

    public var label:String;
    public var module:String;

    public function ModuleUIComponent(module:String, label:String) {
        super();
        this.label = label;
        this.module = module;
    }
}
}