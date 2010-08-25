package com.easyinsight.analysis {
import com.easyinsight.framework.Credentials;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.CredentialFulfillment")]
public class CredentialFulfillment {
    
    public var dataSourceID:int;
    public var credentials:Credentials;

    public function CredentialFulfillment() {
    }
}
}