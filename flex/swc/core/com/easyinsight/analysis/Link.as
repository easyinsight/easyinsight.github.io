package com.easyinsight.analysis {
import mx.utils.ObjectUtil;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.Link")]
public class Link {
    public var label:String = "";
    public var linkID:int;
    public var codeGenerated:Boolean;
    public var defaultLink:Boolean = true;
    public var definedByRule:Boolean;

    public function Link() {
    }

    public function get type():String {
        return null;
    }

    public function clone():Link {
        var link:Link = ObjectUtil.copy(this) as Link;
        link.linkID = 0;
        return link;
    }

    public function createString():String {
        return null;
    }
}
}