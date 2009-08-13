package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountAPISettings")]
public class AccountAPISettings {

    public var accountKey:String;
    public var accountSecretKey:String;
    public var apiEnabled:Boolean;

    public function AccountAPISettings() {
    }
}
}