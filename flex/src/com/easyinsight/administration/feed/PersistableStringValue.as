package com.easyinsight.administration.feed {
[Bindable]
[RemoteClass(alias="com.easyinsight.core.PersistableStringValue")]
public class PersistableStringValue extends PersistableValue{

    public var value:String;

    public function PersistableStringValue() {
        super();
    }
}
}