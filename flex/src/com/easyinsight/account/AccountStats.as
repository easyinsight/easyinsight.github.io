package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountStats")]
public class AccountStats {
    public var usedSpace:int;
    public var maxSpace:int;
    
    public function AccountStats() {
    }
}
}