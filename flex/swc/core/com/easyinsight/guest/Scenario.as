package com.easyinsight.guest {
[Bindable]
[RemoteClass(alias="com.easyinsight.users.Scenario")]
public class Scenario {

    public var scenarioID:int;
    public var name:String;
    public var summary:String;
    public var description:String;
    public var image:String;
    public var accountID:int;
    public var userID:int;
    public var scenarioKey:String;

    public function Scenario() {
    }
}
}