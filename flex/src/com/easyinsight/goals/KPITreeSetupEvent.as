/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/20/11
 * Time: 9:14 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.goals {
import com.easyinsight.solutions.DataSourceDescriptor;

import flash.events.Event;

public class KPITreeSetupEvent extends Event {

    public static const KPI_TREE_SETUP:String = "kpiTreeSetup";

    public var dataSource:DataSourceDescriptor;

    public function KPITreeSetupEvent(dataSource:DataSourceDescriptor) {
        super(KPI_TREE_SETUP);
        this.dataSource = dataSource;
    }

    override public function clone():Event {
        return new KPITreeSetupEvent(dataSource);
    }
}
}
