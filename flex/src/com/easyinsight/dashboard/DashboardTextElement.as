package com.easyinsight.dashboard {

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardText")]
public class DashboardTextElement extends DashboardElement {

    public var text:String;
    public var fontSize:int;
    public var color:uint;

    public function DashboardTextElement() {
        super();
    }
}
}