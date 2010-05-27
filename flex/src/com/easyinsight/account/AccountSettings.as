package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountSettings")]
public class AccountSettings {

    public var apiEnabled:Boolean;
    public var publicData:Boolean;
    public var marketplace:Boolean;
    public var reportSharing:Boolean;
    public var groupID:int;
    public var dateFormat:int;
    
    public function AccountSettings() {
    }
}
}