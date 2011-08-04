/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;

import flash.events.Event;

public class FieldFilterEvent extends Event {

    public static const REMOVE_FIELD:String = "removeField";
    public static const ADD_FIELD:String = "removeField";

    public var analysisItem:AnalysisItem;

    public function FieldFilterEvent(type:String,  analysisItem:AnalysisItem) {
        super(type, true);
        this.analysisItem = analysisItem;
    }

    override public function clone():Event {
        return new FieldFilterEvent(type, analysisItem);
    }
}
}
