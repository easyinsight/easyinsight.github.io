package com.easyinsight.dashboard {

import com.easyinsight.analysis.list.SizeOverrideEvent;

import mx.collections.ArrayCollection;
import mx.containers.Grid;
import mx.containers.GridItem;
import mx.containers.GridRow;
import mx.controls.Alert;
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
        verticalScrollPolicy = "off";
        horizontalScrollPolicy = "off";
        addEventListener(SizeOverrideEvent.SIZE_OVERRIDE, onSizeOverride);
    }
    
    private function onSizeOverride(event:SizeOverrideEvent):void {
        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var gridRow:GridRow = getChildAt(i) as GridRow;
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var e:DashboardGridItem = findItem(i, j);
                var gridItem:GridItem = gridRow.getChildAt(j) as GridItem;
                var childSizeInfo:SizeInfo = IDashboardViewComponent(gridItem.getChildAt(0)).obtainPreferredSizeInfo();
                if (childSizeInfo.preferredWidth != 0) {
                    gridItem.width = childSizeInfo.preferredWidth + dashboardGrid.paddingLeft + dashboardGrid.paddingRight;
                    gridItem.percentWidth = NaN;
                } else {
                    gridItem.width = NaN;
                    gridItem.percentWidth = 100;
                }
                if (childSizeInfo.preferredHeight != 0) {
                    gridItem.height = childSizeInfo.preferredHeight + dashboardGrid.paddingTop + dashboardGrid.paddingBottom;
                    gridItem.percentHeight = NaN;
                } else {
                    gridItem.height = NaN;
                    gridItem.percentHeight = 100;
                }
            }
        }
    }

    private var viewChildren:ArrayCollection;

    protected override function createChildren():void {
        super.createChildren();
        var gridAbsoluteHeight:Boolean = false;
        for (var i1:int = 0; i1 < dashboardGrid.rows; i1++) {
            for (var j1:int = 0; j1 < dashboardGrid.columns; j1++) {
                var e1:DashboardGridItem = findItem(i1, j1);
                var child1:UIComponent = DashboardElementFactory.createViewUIComponent(e1.dashboardElement, dashboardEditorMetadata);
                var childSizeInfo1:SizeInfo = IDashboardViewComponent(child1).obtainPreferredSizeInfo();
                if (childSizeInfo1.preferredWidth != 0) {

                }
                if (childSizeInfo1.preferredHeight != 0 || childSizeInfo1.autoCalcHeight) {
                    gridAbsoluteHeight = true;
                }
            }
        }
        setStyle("backgroundColor", dashboardGrid.backgroundColor);
        setStyle("backgroundAlpha", dashboardGrid.backgroundAlpha);
        viewChildren = new ArrayCollection();

        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var gridRow:GridRow = new GridRow();
            gridRow.setStyle("paddingLeft", 0);
            gridRow.setStyle("paddingRight", 0);
            gridRow.setStyle("paddingTop", 0);
            gridRow.setStyle("paddingBottom", 0);
            addChild(gridRow);
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var e:DashboardGridItem = findItem(i, j);
                var gridItem:GridItem = new GridItem();

                var child:UIComponent = DashboardElementFactory.createViewUIComponent(e.dashboardElement, dashboardEditorMetadata);
                var childSizeInfo:SizeInfo = IDashboardViewComponent(child).obtainPreferredSizeInfo();
                if (childSizeInfo.preferredWidth == 0) {
                    gridItem.percentWidth = 100;
                }
                if (gridAbsoluteHeight) {
                    if (childSizeInfo.preferredHeight == 0 && !childSizeInfo.autoCalcHeight) {
                        gridRow.height = 400;
                        childSizeInfo.preferredHeight = 400;
                    }
                } else {
                    gridItem.percentHeight = 100; 
                }

                if (e.dashboardElement is DashboardReport || e.dashboardElement is DashboardTextElement) {
                    gridItem.setStyle("paddingLeft", 3);
                    gridItem.setStyle("paddingRight", 3);
                    gridItem.setStyle("paddingTop", 3);
                    gridItem.setStyle("paddingBottom", 3);
                }

                viewChildren.addItem(child);
                gridItem.addChild(child);
                gridRow.addChild(gridItem);
            }

            gridRow.percentWidth = 100;
            if (!gridAbsoluteHeight) {
                gridRow.percentHeight = 100;
            }
        }
        percentWidth = 100;
        if (!gridAbsoluteHeight) {
            percentHeight = 100;
        }
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var e:DashboardGridItem = findItem(i, j);
                e.dashboardElement;
            }
        }
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