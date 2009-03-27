package com.easyinsight.groups {
[Bindable]
[RemoteClass(alias="com.easyinsight.groups.GroupResponse")]
public class GroupResponse {

    public static const SUCCESS:int = 1;
    public static const NEED_LOGIN:int = 2;
    public static const REJECTED:int = 3;

    public var status:int;
    public var groupID:int;

    public function GroupResponse() {
    }
}
}