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
        setStyle("paddingLeft", 0);
        setStyle("paddingRight", 0);
        setStyle("paddingTop", 0);
        setStyle("paddingBottom", 0);
        setStyle("horizontalGap", 0);
        setStyle("verticalGap", 0);
    }

    private var viewChildren:ArrayCollection;

    protected override function createChildren():void {
        super.createChildren();
        if (!dashboardEditorMetadata.dashboard.absoluteSizing) {
            this.percentWidth = 100;
            this.percentHeight = 100;
        }
        setStyle("backgroundColor", dashboardGrid.backgroundColor);
        setStyle("backgroundAlpha", dashboardGrid.backgroundAlpha);
        viewChildren = new ArrayCollection();
        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var gridRow:GridRow = new GridRow();
            if (!dashboardEditorMetadata.dashboard.absoluteSizing) {
                gridRow.percentWidth = 100;
                gridRow.percentHeight = 100;
            }
            gridRow.setStyle("paddingLeft", 0);
            gridRow.setStyle("paddingRight", 0);
            gridRow.setStyle("paddingTop", 0);
            gridRow.setStyle("paddingBottom", 0);
            addChild(gridRow);
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var e:DashboardGridItem = findItem(i, j);
                var gridItem:GridItem = new GridItem();
                /*gridItem.setStyle("paddingLeft", dashboardGrid.paddingLeft);
                gridItem.setStyle("paddingRight", dashboardGrid.paddingRight);
                gridItem.setStyle("paddingTop", dashboardGrid.paddingTop);
                gridItem.setStyle("paddingBottom", dashboardGrid.paddingBottom);*/

                var child:UIComponent = DashboardElementFactory.createViewUIComponent(e.dashboardElement, dashboardEditorMetadata);
                var childSizeInfo:SizeInfo = IDashboardViewComponent(child).obtainPreferredSizeInfo();
                if (childSizeInfo.preferredWidth != 0) {
                    gridItem.width = childSizeInfo.preferredWidth + dashboardGrid.paddingLeft + dashboardGrid.paddingRight;
                } else {
                    gridItem.percentWidth = 100;
                }
                if (childSizeInfo.preferredHeight != 0) {
                    gridItem.height = childSizeInfo.preferredHeight + dashboardGrid.paddingTop + dashboardGrid.paddingBottom;
                } else {
                    gridItem.percentHeight = 100;
                }

                if (e.dashboardElement is DashboardReport) {
                    gridItem.setStyle("paddingLeft", 3);
                    gridItem.setStyle("paddingRight", 3);
                    gridItem.setStyle("paddingTop", 3);
                    gridItem.setStyle("paddingBottom", 3);
                }

                viewChildren.addItem(child);
                gridItem.addChild(child);
                gridRow.addChild(gridItem);
            }
        }
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo(dashboardGrid.preferredWidth, dashboardGrid.preferredHeight);
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


    public function toggleFilters(showFilters:Boolean):void {
        for each (var comp:IDashboardViewComponent in viewChildren) {
            comp.toggleFilters(showFilters);
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