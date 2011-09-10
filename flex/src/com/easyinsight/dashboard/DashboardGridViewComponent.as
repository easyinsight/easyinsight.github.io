package com.easyinsight.dashboard {
import mx.collections.ArrayCollection;
import mx.containers.Grid;
import mx.containers.GridItem;
import mx.containers.GridRow;
import mx.core.UIComponent;

public class DashboardGridViewComponent extends Grid implements IDashboardViewComponent {

    public var dashboardGrid:DashboardGrid;

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    public function DashboardGridViewComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    private var viewChildren:ArrayCollection;

    protected override function createChildren():void {
        super.createChildren();
        viewChildren = new ArrayCollection();
        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var gridRow:GridRow = new GridRow();
            gridRow.percentWidth = 100;
            gridRow.percentHeight = 100;
            addChild(gridRow);
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var e:DashboardGridItem = findItem(i, j);
                var gridItem:GridItem = new GridItem();
                gridItem.setStyle("paddingLeft", dashboardGrid.paddingLeft);
                gridItem.setStyle("paddingRight", dashboardGrid.paddingRight);
                gridItem.setStyle("paddingTop", dashboardGrid.paddingTop);
                gridItem.setStyle("paddingBottom", dashboardGrid.paddingBottom);
                gridItem.percentWidth = 100;
                gridItem.percentHeight = 100;
                var child:UIComponent = DashboardElementFactory.createViewUIComponent(e.dashboardElement, dashboardEditorMetadata);
                viewChildren.addItem(child);
                gridItem.addChild(child);
                gridRow.addChild(gridItem);
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
        var reports:ArrayCollection = new ArrayCollection();
        for each (var comp:IDashboardViewComponent in viewChildren) {
            reports.addAll(comp.reportCount());
        }
        return reports;
    }
}
}