package com.easyinsight.customupload {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.OAuthResponse")]
public class OAuthResponse {

    public static const BAD_HOST:int = 1;
    public static const OTHER_OAUTH_PROBLEM:int = 2;

    public var successful:Boolean;
    public var error:int;
    public var requestToken:String;

    public function OAuthResponse() {
    }
}
}