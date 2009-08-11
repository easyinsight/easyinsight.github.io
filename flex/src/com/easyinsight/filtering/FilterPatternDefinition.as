package com.easyinsight.filtering {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.FilterPatternDefinition")]
public class FilterPatternDefinition extends FilterDefinition{

    public var pattern:String;
    public var regex:Boolean;
    public var caseSensitive:Boolean;

    public function FilterPatternDefinition() {
        super();
    }

    override public function getType():int {
        return FilterDefinition.PATTERN;
    }

    override public function getDetailEditorClass():Class {
        return PatternFilterWindow;
    }
}
}