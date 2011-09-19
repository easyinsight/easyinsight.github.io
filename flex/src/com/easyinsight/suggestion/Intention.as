/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/16/11
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {

[Bindable]
[RemoteClass(alias="com.easyinsight.intention.Intention")]
public class Intention {

    public var description:String;
    public var label:String;

    public function Intention() {
    }

    public function apply(suggestionMetadata:SuggestionMetadata):void {

    }
}
}
