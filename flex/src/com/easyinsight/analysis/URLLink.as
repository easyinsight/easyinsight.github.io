package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.URLLink")]
public class URLLink extends Link{
    public var url:String;
    public function URLLink() {
        super();
    }

    override public function get type():String {
        return "URL";
    }
}
}