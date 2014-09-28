/**
 * Created by jamesboe on 9/28/14.
 */
package com.easyinsight.listing {

[Bindable]
[RemoteClass(alias="com.easyinsight.userupload.SuggestionResult")]
public class SuggestionResult {

    public var responseType:int;
    public var emailName:String;
    public var dashboardID:int;

    public function SuggestionResult() {
    }
}
}
