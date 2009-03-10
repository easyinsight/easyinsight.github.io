package com.easyinsight.groups {
[Bindable]
[RemoteClass(alias="com.easyinsight.groups.GroupAuditMessage")]
public class GroupAuditMessage extends AuditMessage {
    public var groupID:int;
    public var groupName:String;

    public function GroupAuditMessage() {
        
    }
}
}