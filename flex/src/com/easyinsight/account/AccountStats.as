package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountStats")]
public class AccountStats {
    public var usedSpace:Number;
    public var maxSpace:Number;
    public var availableUsers:Number;
    public var currentUsers:Number;
    public var apiUsedToday:Number;
    public var apiMaxToday:Number;
    
    public function AccountStats() {
    }
}
}