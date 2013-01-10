package com.easyinsight.dashboard {

import com.easyinsight.scorecard.ScorecardDescriptor;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.solutions.InsightDescriptor;

import flash.display.DisplayObject;
import flash.events.Event;
import flash.events.EventDispatcher;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.Canvas;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.controls.List;
import mx.controls.TextArea;
import mx.core.IUIComponent;
import mx.events.DragEvent;
import mx.managers.DragManager;

public class DashboardBox extends VBox implements IDashboardViewComponent {




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
        horizontalScrollPolicy = "off";
        verticalScrollPolicy = "off";
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    public var element:DashboardElement;

    private var dropBox:Box;

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    private var labelText:Label;

    private var _buttonsVisible:Boolean;

    [Bindable(event="buttonsVisibleChanged")]
    public function get buttonsVisible():Boolean {
        return _buttonsVisible;
    }

    public function set buttonsVisible(value:Boolean):void {
        if (_buttonsVisible == value) return;
        _buttonsVisible = value;
        dispatchEvent(new Event("buttonsVisibleChanged"));
    }

    public function clearValidation():void {
        dropBox.setStyle("borderColor", 0xCCCCCC);
        dropBox.setStyle("borderStyle", "none");
        dropBox.setStyle("borderThickness", 1);
        dropBox.setStyle("backgroundColor", 0xFFFFFF);
    }

    public function validationFail():void {
        dropBox.setStyle("borderColor", "red");
        dropBox.setStyle("borderStyle", "solid");
        dropBox.setStyle("borderThickness", 2);
        dropBox.setStyle("backgroundColor", 0xFFDDDD);
    }

    protected override function createChildren():void {
        super.createChildren();
        var topCanvas:Canvas = new Canvas();
        topCanvas.percentWidth = 100;
        topCanvas.setStyle("backgroundColor", 0xDDDDDD);
        labelText = new Label();
        labelText.percentWidth = 100;
        labelText.y = 3;
        labelText.setStyle("textAlign", "center");
        labelText.setStyle("fontSize", 10);
        topCanvas.addChild(labelText);
        topBox = new HBox();
        topBox.setStyle("horizontalAlign", "right");
        topBox.setStyle("verticalAlign", "middle");
        topBox.setStyle("paddingRight", 5);
        topBox.height = 16;
        topBox.percentWidth = 100;

        
        var editButton:LinkButton = new LinkButton();
        editButton.label = "Edit";
        editButton.setStyle("fontSize", 10);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        BindingUtils.bindProperty(editButton, "visible", this, "buttonsVisible");
        var deleteButton:LinkButton = new LinkButton();
        //deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.label = "Clear";
        deleteButton.setStyle("fontSize", 10);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        deleteButton.enabled = _allowDelete;
        BindingUtils.bindProperty(deleteButton, "visible", this, "buttonsVisible");
        topBox.addChild(editButton);
        topBox.addChild(deleteButton);
        topCanvas.addChild(topBox);
        addChild(topCanvas);
        dropBox = new Box();
        /*dropBox.setStyle("paddingLeft", 5);
        dropBox.setStyle("paddingRight", 5);
        dropBox.setStyle("paddingBottom", 5);
        dropBox.setStyle("paddingTop", 5);*/
        dropBox.percentHeight = 100;
        dropBox.percentWidth = 100;
        dropBox.setStyle("verticalAlign", "middle");
        dropBox.setStyle("horizontalAlign", "center");
        addChild(dropBox);
        if (element != null) {
            if (element is DashboardStack) {
                addButton = new LinkButton();
                addButton.label = "Add";
                addButton.addEventListener(MouseEvent.CLICK, stackAdd);
                addButton.setStyle("fontSize", 10);
                topBox.addChildAt(addButton, 0);
            }
            updateText();
            dropBox.setStyle("backgroundColor", 0xEEEEEE);
            if (editorComp == null) {
                editorComp = IDashboardEditorComponent(DashboardElementFactory.createEditorUIComponent(element, dashboardEditorMetadata));
            }
            dropBox.addChild(DisplayObject(editorComp));
            editorComp.initialRetrieve();
        } else {
            dropBox.setStyle("backgroundColor", 0xFFFFFF);
            dropBox.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
            dropBox.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
            var dropTextArea:TextArea = new TextArea();
            dropTextArea.setStyle("backgroundAlpha", 0);
            dropTextArea.setStyle("fontSize", 12);
            dropTextArea.text = "Drag a control or report from the left side options to here.";
            dropTextArea.width = 200;
            dropBox.addChild(dropTextArea);
        }
        buttonsVisible = element != null;
    }

    private function updateText():void {
        if (element is DashboardReport) {
            labelText.text = DashboardReport(element).report.name;
        } else {
            if (element.label == null) {
                if (element is DashboardGrid) {
                    labelText.text = "Grid";
                } else if (element is DashboardStack) {
                    labelText.text = "Stack";
                } else if (element is DashboardImage) {
                    labelText.text = "Image";
                } else if (element is DashboardTextElement) {
                    labelText.text = "Text";
                }
            } else {
                labelText.text = element.label;
            }
        }
    }

    private function stackAdd(event:MouseEvent):void {
        DashboardStackEditorComponent(editorComp).addStackElement();
    }

    private function onEdit(event:MouseEvent):void {
        if (element != null) {
            IDashboardEditorComponent(dropBox.getChildAt(0)).edit();
            //editorComp = null;
        }
    }

    private function onDelete(event:MouseEvent):void {
        if (element != null) {
            labelText.text = "";
            element = null;
            editorComp = null;
            if (addButton) {
                topBox.removeChild(addButton);
                addButton = null;
            }
            buttonsVisible = false;
            dropBox.removeAllChildren();
            dropBox.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
            dropBox.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
            dropBox.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
            dropBox.setStyle("backgroundColor", 0xFFFFFF);
            var dropTextArea:TextArea = new TextArea();
            dropTextArea.setStyle("backgroundAlpha", 0);
            dropTextArea.setStyle("fontSize", 12);
            dropTextArea.text = "Drag a control or report from the left side options to here.";
            dropTextArea.width = 200;
            dropTextArea.height = 35;
            dropBox.addChild(dropTextArea);
            dispatchEvent(new DashboardPopulateEvent(DashboardPopulateEvent.DASHBOARD_POPULATE, this));
            dispatchEvent(new DashboardChangedEvent());
        }
    }

    private function dragOverHandler(event:DragEvent):void {
        DragManager.showFeedback(DragManager.MOVE);
    }

    public function validate(results:Array):void {
        if (dropBox.getChildren().length == 0 || !(dropBox.getChildAt(0) is IDashboardEditorComponent)) {
            results.push("You need to add at least one element to the dashboard.");
        } else {
            errorString = null;
        }
        var comp:IDashboardEditorComponent = dropBox.getChildAt(0) as IDashboardEditorComponent;
        return comp.validate(results);
    }

    public function save():void {
        if (dropBox.getChildren().length > 0) {
            var comp:IDashboardEditorComponent = dropBox.getChildAt(0) as IDashboardEditorComponent;
            comp.save();
        }
    }
    
    public function component():IDashboardEditorComponent {
        if (dropBox.getChildren().length > 0) {
            return dropBox.getChildAt(0) as IDashboardEditorComponent;
        }
        return null;
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

        dropBox.removeAllChildren();
        this.element = null;
        editorComp = null;


        buttonsVisible = true;
        this.element = element;
        updateText();
        clearValidation();
        errorString = null;
        editorComp = IDashboardEditorComponent(DashboardElementFactory.createEditorUIComponent(element, dashboardEditorMetadata));
        if (element is DashboardStack) {
            addButton = new LinkButton();
            addButton.label = "Add";
            addButton.addEventListener(MouseEvent.CLICK, stackAdd);
            addButton.setStyle("fontSize", 10);
            topBox.addChildAt(addButton, 0);
        }
        EventDispatcher(editorComp).addEventListener(DashboardPopulateEvent.DASHBOARD_POPULATE, propagate);
        editorComp.initialRetrieve();
        dropBox.addChild(DisplayObject(editorComp));
        dropBox.setStyle("backgroundColor", 0xEEEEEE);
        dropBox.removeEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        dropBox.removeEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        dispatchEvent(new DashboardPopulateEvent(DashboardPopulateEvent.DASHBOARD_POPULATE, this));
        dispatchEvent(new DashboardChangedEvent());

    }

    private var topBox:HBox;
    private var addButton:LinkButton;

    private function propagate(event:DashboardPopulateEvent):void {
        dispatchEvent(new DashboardPopulateEvent(event.type, this));
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