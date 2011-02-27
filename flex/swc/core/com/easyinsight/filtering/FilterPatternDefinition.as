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

    override public function matches(filterDefinition:FilterDefinition):Boolean {
        var matches:Boolean = super.matches(filterDefinition);
        if (matches) {
            var pattern:FilterPatternDefinition = filterDefinition as FilterPatternDefinition;
            matches = regex == pattern.regex && caseSensitive == pattern.caseSensitive && _pattern == pattern._pattern;
            return matches;
        }
        return false;
    }

    override public function getType():int {
        return FilterDefinition.PATTERN;
    }
}
}