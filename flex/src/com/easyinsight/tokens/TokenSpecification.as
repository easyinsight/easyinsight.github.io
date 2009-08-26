package com.easyinsight.tokens {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.TokenSpecification")]
public class TokenSpecification {

    public var name:String;
    public var type:int;
    public var defined:Boolean;
    public var urlToConfigure:String;

    public function TokenSpecification() {
    }
}
}