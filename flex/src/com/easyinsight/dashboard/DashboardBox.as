package com.easyinsight.dashboard {
import com.easyinsight.dashboard.DashboardElementFactory;
import com.easyinsight.scorecard.ScorecardDescriptor;
import com.easyinsight.solutions.InsightDescriptor;

import flash.display.DisplayObject;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.Canvas;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.List;
import mx.core.IUIComponent;
import mx.events.DragEvent;
import mx.managers.DragManager;

public class DashboardBox extends VBox implements IDashboardViewComponent {

    [Embed(source="../../../../assets/pencil.png")]
    private var editIcon:Class;

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var _allowDelete:Boolean = true;

    public function set allowDelete(value:Boolean):void {
        _allowDelete = value;
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo();
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

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    private var labelText:Label;

    protected override function createChildren():void {
        super.createChildren();
        var topCanvas:Canvas = new Canvas();
        topCanvas.percentWidth = 100;
        topCanvas.setStyle("backgroundColor", 0xDDDDDD);
        labelText = new Label();
        labelText.percentWidth = 100;
        labelText.y = 3;
        labelText.setStyle("textAlign", "center");
        labelText.setStyle("fontSize", 14);
        topCanvas.addChild(labelText);
        var topBox:HBox = new HBox();
        topBox.setStyle("horizontalAlign", "right");
        topBox.setStyle("verticalAlign", "middle");
        topBox.setStyle("paddingRight", 5);
        topBox.height = 30;
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
        topCanvas.addChild(topBox);
        addChild(topCanvas);
        dropBox = new Box();
        dropBox.setStyle("paddingLeft", 5);
        dropBox.setStyle("paddingRight", 5);
        dropBox.setStyle("paddingBottom", 5);
        dropBox.setStyle("paddingTop", 5);
        dropBox.percentHeight = 100;
        dropBox.percentWidth = 100;
        addChild(dropBox);
        if (element != null) {
            if (element is DashboardReport) {
                labelText.text = DashboardReport(element).report.name;
            } else {
                labelText.text = element.label;
            }
            dropBox.setStyle("backgroundColor", 0xEEEEEE);
            if (editorComp == null) {
                editorComp = IDashboardEditorComponent(DashboardElementFactory.createEditorUIComponent(element, dashboardEditorMetadata));
            }
            dropBox.addChild(DisplayObject(editorComp));
        } else {
            dropBox.setStyle("backgroundColor", 0xFFFFFF);
            dropBox.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
            dropBox.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        }
    }

    private function onEdit(event:MouseEvent):void {
        if (element != null) {
            IDashboardEditorComponent(dropBox.getChildAt(0)).edit();
            editorComp = null;
        }
    }

    private function onDelete(event:MouseEvent):void {
        if (element != null) {
            labelText.text = "";
            element = null;
            editorComp = null;
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
        } else if (source is ScorecardDescriptor) {
            var dashboardScorecard:DashboardScorecard = new DashboardScorecard();
            dashboardScorecard.scorecard = source as ScorecardDescriptor;
            element = dashboardScorecard;
        }
        if (this.element != null) {
            dropBox.removeAllChildren();
            this.element = null;
            editorComp = null;
        }
        labelText.text = element.label;
        this.element = element;
        errorString = null;
        editorComp = IDashboardEditorComponent(DashboardElementFactory.createEditorUIComponent(element, dashboardEditorMetadata));
        editorComp.initialRetrieve();
        dropBox.addChild(DisplayObject(editorComp));
        dropBox.setStyle("backgroundColor", 0xEEEEEE);
        dropBox.removeEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        dropBox.removeEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        dispatchEvent(new DashboardChangedEvent());
    }

    public var editorComp:IDashboardEditorComponent;

    public function refresh():void {
        if (editorComp != null) {
            editorComp.refresh();
        }
    }

    public function updateAdditionalFilters(filterMap:Object):void {
        if (editorComp != null) {
            editorComp.updateAdditionalFilters(filterMap);
        }
    }

    public function initialRetrieve():void {
        if (editorComp != null) {
            editorComp.initialRetrieve();
        }
    }

    public function reportCount():ArrayCollection {
        if (editorComp != null) {
            return editorComp.reportCount();
        }
        return new ArrayCollection();
    }

    public function toggleFilters(showFilters:Boolean):void {
    }
}
}