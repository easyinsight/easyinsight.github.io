package com.easyinsight.framework {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.WelcomeBackInfo")]
public class WelcomeBackInfo {

    public var firstName:String;
    public var lastName:String;
    public var accountName:String;
    public var optIn:Boolean;
    public var accountTier:int;

    public function WelcomeBackInfo() {
    }
}
}