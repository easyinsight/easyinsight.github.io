package com.easyinsight.groups {
[Bindable]
[RemoteClass(alias="com.easyinsight.groups.GroupComment")]
public class GroupComment extends AuditMessage {
    public var groupID:int;
    public var groupName:String;

    public function GroupComment() {
        
    }
}
}