/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/3/11
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.scorecard {
import flash.events.Event;

public class ScorecardSaveEvent extends Event {

    public static const SCORECARD_SAVE:String = "scorecardSave";

    public var scorecard:Scorecard;

    public function ScorecardSaveEvent(scorecard:Scorecard) {
        super(SCORECARD_SAVE);
        this.scorecard = scorecard;
    }

    override public function clone():Event {
        return new ScorecardSaveEvent(scorecard);
    }
}
}
