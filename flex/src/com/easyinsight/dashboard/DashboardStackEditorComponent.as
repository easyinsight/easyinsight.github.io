package com.easyinsight.dashboard {
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.managers.PopUpManager;

public class DashboardStackEditorComponent extends HBox implements IDashboardEditorComponent {

    public var dashboardStack:DashboardStack;

    public function DashboardStackEditorComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    public function save():void {
        var items:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < dashboardStack.count; i++) {
            var box:DashboardBox = getChildAt(i) as DashboardBox;
            box.save();
            var dashboardGridItem:DashboardStackItem = new DashboardStackItem();
            dashboardGridItem.position = i;
            dashboardGridItem.dashboardElement = box.element;
            items.addItem(dashboardGridItem);
        }
        dashboardStack.gridItems = items;
    }

    protected override function createChildren():void {
        super.createChildren();
        if (dashboardStack.count == 0) {
            var window:StackDimensionsWindow = new StackDimensionsWindow();
            window.dashboardStack = dashboardStack;
            window.addEventListener(GridDimensionEvent.GRID_DIMENSION, onDimensions, false, 0, true);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        } else {
            recreateStructure();
        }
    }



    private function recreateStructure():void {
        removeAllChildren();
        for (var i:int = 0; i < dashboardStack.count; i++) {
            var box:DashboardBox = new DashboardBox();
            if (dashboardStack.gridItems.length > i) {
                var e:DashboardStackItem = dashboardStack.gridItems.getItemAt(i) as DashboardStackItem;
                box.element = e.dashboardElement;
            }
            addChild(box);
        }
    }

    private function onDimensions(event:GridDimensionEvent):void {
        if (dashboardStack.count != getChildren().length) {
            recreateStructure();
        }
    }

    public function validate():Boolean {
        var valid:Boolean = true;
        for (var i:int = 0; i < dashboardStack.count; i++) {
            var box:DashboardBox = getChildAt(i) as DashboardBox;
            if (box.element == null) {
                box.errorString = "You need to configure this section of the grid.";
                box.dispatchEvent(new MouseEvent(MouseEvent.MOUSE_OVER));
                valid = false;
            }
        }
        return valid;
    }

    public function edit():void {
        var window:StackDimensionsWindow = new StackDimensionsWindow();
        window.dashboardStack = dashboardStack;
        window.addEventListener(GridDimensionEvent.GRID_DIMENSION, onDimensions, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }
}
}