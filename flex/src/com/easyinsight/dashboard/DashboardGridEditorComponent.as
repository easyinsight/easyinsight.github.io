package com.easyinsight.dashboard {
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.Grid;
import mx.containers.GridItem;
import mx.containers.GridRow;
import mx.managers.PopUpManager;

public class DashboardGridEditorComponent extends Grid implements IDashboardEditorComponent {

    public var dashboardGrid:DashboardGrid;

    public function DashboardGridEditorComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    public function save():void {
        var items:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var row:GridRow = getChildAt(i) as GridRow;
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var item:GridItem = row.getChildAt(j) as GridItem;
                var box:DashboardBox = item.getChildAt(0) as DashboardBox;
                box.save();
                var dashboardGridItem:DashboardGridItem = new DashboardGridItem();
                dashboardGridItem.rowIndex = i;
                dashboardGridItem.columnIndex = j;
                dashboardGridItem.dashboardElement = box.element;
                items.addItem(dashboardGridItem);
            }
        }
        dashboardGrid.gridItems = items;
    }

    protected override function createChildren():void {
        super.createChildren();
        if (dashboardGrid.rows == 0 && dashboardGrid.columns == 0) {
            var window:GridDimensionsWindow = new GridDimensionsWindow();
            window.addEventListener(GridDimensionEvent.GRID_DIMENSION, onDimensions, false, 0, true);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        }
        recreateStructure();
    }

    private function recreateStructure():void {
        removeAllChildren();
        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var gridRow:GridRow = new GridRow();
            gridRow.percentWidth = 100;
            gridRow.percentHeight = 100;
            addChild(gridRow);
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var e:DashboardGridItem = findItem(i, j);
                var gridItem:GridItem = new GridItem();
                gridItem.setStyle("borderThickness", 1);
                gridItem.setStyle("borderStyle", "solid");
                gridItem.percentWidth = 100;
                gridItem.percentHeight = 100;
                var box:DashboardBox = new DashboardBox();
                if (e != null && e.dashboardElement != null) {
                    box.element = e.dashboardElement;
                }
                gridItem.addChild(box);
                gridRow.addChild(gridItem);
            }
        }
    }

    private function onDimensions(event:GridDimensionEvent):void {
        dashboardGrid.rows = event.rows;
        dashboardGrid.columns = event.columns;
        recreateStructure();
    }

    private function findItem(x:int, y:int):DashboardGridItem {
        for each (var e:DashboardGridItem in dashboardGrid.gridItems) {
            if (e.rowIndex == x && e.columnIndex == y) {
                return e;
            }
        }
        return null;
    }

    public function validate():Boolean {
        var valid:Boolean = true;
        for (var i:int = 0; i < dashboardGrid.rows; i++) {
            var row:GridRow = getChildAt(i) as GridRow;
            for (var j:int = 0; j < dashboardGrid.columns; j++) {
                var item:GridItem = row.getChildAt(j) as GridItem;
                var box:DashboardBox = item.getChildAt(0) as DashboardBox;
                if (box.element == null) {
                    box.errorString = "You need to configure this section of the grid.";
                    box.dispatchEvent(new MouseEvent(MouseEvent.MOUSE_OVER));
                    valid = false;
                }
            }
        }
        return valid;
    }

    public function edit():void {
        var window:DashboardEditWindow = new DashboardEditWindow();
        window.dashboardElement = dashboardGrid;
        PopUpManager.addPopUp(window, this, true);
    }
}
}