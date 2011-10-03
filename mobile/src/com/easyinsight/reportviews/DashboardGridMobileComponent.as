/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.dashboard.DashboardEditorMetadata;
import com.easyinsight.dashboard.DashboardGrid;
import com.easyinsight.dashboard.DashboardGridItem;
import com.easyinsight.dashboard.IDashboardViewComponent;

import mx.collections.ArrayCollection;

import mx.core.UIComponent;

import spark.components.HGroup;

import spark.components.VGroup;

public class DashboardGridMobileComponent extends VGroup implements IDashboardViewComponent {

    public var dashboardGrid:DashboardGrid;
    public var dashboardEditorMetadata:DashboardEditorMetadata;

    private var viewChildren:ArrayCollection = new ArrayCollection();

    public function DashboardGridMobileComponent() {
        super();
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var hgroup:HGroup = new HGroup();
            hgroup.percentHeight = 100;
            hgroup.percentWidth = 100;
            addElement(hgroup);
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var e:DashboardGridItem = findItem(i, j);
                var child:UIComponent = DashboardMobileFactory.createViewUIComponent(e.dashboardElement, dashboardEditorMetadata) as UIComponent;
                viewChildren.addItem(child);
                hgroup.addElement(child);
            }
        }
    }

    private function findItem(x:int, y:int):DashboardGridItem {
        for each (var e:DashboardGridItem in dashboardGrid.gridItems) {
            if (e.rowIndex == x && e.columnIndex == y) {
                return e;
            }
        }
        return null;
    }

    public function refresh():void {
        for each (var comp:IDashboardViewComponent in viewChildren) {
            comp.refresh();
        }
    }

    public function updateAdditionalFilters(filters:Object):void {
        for each (var comp:IDashboardViewComponent in viewChildren) {
            comp.updateAdditionalFilters(filters);
        }
    }

    public function initialRetrieve():void {
        for each (var comp:IDashboardViewComponent in viewChildren) {
            comp.initialRetrieve();
        }
    }

    public function reportCount():ArrayCollection {
        return null;
    }
}
}
