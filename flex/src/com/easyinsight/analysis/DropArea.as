package com.easyinsight.analysis
{
import com.easyinsight.commands.CommandEvent;

import com.easyinsight.util.PopUpUtil;

import flash.display.Bitmap;
import flash.display.BitmapData;
import flash.events.ContextMenuEvent;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.geom.Point;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;
import flash.ui.Keyboard;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.AdvancedDataGrid;
import mx.controls.Button;
import mx.controls.DataGrid;
import mx.controls.Image;
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

    private var _assigned:Boolean;
    private var _selected:Boolean;

    private var coreComponent:UIComponent;

    private var editButton:Button;

    private var _analysisItems:ArrayCollection;

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
        configured.overrides = [ addChildAction ];
        states = [ configured ];

        this.setStyle("borderStyle", "solid");
        this.setStyle("borderThickness", 1);
        setStyle("verticalAlign", "middle");
        setStyle("backgroundColor", 0xFFFFFF);
        var deleteContextItem:ContextMenuItem = new ContextMenuItem("Delete Field", true);
        deleteContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onDelete);
        //dataSet.addEventListener(KeyboardEvent.KEY_UP, keyboardHandler);
        PopupMenuFactory.assignMenu(this, [ deleteContextItem ]);
        /*contextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        contextMenu.customItems = [ deleteContextItem ];*/
    }

    private function createNoDataLabel():UIComponent {
        var label:EmptyDropAreaLabel = new EmptyDropAreaLabel();
        label.text = getNoDataLabel();
        return label;
    }

    private function onDelete(event:ContextMenuEvent):void {
        deletion();
    }

    public function getItemEditorClass():Class {
        return AnalysisItemEditor;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        this._analysisItems = analysisItems;
    }

    protected function supportsDrilldown():Boolean {                         
        return true;
    }

    private function editEvent(event:MouseEvent):void {
        var analysisItemEditor:AnalysisItemEditWindow = new AnalysisItemEditWindow();
        if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY) && supportsDrilldown()) {
            analysisItemEditor.editorClass = HierarchyWindow;
        } else if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
            analysisItemEditor.editorClass = CalculationWindow;
        } else {
            analysisItemEditor.editorClass = getItemEditorClass();
        }
        analysisItemEditor.analysisItems = this._analysisItems;
        analysisItemEditor.analysisItem = this.analysisItem;
        PopUpManager.addPopUp(analysisItemEditor, this);
        PopUpUtil.centerPopUp(analysisItemEditor);
        analysisItemEditor.addEventListener(AnalysisItemEditEvent.ANALYSIS_ITEM_EDIT, itemEdited);
    }

    private function itemEdited(event:AnalysisItemEditEvent):void {
        dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, event.analysisItem)));
    }

    public function createAnalysisItem():AnalysisItem {
        return analysisItem;
    }

    private function sendUpdateEvent():void {
        dispatchEvent(new DropAreaUpdateEvent(analysisItem));
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
            var dropArea:DropArea = event.dragInitiator as DropArea;
            analysisItem = dropArea.analysisItem;
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