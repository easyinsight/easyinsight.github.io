/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/18/11
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class AnalysisItemAddedEvent extends Event {

    public static const ANALYSIS_ITEM_ADDED:String = "analysisItemAdded";

    public var analysisItem:AnalysisItem;

    public function AnalysisItemAddedEvent(analysisItem:AnalysisItem) {
        super(ANALYSIS_ITEM_ADDED);
        this.analysisItem = analysisItem;
    }

    override public function clone():Event {
        return new AnalysisItemAddedEvent(analysisItem);
    }
}
}
