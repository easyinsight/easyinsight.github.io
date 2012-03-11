/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/8/12
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.NamedKey;

import flash.events.Event;

import flash.events.EventDispatcher;

public class AnalysisItemDispatcher extends EventDispatcher {
    
    public var analysisItem:AnalysisItem;
    private var _indexed:Boolean;
    
    public function AnalysisItemDispatcher(analysisItem:AnalysisItem) {
        this.analysisItem = analysisItem;
        var namedKey:NamedKey = analysisItem.key.toBaseKey() as NamedKey;
        this.indexed = namedKey.indexed;
    }
    
    public function get display():String {
        return analysisItem.display;
    }

    [Bindable(event="indexedChanged")]
    public function get indexed():Boolean {
        return _indexed;
    }

    public function set indexed(value:Boolean):void {
        if (_indexed == value) return;
        _indexed = value;
        dispatchEvent(new Event("indexedChanged"));
    }
}
}
