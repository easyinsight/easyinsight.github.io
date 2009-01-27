package com.easyinsight.analysis
{
import com.easyinsight.commands.CommandEvent;
	[Event(name="itemDrop", type="com.easyinsight.analysis.AnalysisListUpdateEvent")]
	import com.easyinsight.commands.CommandEvent;
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.ui.Keyboard;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.DataGrid;
	import mx.controls.List;
	import mx.events.DragEvent;
	import mx.managers.DragManager;
	import mx.managers.PopUpManager;
	
	public class DropArea extends HBox
	{		
		private var _analysisItem:AnalysisItem;
		
		[Bindable]
		private var hackCollection:ArrayCollection = new ArrayCollection();
		
		private var hackList:List;
		
		private var editButton:Button;
		
		private var _analysisItems:ArrayCollection;				
		 		
		public function DropArea()
		{
			super();
			hackList = new List();
			hackList.selectable = false;
			hackList.rowCount = 1;
			hackList.dataProvider = hackCollection;			
			var labelText:String = getNoDataLabel();			
			hackCollection.addItem(labelText);
			addChild(hackList);
			horizontalScrollPolicy = "off";
			this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
			this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
			this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
			this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
			this.addEventListener(KeyboardEvent.KEY_UP, keyPressed);						
			editButton = new Button();
			editButton.label = "...";
			editButton.addEventListener(MouseEvent.CLICK, editEvent);
			editButton.visible = false;
			addChild(editButton);
			this.setStyle("borderStyle", "solid");
			this.setStyle("borderThickness", 1);					
		}
		
		override protected function measure():void {
			super.measure();
			if (editButton.visible) {
				measuredWidth = hackList.measuredWidth + editButton.measuredWidth + hackList.viewMetrics.left + hackList.viewMetrics.right + 8;
			} else {
				measuredWidth = hackList.measuredWidth + hackList.viewMetrics.left + hackList.viewMetrics.right;
			}
		}
		
		public function getItemEditorClass():Class {
			return AnalysisItemEditor;
		}		
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			this._analysisItems = analysisItems;
		}
		
		private function editEvent(event:MouseEvent):void {
			var analysisItemEditor:AnalysisItemEditWindow = new AnalysisItemEditWindow();
			analysisItemEditor.analysisItem = this.analysisItem;
			analysisItemEditor.analysisItems = this._analysisItems;
			analysisItemEditor.editorClass = getItemEditorClass();
			PopUpManager.addPopUp(analysisItemEditor, this);
			var point:Point = new Point();
			point.x = 0;
			point.y = 0;
			point = this.localToGlobal(point);
			analysisItemEditor.x = point.x + 25;
			analysisItemEditor.y = point.y + 25;
			analysisItemEditor.analysisItems = this._analysisItems;
			analysisItemEditor.analysisItem = this.analysisItem;
			analysisItemEditor.addEventListener(AnalysisItemEditEvent.ANALYSIS_ITEM_EDIT, itemEdited);
		}
		
		private function itemEdited(event:AnalysisItemEditEvent):void {
			//this.analysisItem = event.analysisItem;
            dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, event.analysisItem)));
			//dispatchEvent(new DropAreaUpdateEvent(analysisItem, analysisItem));
		}
		
		public function createAnalysisItem():AnalysisItem {
			return analysisItem;
		}
		
		private function sendUpdateEvent():void {
			dispatchEvent(new DropAreaUpdateEvent(analysisItem));
		}		
		
		protected function toDefaultState():void {
			removeChildAt(0);
			addChildAt(hackList, 0);
		}
		
		protected function getHackList():List {
			return hackList;
		}
		
		public function get analysisItem():AnalysisItem {
			return this._analysisItem;
		}
		
		public function set analysisItem(analysisItem:AnalysisItem):void {
			this._analysisItem = analysisItem;

			removeChild(hackList);
			hackList = new List();
			hackList.selectable = false;
			hackList.rowCount = 1;			
			hackCollection.removeAll();
			var labelText:String;
			if (analysisItem == null) {
				labelText = getNoDataLabel();
			} else {
				labelText = analysisItem.display;
			}
			
			hackCollection.addItem(labelText);
			hackList.dataProvider = hackCollection;		
			addChildAt(hackList, 0);
			
			hackList.selectable = (analysisItem != null);
			
			editButton.visible = (analysisItem != null);					
		}
		
		protected function getNoDataLabel():String {
			return null;
		}
		
		protected function dragEnterHandler(event:DragEvent):void {
			if (event.dragInitiator is DataGrid) {
				var initialList:DataGrid = DataGrid(event.dragInitiator);	
				var targetCanvas:DropArea = DropArea(event.currentTarget);
				var selectedObject:AnalysisItemWrapper = AnalysisItemWrapper(initialList.selectedItem);
				setStyle("borderColor", "green");
				DragManager.acceptDragDrop(targetCanvas);
			}	
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
		
		protected function dragDropHandler(event:DragEvent):void {
			//setStyle("borderThickness", 0);			
			setStyle("borderColor", 0xB7BABC);
			var initialList:DataGrid = DataGrid(event.dragInitiator);			
			var newAnalysisItem:AnalysisItemWrapper = AnalysisItemWrapper(initialList.selectedItem);
			if (this.analysisItem == null) {
				dispatchEvent(new CommandEvent(new DropAreaAddedCommand(this, newAnalysisItem.analysisItem)));
			} else {
				dispatchEvent(new CommandEvent(new DropAreaDragUpdateCommand(this, this.analysisItem, newAnalysisItem.analysisItem)));
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