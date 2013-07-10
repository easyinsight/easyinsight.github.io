/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/13
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.analysis.ReportFormItem;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.controls.LinkButton;
import mx.managers.PopUpManager;

public class DashboardFilterFormItem extends ReportFormItem {

    public var button:LinkButton;
    private var overrideFilters:ArrayCollection;
    public var filterDefinitions:ArrayCollection;

    public function DashboardFilterFormItem(label:String, property:String, value:Object, report:Object,
                                           filterDefinitions:ArrayCollection) {
        super(label, property, value, report);
        this.filterDefinitions = filterDefinitions;
    }

    protected override function createChildren():void {
        super.createChildren();

        button = new LinkButton();
        button.setStyle("left", 0);
        button.setStyle("paddingLeft", 0);
        button.label = "Edit...";
        button.addEventListener(MouseEvent.CLICK, onClick);

        if (this.value != null) overrideFilters = ArrayCollection(this.value);
        addChild(button);
    }

    private function onClick(event:MouseEvent):void {
        var window:DashboardFilterOverrideWindow = new DashboardFilterOverrideWindow();
        window.filterDefs = filterDefinitions;
        window.dashboardElement = this.report as DashboardElement;
        window.addEventListener(DashboardFilterOverrideEvent.DASHBOARD_FILTER_OVERRIDE, onDashboardFilter, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onDashboardFilter(event:DashboardFilterOverrideEvent):void {
        overrideFilters = event.filters;
    }

    override protected function getValue():Object {
        return overrideFilters;
    }
}
}
