package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.Link")]
public class Link {
    public var label:String;
    public var linkID:int;
    public var codeGenerated:Boolean;

    public function Link() {
    }

    public function get type():String {
        return null;
    }
}
}