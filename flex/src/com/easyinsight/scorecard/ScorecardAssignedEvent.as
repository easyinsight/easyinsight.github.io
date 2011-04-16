/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 4/13/11
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.scorecard {
import flash.events.Event;

public class ScorecardAssignedEvent extends Event {

    public static const SCORECARD_ASSIGNED:String = "scorecardAssigned";

    public var scorecard:Scorecard;

    public function ScorecardAssignedEvent(scorecard:Scorecard) {
        super(SCORECARD_ASSIGNED);
        this.scorecard = scorecard;
    }

    override public function clone():Event {
        return new ScorecardAssignedEvent(scorecard);
    }
}
}
