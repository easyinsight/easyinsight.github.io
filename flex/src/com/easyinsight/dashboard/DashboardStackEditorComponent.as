package com.easyinsight.dashboard {
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.core.Container;
import mx.core.UIComponent;
import mx.managers.PopUpManager;

public class DashboardStackEditorComponent extends DashboardStackViewComponent implements IDashboardEditorComponent {

    public function DashboardStackEditorComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    override protected function createComp(element:DashboardElement, i:int):UIComponent {
        var box:DashboardBox = new DashboardBox();
        box.dashboardEditorMetadata = dashboardEditorMetadata;
        if (element == null) {

        } else {
            var comp:UIComponent = DashboardElementFactory.createEditorUIComponent(element, dashboardEditorMetadata);
            if (comp is DashboardStackViewComponent) {
                DashboardStackViewComponent(comp).stackFilterMap = this.stackFilterMap;
            } else if (comp is DashboardReportViewComponent) {
                //DashboardReportViewComponent(comp).stackFilterMap = this.stackFilterMap;
            }
            if (dashboardStack.consolidateHeaderElements) {
                var filterContainer:Container = new HBox();
                childFilters.addItem(filterContainer);
                if (i == 0) {
                    childFilterBox.addChild(filterContainer);
                }
                if (dashboardStack.consolidateHeaderElements && comp is DashboardStackViewComponent) {
                    DashboardStackViewComponent(comp).consolidateHeader = filterContainer;
                } else if (dashboardStack.consolidateHeaderElements && comp is DashboardReportViewComponent) {
                    //DashboardReportViewComponent(comp).consolidateHeader = filterContainer;
                }
            }

            box.element = element;
            box.editorComp = IDashboardEditorComponent(comp);
        }
        return box;
    }

    public function save():void {
        var comps:ArrayCollection = stackComponents();
        var items:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < dashboardStack.count; i++) {
            var box:DashboardBox = comps.getItemAt(i) as DashboardBox;
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
        } /*else {
            recreateStructure();
        }*/
    }



    /*private function recreateStructure():void {
        removeAllChildren();
        for (var i:int = 0; i < dashboardStack.count; i++) {
            var box:DashboardBox = new DashboardBox();
            box.dashboardEditorMetadata = dashboardEditorMetadata;
            if (dashboardStack.gridItems.length > i) {
                var e:DashboardStackItem = dashboardStack.gridItems.getItemAt(i) as DashboardStackItem;
                box.element = e.dashboardElement;
            } else {
                var eTemp:DashboardStackItem = new DashboardStackItem();
                dashboardStack.gridItems.addItem(eTemp);
            }
            addChild(box);
        }
    }*/

    private function onDimensions(event:GridDimensionEvent):void {
        if (dashboardStack.count != stackChildSize()) {
            for (var i:int = 0; i < dashboardStack.count; i++) {
                if (dashboardStack.gridItems.length > i) {

                } else {
                    var eTemp:DashboardStackItem = new DashboardStackItem();
                    dashboardStack.gridItems.addItem(eTemp);
                }
            }
            createStackContents();
        }
    }

    public function validate():Boolean {
        var valid:Boolean = true;
        var comps:ArrayCollection = stackComponents();
        if (comps.length != dashboardStack.count) {
            this.errorString = "You need to configure all children of this stack.";
            dispatchEvent(new MouseEvent(MouseEvent.MOUSE_OVER));
            valid = false;
        }
        if (valid) {
            for each (var box:DashboardBox in comps) {
                if (box.element == null) {
                    box.errorString = "You need to configure this section of the grid.";
                    box.dispatchEvent(new MouseEvent(MouseEvent.MOUSE_OVER));
                    valid = false;
                } else {
                    valid = valid && box.validate();
                }
            }
        }
        return valid;
    }

    public function edit():void {
        var window:StackDimensionsWindow = new StackDimensionsWindow();
        window.dashboardStack = dashboardStack;
        window.availableDimensions = dashboardEditorMetadata.availableFields;
        window.dataSourceID = dashboardEditorMetadata.dataSourceID;
        window.allFields = dashboardEditorMetadata.allFields;
        //window.addEventListener(GridDimensionEvent.GRID_DIMENSION, onDimensions, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }
}
}