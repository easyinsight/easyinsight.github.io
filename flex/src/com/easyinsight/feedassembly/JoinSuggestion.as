/**
 * User: jamesboe
 * Date: 2/9/11
 * Time: 11:52 AM
 */
package com.easyinsight.feedassembly {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.JoinSuggestion")]
public class JoinSuggestion {

    public var sourceType:String;
    public var targetType:String;
    public var possibleSources:ArrayCollection;
    public var possibleTargets:ArrayCollection;

    public function JoinSuggestion() {
    }

    public function toComponent():JoinSuggestionLine {
        var line:JoinSuggestionLine = new JoinSuggestionLine();
        line.joinSuggestion = this;
        return line;
    }
}
}
