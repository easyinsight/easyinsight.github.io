package com.easyinsight.groups {
[Bindable]
[RemoteClass(alias="com.easyinsight.audit.AuditMessage")]
public class AuditMessage {
    public var userID:int;
    public var userName:String;
    public var message:String;
    public var timestamp:Date;
    public var audit:Boolean;

    public function AuditMessage() {
        
    }
}
}