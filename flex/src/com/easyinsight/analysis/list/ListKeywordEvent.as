package com.easyinsight.analysis.list {
import flash.events.Event;
public class ListKeywordEvent extends Event{

    public static const LIST_KEYWORD:String = "listKeyword";

    public var keyword:String;
    public var caseSensitive:Boolean;
    public var wholeWords:Boolean;

    public function ListKeywordEvent(keyword:String, caseSensitive:Boolean, wholeWords:Boolean) {
        super(LIST_KEYWORD);
        this.keyword = keyword;
        this.caseSensitive = caseSensitive;
        this.wholeWords = wholeWords;
    }

    override public function clone():Event {
        return new ListKeywordEvent(keyword, caseSensitive, wholeWords);
    }
}
}