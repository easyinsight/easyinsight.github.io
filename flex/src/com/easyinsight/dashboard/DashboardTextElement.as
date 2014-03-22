package com.easyinsight.dashboard {

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardText")]
public class DashboardTextElement extends DashboardElement {

    public var text:String;
    public var html:String;
    public var fontSize:int = 12;
    public var color:uint;
    public var markdown:Boolean;

    public function DashboardTextElement() {
        super();
    }
}
}