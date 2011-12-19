/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/3/11
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.dashboard.IDashboardViewComponent;

import mx.core.UIComponent;
import mx.states.IOverride;

import spark.components.Group;

public class RetrieveDataAction implements IOverride {

    private var component:IDashboardViewComponent;
    private var filterMap:Object;

    public function RetrieveDataAction(component:IDashboardViewComponent, filterMap:Object) {
        this.component = component;
        this.filterMap = filterMap;
    }

    public function initialize():void {
    }

    public function apply(parent:UIComponent):void {
        component.updateAdditionalFilters(filterMap);
        component.initialRetrieve();
    }

    public function remove(parent:UIComponent):void {
    }
}
}
