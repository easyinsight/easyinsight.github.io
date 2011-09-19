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

    public var headline:String;
    public var description:String;
    public var type:int;
    public var scope:int;

    public function IntentionSuggestion() {
    }
}
}
