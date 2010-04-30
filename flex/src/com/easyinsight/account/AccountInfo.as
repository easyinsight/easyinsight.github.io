package com.easyinsight.account {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountInfo")]
public class AccountInfo {

    public var accountState:int;
    public var accountType:int;
    public var createdDate:Date;
    public var trialEndDate:Date;
    public var lastBillingPaidDate:Date;
    public var account:Account;
    public var accountStats:AccountStats;

    public function AccountInfo() {
    }
}
}