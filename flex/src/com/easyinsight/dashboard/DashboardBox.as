package com.easyinsight.dashboard {
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Button;
import mx.controls.List;
import mx.core.IUIComponent;
import mx.events.DragEvent;
import mx.managers.DragManager;

public class DashboardBox extends VBox {

    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var _allowDelete:Boolean = true;

    public function set allowDelete(value:Boolean):void {
        _allowDelete = value;
    }

    public function DashboardBox() {
        super();
        setStyle("paddingTop", 0);
        setStyle("paddingLeft", 0);
        setStyle("paddingRight", 0);
        setStyle("paddingBottom", 0);
        setStyle("borderStyle", "solid");
        setStyle("borderThickness", 1);
        setStyle("verticalGap", 0);
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    public var element:DashboardElement;

    private var dropBox:Box;

    protected override function createChildren():void {
        super.createChildren();
        var topBox:HBox = new HBox();
        topBox.setStyle("horizontalAlign", "right");
        topBox.setStyle("verticalAlign", "middle");
        topBox.setStyle("paddingRight", 5);
        topBox.height = 30;
        topBox.setStyle("backgroundColor", 0xDDDDDD);
        topBox.percentWidth = 100;
        var editButton:Button = new Button();
        editButton.setStyle("icon", editIcon);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        var deleteButton:Button = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        deleteButton.enabled = _allowDelete;
        topBox.addChild(editButton);
        topBox.addChild(deleteButton);
        addChild(topBox);
        dropBox = new Box();
        dropBox.setStyle("paddingLeft", 20);
        dropBox.setStyle("paddingRight", 20);
        dropBox.setStyle("paddingBottom", 20);
        dropBox.setStyle("paddingTop", 20);
        dropBox.percentHeight = 100;
        dropBox.percentWidth = 100;
        addChild(dropBox);
        if (element != null) {
            dropBox.setStyle("backgroundColor", 0xEEEEEE);
            dropBox.addChild(element.createEditorComponent());
        } else {
            dropBox.setStyle("backgroundColor", 0xFFFFFF);
            dropBox.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
            dropBox.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        }
    }

    private function onEdit(event:MouseEvent):void {
        if (element != null) {
            IDashboardEditorComponent(dropBox.getChildAt(0)).edit();            
        }
    }

    private function onDelete(event:MouseEvent):void {
        if (element != null) {
            element = null;
            dropBox.removeAllChildren();
            dropBox.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
            dropBox.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
            dropBox.setStyle("backgroundColor", 0xFFFFFF);
            dispatchEvent(new DashboardChangedEvent());
        }
    }

    public function validate():Boolean {
        var comp:IDashboardEditorComponent = dropBox.getChildAt(0) as IDashboardEditorComponent;
        return comp.validate();
    }

    public function save():void {
        var comp:IDashboardEditorComponent = dropBox.getChildAt(0) as IDashboardEditorComponent;
        comp.save();
    }

    protected function dragEnterHandler(event:DragEvent):void {
        var data:ArrayCollection = ArrayCollection(List(event.dragInitiator).dataProvider);
        var source:Object = event.dragSource.dataForFormat("items")[0];
        DragManager.acceptDragDrop(event.currentTarget as IUIComponent);
    }

    protected function dragDropHandler(event:DragEvent):void {
        var data:ArrayCollection = ArrayCollection(List(event.dragInitiator).dataProvider);
        var source:Object = event.dragSource.dataForFormat("items")[0];
        var element:DashboardElement;
        if (source is InsightDescriptor) {
            var dashboardReport:DashboardReport = new DashboardReport();
            dashboardReport.report = source as InsightDescriptor;
            element = dashboardReport;
        } else if (source is DashboardControl) {
            var dashboardControl:DashboardControl = source as DashboardControl;
            element = dashboardControl.createElement();
        }
        if (this.element != null) {
            dropBox.removeAllChildren();
            this.element = null;
        }
        this.element = element;
        errorString = null;
        dropBox.addChild(element.createEditorComponent());
        dropBox.setStyle("backgroundColor", 0xEEEEEE);
        dropBox.removeEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        dropBox.removeEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        dispatchEvent(new DashboardChangedEvent());
    }
}
}