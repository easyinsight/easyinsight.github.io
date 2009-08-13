package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountSettings")]
public class AccountSettings {

    public var apiEnabled:Boolean;
    
    public function AccountSettings() {
    }
}
}