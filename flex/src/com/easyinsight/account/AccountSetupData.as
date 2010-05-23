package com.easyinsight.account {

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.users.AccountSetupData")]
public class AccountSetupData {

    public var myPersona:int;
    public var personas:ArrayCollection;
    public var users:ArrayCollection;

    public function AccountSetupData() {
    }
}
}