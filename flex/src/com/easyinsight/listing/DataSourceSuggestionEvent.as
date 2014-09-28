/**
 * Created by jamesboe on 9/28/14.
 */
package com.easyinsight.listing {
import flash.events.Event;

public class DataSourceSuggestionEvent extends Event {

    public var suggestionType:int;

    public static const DATA_SOURCE_SUGGESTION:String = "dataSourceSuggestion";

    public function DataSourceSuggestionEvent(suggestionType:int) {
        super(DATA_SOURCE_SUGGESTION, true);
        this.suggestionType = suggestionType;
    }

    override public function clone():Event {
        return new DataSourceSuggestionEvent(suggestionType);
    }
}
}
