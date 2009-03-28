package com.easyinsight.analysis.list {
import flash.events.EventDispatcher;
public class ListKeywordController extends EventDispatcher{

    public function ListKeywordController() {
        super();
    }

    public function keywordChange(keyword:String, caseSensitive:Boolean, wholeWordsOnly:Boolean):void {
        if (!caseSensitive) {
            keyword = keyword.toLowerCase();
        }
        dispatchEvent(new ListKeywordEvent(keyword, caseSensitive, wholeWordsOnly));
    }
}
}