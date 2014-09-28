/**
 * Created by jamesboe on 9/27/14.
 */
package com.easyinsight.listing {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.DataSourceSuggestion")]
public class DataSourceSuggestion {

    public var suggestionType:int;
    public var suggestionLabel:String;

    public function DataSourceSuggestion() {
    }
}
}
