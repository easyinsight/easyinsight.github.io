package com.easyinsight.framework {
[Bindable]
[RemoteClass(alias="com.easyinsight.security.AuthorizationRequirement")]
public class AuthorizationRequirement {

    public var authorizationType:int;
    public var url:String;

    public function AuthorizationRequirement() {
    }
}
}