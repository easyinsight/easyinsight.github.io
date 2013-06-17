/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/17/11
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
[Bindable]
[RemoteClass(alias="com.easyinsight.intention.IntentionSuggestion")]
public class IntentionSuggestion {

    public static const PROBLEM:int = 1;
    public static const WARNING:int = 2;
    public static const OTHER:int = 3;

    public static const FILTERED_FIELD:int = 18;
    public static const DISTINCT_COUNT:int = 19;

    public var headline:String;
    public var description:String;
    public var type:int;
    public var scope:int;
    public var priority:int;
    public var requiresServerCallback:Boolean;

    public function IntentionSuggestion() {
    }
}
}
