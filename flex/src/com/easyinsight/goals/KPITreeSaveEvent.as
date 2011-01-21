/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/20/11
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.goals {
import flash.events.Event;

public class KPITreeSaveEvent extends Event {

    public static const SAVE_KPI_TREE:String = "saveKPITree";

    public var goalTree:GoalTree;

    public function KPITreeSaveEvent(goalTree:GoalTree) {
        super(SAVE_KPI_TREE);
        this.goalTree = goalTree;
    }

    override public function clone():Event {
        return new KPITreeSaveEvent(goalTree);
    }
}
}
