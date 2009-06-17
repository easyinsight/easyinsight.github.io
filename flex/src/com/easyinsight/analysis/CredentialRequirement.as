package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.CredentialRequirement")]
public class CredentialRequirement {
    public var dataSourceID:int;
    public var dataSourceName:String;
    public var credentialsDefinition:CredentialsDefinition;

    public function CredentialRequirement() {
    }
}
}