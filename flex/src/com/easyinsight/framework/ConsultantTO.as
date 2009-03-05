package com.easyinsight.framework {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.ConsultantTO")]
public class ConsultantTO {
    public var consultantID:int;
    public var userTransferObject:UserTransferObject;
    public var state:int;
    
    public function ConsultantTO() {
    }

    public function get name():String {
        return userTransferObject.name;
    }
}
}