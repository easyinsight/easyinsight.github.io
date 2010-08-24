package com.easyinsight.filtering {
import flash.events.Event;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.FilterPatternDefinition")]
public class FilterPatternDefinition extends FilterDefinition{

    private var _pattern:String;
    public var regex:Boolean;
    public var caseSensitive:Boolean;

    public function FilterPatternDefinition() {
        super();
    }

    [Bindable(event="patternChanged")]
    public function get pattern():String {
        return _pattern;
    }

    public function set pattern(value:String):void {
        if (_pattern == value) return;
        _pattern = value;
        dispatchEvent(new Event("patternChanged"));
    }

    override public function getType():int {
        return FilterDefinition.PATTERN;
    }
}
}