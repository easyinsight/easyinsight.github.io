package com.easyinsight.analysis
{
import com.easyinsight.WindowManagement;
import com.easyinsight.analysis.crosstab.CrosstabDefinition;
import com.easyinsight.commands.CommandEvent;
import com.easyinsight.listing.ArghButton;


import flash.display.Bitmap;
import flash.display.BitmapData;

import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.geom.Point;
import flash.ui.Keyboard;

import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.controls.AdvancedDataGrid;
import mx.controls.DataGrid;
import mx.controls.Image;
import mx.controls.List;
import mx.controls.Text;
import mx.core.Application;
import mx.core.DragSource;
import mx.core.IUIComponent;
import mx.core.UIComponent;
import mx.events.DragEvent;
import mx.events.MenuEvent;
import mx.managers.DragManager;
import mx.managers.PopUpManager;

public class DropArea extends Box
{
    private var _analysisItem:AnalysisItem;

    private var _analysisItems:ArrayCollection;

    private var _report:AnalysisDefinition;

    private var _dataSourceID:int;

    public function set dataSourceID(value:int):void {
        _dataSourceID = value;
    }

    private function onItemClick(event:MenuEvent):void {
        var target:String = event.item.data;
        if (target == "deleteField") {
            deletion();
        } else if (target == "editFieldProperties") {
            editEvent(null, 0);
        } else if (target == "dateSwitch") {
            AnalysisDateDimension(_analysisItem).dateLevel = event.item.dateLevel;
            dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, this.analysisItem)));
        } else if (target == "filterOnField") {
            dispatchEvent(new ReportEditorFieldEvent(ReportEditorFieldEvent.ITEM_FILTER, new AnalysisItemWrapper(new AnalysisItemNode(_analysisItem))));
        } else if (target == "xtabSwap") {
            dispatchEvent(new CommandEvent(new DropAreaCrosstabSwapCommand()));
        } else if (target == "moveField") {
            var dragSource:DragSource = new DragSource();
            var bd:BitmapData = new BitmapData(this.width, this.height);
            bd.draw(this);
            var bitmap:Bitmap = new Bitmap(bd);
            var image:Image = new Image();
            image.source = bitmap;
            var mouseEvent:MouseEvent = new MouseEvent(MouseEvent.MOUSE_DOWN);
            DragManager.doDrag(this, dragSource, mouseEvent, image);
        }
    }

    private var argh:ArghButton = new ArghButton();
    private var notDone:Text = new Text();
    private var notDoneBox:Box = new Box();

    public function DropArea()
    {
        super();
        this.setStyle("fontSize", 12);
        argh.styleName = "flatWhiteButton";
        argh.setStyle("popUpStyleName", "dropAreaPopup");
        //horizontalScrollPolicy = "off";
        this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
        this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
        //this.addEventListener(MouseEvent.CLICK, onClick);
        argh.addEventListener(MenuEvent.ITEM_CLICK, onItemClick);
        argh.labelField = "label";
        argh.openAlways = true;
        this.addEventListener(KeyboardEvent.KEY_UP, keyPressed);
        notDoneBox.setStyle("cornerRadius", 2);
        notDoneBox.setStyle("borderColor", 0x357ebd);
        notDoneBox.setStyle("borderWidth", 1);
        notDoneBox.setStyle("borderStyle", "solid");
        notDoneBox.height = 27;
        notDoneBox.setStyle("backgroundColor", 0x0084B4);
        notDoneBox.setStyle("paddingLeft", 5);
        notDoneBox.setStyle("paddingRight", 5);
        notDoneBox.setStyle("paddingTop", 1);
        notDoneBox.addChild(notDone);
        notDone.selectable = false;
        notDone.text = getNoDataLabel();
        notDone.setStyle("color", 0xFFFFFF);
        notDone.setStyle("fontSize", 14);
        addChild(notDoneBox);
        setStyle("verticalAlign", "middle");
    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    public function highlight(analysisItem:AnalysisItem):Boolean {
        var valid:Boolean = recommend(analysisItem);
        if (valid) {
            if (notDoneBox.parent) {
                notDoneBox.setStyle("backgroundColor", "green");
            } else if (argh.parent) {
                argh.setStyle("fillColors", [0x008800, 0x008800, 0x008800, 0x008800]);
                argh.setStyle("themeColor", 0x008800);
            }
        }
        return valid;
    }

    public function normal():void {
        if (notDoneBox.parent) {
            notDoneBox.setStyle("backgroundColor", 0x0084B4);
        } else if (argh.parent) {
            argh.setStyle("fillColors", [0x0084B4, 0x0084B4, 0x0084B4, 0x0084B4]);
        }
    }

    private function onDelete(event:Event):void {
        deletion();
    }

    private var _dataSourceFields:ArrayCollection;

    public function set dataSourceFields(value:ArrayCollection):void {
        _dataSourceFields = value;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        this._analysisItems = analysisItems;
    }

    protected function supportsDrilldown():Boolean {
        return true;
    }

    public function customEditor():Class {
        if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
            return CalculationMeasureWindow;
        } else if (analysisItem.hasType(AnalysisItemTypes.DERIVED_GROUPING)) {
            return DerivedGroupingWindow;
        } else if (analysisItem.hasType(AnalysisItemTypes.DERIVED_DATE)) {
            return DerivedDateWindow;
        } else if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
            return HierarchyWindow;
        } else {
            return null;
        }
    }

    protected function editEvent(event:MouseEvent, initialWindow:int = 0):void {
        var analysisItemEditor:AnalysisItemEditWindow = new AnalysisItemEditWindow();
        analysisItemEditor.editorClass = customEditor();
        analysisItemEditor.dataSourceID = _dataSourceID;
        analysisItemEditor.analysisItems = this._analysisItems;
        analysisItemEditor.analysisItem = this.analysisItem;
        analysisItemEditor.report = this._report;
        analysisItemEditor.initialWindow = initialWindow;
        if (_report != null) {
            analysisItemEditor.reportType = _report.reportType;
        }
        if (startX > 0) {
            analysisItemEditor.x = startX;
            analysisItemEditor.y = startY;
        } else {
            var p:Point = new Point(this.x, this.y);
            var g:Point = localToGlobal(p);
            analysisItemEditor.x = g.x;
            analysisItemEditor.y = g.y;
        }
        WindowManagement.manager.addWindow(analysisItemEditor);
        PopUpManager.addPopUp(analysisItemEditor, UIComponent(Application.application));
        dispatchEvent(new FieldEditorEvent(FieldEditorEvent.FIELD_EDITOR_OPENED, analysisItemEditor));
        analysisItemEditor.addEventListener(AnalysisItemEditEvent.ANALYSIS_ITEM_EDIT, itemEdited, false, 0, true);
        analysisItemEditor.addEventListener(Event.CLOSE, onClose, false, 0, true);
    }

    private function onClose(event:Event):void {
        dispatchEvent(new FieldEditorEvent(FieldEditorEvent.FIELD_EDITOR_CLOSED, event.currentTarget as AnalysisItemEditWindow));
    }

    public var startX:int;
    public var startY:int;

    private function itemEdited(event:AnalysisItemEditEvent):void {
        dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, event.analysisItem)));
    }

    public function createAnalysisItem():AnalysisItem {
        return analysisItem;
    }

    public function get analysisItem():AnalysisItem {
        return this._analysisItem;
    }

    public function set analysisItem(analysisItem:AnalysisItem):void {
        /*if (this._analysisItem != null) {
            getChildAt(0).removeEventListener(MouseEvent.MOUSE_DOWN, onMouseDown);
        }*/
        if (_analysisItem == null && analysisItem != null) {
            removeChild(notDoneBox);
            addChild(argh);
        } else if (_analysisItem != null && analysisItem == null) {
            removeChild(argh);
            addChild(notDoneBox);
        }
        this._analysisItem = analysisItem;

        var options:ArrayCollection = new ArrayCollection([{label: "Filter on the Field...", data: "filterOnField"},
            {label: "Edit Field Properties...", data: "editFieldProperties"},
            {label: "Remove the Field from Report", data: "deleteField"},
            {label: "Move the Field in the Report", data: "moveField"}]);
        if (_analysisItem != null && _analysisItem.hasType(AnalysisItemTypes.DATE)) {
            options.addItem({type: "separator"});
            options.addItem({label: "Year", data: "dateSwitch", dateLevel: AnalysisItemTypes.YEAR_LEVEL});
            options.addItem({label: "Quarter - Year", data: "dateSwitch", dateLevel: AnalysisItemTypes.QUARTER_OF_YEAR});
            options.addItem({label: "Month - Year", data: "dateSwitch", dateLevel: AnalysisItemTypes.MONTH_LEVEL});
            options.addItem({label: "Week - Year", data: "dateSwitch", dateLevel: AnalysisItemTypes.WEEK_LEVEL});
            options.addItem({label: "Day - Month - Year", data: "dateSwitch", dateLevel: AnalysisItemTypes.DAY_LEVEL});
        }
        if (_report is CrosstabDefinition && CrosstabDefinition(_report).columns != null && CrosstabDefinition(_report).columns.length > 0 &&
                _analysisItem == CrosstabDefinition(_report).columns.getItemAt(0) && CrosstabDefinition(_report).rows != null && CrosstabDefinition(_report).rows.length > 0) {
            options.addItem({type: "separator"});
            options.addItem({label: "Swap with Column", data: "xtabSwap"});

        } else if (_report is CrosstabDefinition && CrosstabDefinition(_report).rows != null && CrosstabDefinition(_report).rows.length > 0 &&
                _analysisItem == CrosstabDefinition(_report).rows.getItemAt(0) && CrosstabDefinition(_report).columns != null && CrosstabDefinition(_report).columns.length > 0) {
            options.addItem({type: "separator"});
            options.addItem({label: "Swap with Row", data: "xtabSwap"});
        }
        argh.dataProvider = options;

        if (analysisItem == null) {
            notDone.text = getNoDataLabel();
            currentState = "";
        } else {
            argh.label = analysisItem.unqualifiedDisplay;
        }
    }

    protected function getNoDataLabel():String {
        return null;
    }

    public function dragEnterHandler(event:DragEvent):void {
        var analysisItem:AnalysisItem = null;
        var okay:Boolean = true;
        if (event.dragInitiator is DataGrid) {
            var initialList:DataGrid = event.dragInitiator as DataGrid;
            var newAnalysisItem:AnalysisItemWrapper = initialList.selectedItem as AnalysisItemWrapper;
            analysisItem = newAnalysisItem.analysisItem;
        } else if (event.dragInitiator is DropArea) {
            if (this.analysisItem == null) {
                okay = false;
            } else {
                var dropArea:DropArea = event.dragInitiator as DropArea;
                analysisItem = dropArea.analysisItem;
            }
        } else if (event.dragInitiator is AdvancedDataGrid) {
            var analysisItemLabel:AdvancedDataGrid = event.dragInitiator as AdvancedDataGrid;
            newAnalysisItem = analysisItemLabel.selectedItem as AnalysisItemWrapper;
            if (newAnalysisItem.isAnalysisItem()) {
                analysisItem = newAnalysisItem.analysisItem;
            } else {
                okay = false;
            }
        }
        if (okay && accept(analysisItem)) {
            if (notDoneBox.parent) {
                notDoneBox.setStyle("backgroundColor", "green");
            } else if (argh.parent) {
                argh.setStyle("fillColors", ["green", "green", "green", "green"]);
            }

            DragManager.acceptDragDrop(event.currentTarget as IUIComponent);
        }
    }


    public function get analysisItems():ArrayCollection {
        return _analysisItems;
    }

    public function accept(analysisItem:AnalysisItem):Boolean {
        return true;
    }

    public function recommend(analysisItem:AnalysisItem):Boolean {
        return true;
    }

    public function isConfigured():Boolean {
        return _analysisItem != null;
    }

    public function getDropAreaType():String {
        return null;
    }

    public function keyPressed(event:KeyboardEvent):void {
        if (event.keyCode == Keyboard.DELETE) {
            dispatchEvent(new CommandEvent(new DragAreaDeleteCommand(this)));
        }
    }

    public function deletion():void {
        dispatchEvent(new AnalysisChangedEvent());
        dispatchEvent(new DropAreaDeletionEvent(this));
    }

    public function dragDropHandler(event:DragEvent):void {
        if (event.dragInitiator is DataGrid) {
            var newAnalysisItem:AnalysisItemWrapper = event.dragSource.dataForFormat("items")[0] as AnalysisItemWrapper;
            if (this.analysisItem == null) {
                dispatchEvent(new CommandEvent(new DropAreaAddedCommand(this, newAnalysisItem.analysisItem)));
            } else {
                dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, newAnalysisItem.analysisItem)));
            }
        } else if (event.dragInitiator is DropArea) {
            var dropArea:DropArea = event.dragInitiator as DropArea;
            dispatchEvent(new CommandEvent(new DropAreaSwapCommand(dropArea, this)));
        } else if (event.dragInitiator is AdvancedDataGrid) {
            newAnalysisItem = event.dragSource.dataForFormat("treeDataGridItems")[0] as AnalysisItemWrapper;
            if (newAnalysisItem.isAnalysisItem()) {
                if (this.analysisItem == null) {
                    dispatchEvent(new CommandEvent(new DropAreaAddedCommand(this, newAnalysisItem.analysisItem)));
                } else {
                    dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, newAnalysisItem.analysisItem)));
                }
            }
        } else if (event.dragInitiator is List) {
            var list:List = event.dragInitiator as List;
            var newListItem:AnalysisItem = event.dragSource.dataForFormat("items")[0];
            if (this.analysisItem == null) {
                dispatchEvent(new CommandEvent(new DropAreaAddedCommand(this, newListItem)));
            } else {
                dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, newListItem)));
            }
        }
    }

    protected function dragOverHandler(event:DragEvent):void {
        DragManager.showFeedback(DragManager.MOVE);
    }

    protected function dragExitHandler(event:DragEvent):void {
        normal();
        //setStyle("borderColor", 0xB7BABC);
    }
}
}