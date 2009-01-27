package com.easyinsight.account {
import mx.core.Container;
public class AccountOption {
    public var label:String;
    public var container:Container;


    public function AccountOption(label:String, container:Container) {
        this.label = label;
        this.container = container;
    }
}
}