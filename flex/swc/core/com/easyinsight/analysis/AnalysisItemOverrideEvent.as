/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 12/15/10
 * Time: 10:49 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class AnalysisItemOverrideEvent extends Event {

    public static const ANALYSIS_ITEM_OVERRIDE:String = "analysisItemOverride";

    public var analysisItemOverride:AnalysisItemOverride;

    public function AnalysisItemOverrideEvent(analysisItemOverride:AnalysisItemOverride) {
        super(ANALYSIS_ITEM_OVERRIDE, true);
        this.analysisItemOverride = analysisItemOverride;
    }

    override public function clone():Event {
        return new AnalysisItemOverrideEvent(analysisItemOverride);
    }
}
}
