package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountAPISettings")]
public class AccountAPISettings {

    public var apiEnabled:Boolean;
    public var userKey:String;
    public var userSecretKey:String;

    public function AccountAPISettings() {
    }
}
}