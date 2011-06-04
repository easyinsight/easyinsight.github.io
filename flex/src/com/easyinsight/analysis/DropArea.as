package com.easyinsight.analysis
{
import com.easyinsight.commands.CommandEvent;

import com.easyinsight.util.PopUpUtil;

import flash.display.Bitmap;
import flash.display.BitmapData;
import flash.events.ContextMenuEvent;
import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.ui.ContextMenuItem;
import flash.ui.Keyboard;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.AdvancedDataGrid;
import mx.controls.Button;
import mx.controls.DataGrid;
import mx.controls.Image;
import mx.controls.List;
import mx.core.DragSource;
import mx.core.IUIComponent;
import mx.core.UIComponent;
import mx.events.DragEvent;
import mx.managers.DragManager;
import mx.managers.PopUpManager;
import mx.states.AddChild;
import mx.states.State;

public class DropArea extends HBox
{
    private var _analysisItem:AnalysisItem;

    [Bindable]
    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var editButton:Button;
    private var deleteButton:Button;

    private var _analysisItems:ArrayCollection;

    private var _dataSourceID:int;

    public function set dataSourceID(value:int):void {
        _dataSourceID = value;
    }

    public function DropArea()
    {
        super();
        addChild(createNoDataLabel());
        horizontalScrollPolicy = "off";
        this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
        this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
        this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
        this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
        this.addEventListener(KeyboardEvent.KEY_UP, keyPressed);

        editButton = new Button();
        editButton.label = "...";
        editButton.addEventListener(MouseEvent.CLICK, editEvent);
        var configured:State = new State();
        configured.name = "Configured";
        var addChildAction:AddChild = new AddChild();
        addChildAction.relativeTo = this;
        addChildAction.target = editButton;
        var addDeleteButton:AddChild = new AddChild();
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
        deleteButton.toolTip = "Clear This Field";
        addDeleteButton.relativeTo = this;
        addDeleteButton.target = deleteButton;
        configured.overrides = [ addChildAction, addDeleteButton ];
        states = [ configured ];

        this.setStyle("borderStyle", "solid");
        this.setStyle("borderThickness", 1);
        setStyle("verticalAlign", "middle");
        setStyle("backgroundColor", 0xFFFFFF);
        var deleteContextItem:ContextMenuItem = new ContextMenuItem("Delete Field", true);
        deleteContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onDelete);
        PopupMenuFactory.assignMenu(this, [ deleteContextItem ]);
    }

    public function highlight():void {
        //setStyle("borderColor", "0x0000FF");
    }

    public function normal():void {
       // setStyle("borderColor", 0xB7BABC);
    }

    private function createNoDataLabel():UIComponent {
        var label:EmptyDropAreaLabel = new EmptyDropAreaLabel();
        label.text = getNoDataLabel();
        return label;
    }

    private function onDelete(event:Event):void {
        deletion();
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

    private function editEvent(event:MouseEvent):void {
        var analysisItemEditor:AnalysisItemEditWindow = new AnalysisItemEditWindow();
        analysisItemEditor.editorClass = customEditor();
        analysisItemEditor.dataSourceID = _dataSourceID;
        analysisItemEditor.analysisItems = this._analysisItems;
        analysisItemEditor.analysisItem = this.analysisItem;
        PopUpManager.addPopUp(analysisItemEditor, this);
        PopUpUtil.centerPopUp(analysisItemEditor);
        analysisItemEditor.addEventListener(AnalysisItemEditEvent.ANALYSIS_ITEM_EDIT, itemEdited, false, 0, true);
    }

    private function itemEdited(event:AnalysisItemEditEvent):void {
        dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, event.analysisItem)));
    }

    public function createAnalysisItem():AnalysisItem {
        return analysisItem;
    }

    protected function toDefaultState():void {
        removeChildAt(0);
        addChild(createNoDataLabel());
    }

    public function get analysisItem():AnalysisItem {
        return this._analysisItem;
    }

    public function set analysisItem(analysisItem:AnalysisItem):void {
        if (this._analysisItem != null) {
            getChildAt(0).removeEventListener(MouseEvent.MOUSE_DOWN, onMouseDown);
        }
        this._analysisItem = analysisItem;

        removeChildAt(0);
        if (analysisItem == null) {
            addChildAt(createNoDataLabel(), 0);
            currentState = "";
        } else {
            var component:UIComponent = DropAreaFactory.createDropItemElement(this, analysisItem);
            component.addEventListener(MouseEvent.MOUSE_DOWN, onMouseDown);
            addChildAt(component, 0);
            currentState = "Configured";
        }
    }

    protected function getNoDataLabel():String {
        return null;
    }

    protected function dragEnterHandler(event:DragEvent):void {
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
            setStyle("borderColor", "green");
            DragManager.acceptDragDrop(event.currentTarget as IUIComponent);
        }
    }

    protected function accept(analysisItem:AnalysisItem):Boolean {
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

    private function onMouseDown(event:MouseEvent):void {
        var dragSource:DragSource = new DragSource();
        var bd:BitmapData = new BitmapData(this.width, this.height);
        bd.draw(this);
        var bitmap:Bitmap = new Bitmap(bd);
        var image:Image = new Image();
        image.source = bitmap;
        DragManager.doDrag(this, dragSource, event, image);
    }

    protected function dragDropHandler(event:DragEvent):void {
        //setStyle("borderThickness", 0);
        setStyle("borderColor", 0xB7BABC);
        if (event.dragInitiator is DataGrid) {
            var initialList:DataGrid = event.dragInitiator as DataGrid;
            var newAnalysisItem:AnalysisItemWrapper = initialList.selectedItem as AnalysisItemWrapper;
            if (this.analysisItem == null) {
                dispatchEvent(new CommandEvent(new DropAreaAddedCommand(this, newAnalysisItem.analysisItem)));
            } else {
                dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, newAnalysisItem.analysisItem)));
            }
        } else if (event.dragInitiator is DropArea) {
            var dropArea:DropArea = event.dragInitiator as DropArea;
            dispatchEvent(new CommandEvent(new DropAreaSwapCommand(dropArea, this)));
        } else if (event.dragInitiator is AdvancedDataGrid) {
            var analysisItemLabel:AdvancedDataGrid = event.dragInitiator as AdvancedDataGrid;
            newAnalysisItem = analysisItemLabel.selectedItem as AnalysisItemWrapper;
            if (newAnalysisItem.isAnalysisItem()) {
                if (this.analysisItem == null) {
                    dispatchEvent(new CommandEvent(new DropAreaAddedCommand(this, newAnalysisItem.analysisItem)));
                } else {
                    dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, newAnalysisItem.analysisItem)));
                }
            }
        } else if (event.dragInitiator is List) {
            var list:List = event.dragInitiator as List;
            var data:ArrayCollection = ArrayCollection(list.dataProvider);
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
        setStyle("borderColor", 0xB7BABC);
    }
}
}