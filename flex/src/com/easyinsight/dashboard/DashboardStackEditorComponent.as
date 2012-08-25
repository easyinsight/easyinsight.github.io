package com.easyinsight.dashboard {
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.core.Container;
import mx.core.UIComponent;
import mx.managers.PopUpManager;

public class DashboardStackEditorComponent extends DashboardStackViewComponent implements IDashboardEditorComponent {

    public function DashboardStackEditorComponent() {
        super();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    private function dashboardPopulate(event:DashboardPopulateEvent):void {
        var box:DashboardBox = event.dashboardBox;
        var index:int = viewChildren.getItemIndex(box);
        if (box.element == null) {
            (getButtonsBox().getChildAt(index))["label"] = "Stack Item " + index;
        } else if (box.element is DashboardReport) {
            (getButtonsBox().getChildAt(index))["label"] = DashboardReport(box.element).report.name;
        } else if (box.element.label != null && box.element.label != "") {
            (getButtonsBox().getChildAt(index))["label"] = box.element.label;
        } else {
            (getButtonsBox().getChildAt(index))["label"] = "Stack Item " + index;
        }  
    }

    override protected function createComp(element:DashboardElement, i:int):UIComponent {
        var box:DashboardBox = new DashboardBox();
        box.addEventListener(DashboardPopulateEvent.DASHBOARD_POPULATE, dashboardPopulate);
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
            if (i < comps.length) {
                var box:DashboardBox = comps.getItemAt(i) as DashboardBox;
                box.save();
                var dashboardGridItem:DashboardStackItem = new DashboardStackItem();
                dashboardGridItem.position = i;
                dashboardGridItem.dashboardElement = box.element;
                items.addItem(dashboardGridItem);
            }
        }
        dashboardStack.gridItems = items;
    }

    override protected function editMode():Boolean {
        return true;
    }

    protected override function createChildren():void {
        if (dashboardStack.count == 0) {
            dashboardStack.count = 1;
            var eTemp:DashboardStackItem = new DashboardStackItem();
            dashboardStack.gridItems.addItem(eTemp);
        }
        super.createChildren();
        /*if (dashboardStack.count == 0) {
            var window:StackDimensionsWindow = new StackDimensionsWindow();
            window.dashboardStack = dashboardStack;
            window.addEventListener(GridDimensionEvent.GRID_DIMENSION, onDimensions, false, 0, true);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        }*/
        var button:Button = new Button();
        button.label = "Add";
        button.addEventListener(MouseEvent.CLICK, addStackElement);
        getButtonsBox().addChild(button);
         /*else {
            recreateStructure();
        }*/

    }
    
    private function deletePage(event:DashboardStackEvent):void {
        var button:UIComponent = event.currentTarget as UIComponent;
        dashboardStack.count--;
        var index:int = getButtonsBox().getChildIndex(button);
        dashboardStack.gridItems.removeItemAt(index);
        getButtonsBox().removeChildAt(index);
        viewStack.removeChildAt(index);
        viewChildren.removeItemAt(index);
    }

    override protected function createStackButton(index:int, label:String):UIComponent {
        var topButton:DashboardEditButton = new DashboardEditButton();
        topButton.dashboardStack = dashboardStack;
        topButton.addEventListener(DashboardStackEvent.DELETE_PAGE, deletePage);
        topButton.addEventListener(DashboardStackEvent.CLICK, onButtonClick);
        topButton.data = index;
        topButton.label = label;
        return topButton;
    }

    private function addStackElement(event:MouseEvent):void {
        dashboardStack.count++;
        var stackItem:DashboardStackItem = new DashboardStackItem();
        dashboardStack.gridItems.addItem(stackItem);
        addStackChild(stackItem, dashboardStack.gridItems.getItemIndex(stackItem));
    }

    private function onDimensions(event:GridDimensionEvent):void {
        dispatchEvent(new DashboardPopulateEvent(DashboardPopulateEvent.DASHBOARD_POPULATE));
        if (dashboardStack.count != stackChildSize()) {
            for (var i:int = stackChildSize(); i < dashboardStack.count; i++) {
                if (dashboardStack.gridItems.length > i) {

                } else {
                    var eTemp:DashboardStackItem = new DashboardStackItem();
                    dashboardStack.gridItems.addItem(eTemp);
                }
            }
            createStackContents();
        }
    }

    public function validate():String {
        var valid:String = null;
        var comps:ArrayCollection = stackComponents();
        if (comps.length != dashboardStack.count) {
            this.errorString = "You need to configure all children of this stack.";
            dispatchEvent(new MouseEvent(MouseEvent.MOUSE_OVER));
            valid = "You need to configure all children of this stack.";
        }
        if (valid) {
            for each (var box:DashboardBox in comps) {
                if (box.element == null) {
                    box.errorString = "You need to configure this section of the grid.";
                    box.dispatchEvent(new MouseEvent(MouseEvent.MOUSE_OVER));
                    valid = "You need to configure all children of this stack.";
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
        window.addEventListener(GridDimensionEvent.GRID_DIMENSION, onDimensions, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }
}
}