package com.easyinsight.analysis
{
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;
	import flash.geom.Point;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
import mx.core.Application;
import mx.managers.PopUpManager;

    import com.easyinsight.administration.feed.DeleteAnalysisItemEvent;

import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class ReportEditorFieldControls extends HBox
	{
		private var analysisItemWrapper:AnalysisItemWrapper;
		private var _displayName:String;
		private var _analysisItems:ArrayCollection;
		private var button:Button;
		private var editButton:Button;
		private var deleteButton:Button;

        private var analysisService:RemoteObject;

		[Bindable]
		[Embed(source="../../../../assets/copy.png")]
        public var copyIcon:Class;

        [Bindable]
		[Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;

        [Bindable]
		[Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;

    private var _showDelete:Boolean;


    [Bindable(event="showDeleteChanged")]
    public function get showDelete():Boolean {
        return _showDelete;
    }

    public function set showDelete(value:Boolean):void {
        if (_showDelete == value) return;
        _showDelete = value;
        dispatchEvent(new Event("showDeleteChanged"));
    }

    private var _showEdit:Boolean;


    [Bindable(event="showEditChanged")]
    public function get showEdit():Boolean {
        return _showEdit;
    }

    public function set showEdit(value:Boolean):void {
        if (_showEdit == value) return;
        _showEdit = value;
        dispatchEvent(new Event("showEditChanged"));
    }

    public function ReportEditorFieldControls()
		{
			super();	
			setStyle("horizontalAlign", "center");
			this.percentWidth = 100;		
		}
		
		override protected function createChildren():void {
			if (button == null) {
				button = new Button();
				button.setStyle("icon", copyIcon);
				button.toolTip = "Copy...";
                button.addEventListener(MouseEvent.CLICK, copy);
			}
            addChild(button);
            if (editButton == null) {
				editButton = new Button();
				editButton.setStyle("icon", editIcon);
				editButton.toolTip = "Edit...";
                BindingUtils.bindProperty(editButton, "visible", this, "showEdit");
                editButton.addEventListener(MouseEvent.CLICK, editItem);
			}
            addChild(editButton);
            if (deleteButton == null) {
                deleteButton = new Button();
                deleteButton.setStyle("icon", deleteIcon);
                BindingUtils.bindProperty(deleteButton, "visible", this, "showDelete");
                deleteButton.toolTip = "Delete Field";

                deleteButton.addEventListener(MouseEvent.CLICK, deleteItem);
            }
            addChild(deleteButton);
		}

        private function copy(event:MouseEvent):void {
            analysisService = new RemoteObject();
            analysisService.destination = "analysisDefinition";
            analysisService.cloneItem.addEventListener(ResultEvent.RESULT, onCopy);
            analysisService.cloneItem.send(analysisItemWrapper.analysisItem);
        }

    private function onCopy(event:ResultEvent):void {
        var copyItem:AnalysisItem = analysisService.cloneItem.lastResult as AnalysisItem;
        dispatchEvent(new AnalysisItemCopyEvent(copyItem));
    }
		
		[Bindable]
		public function get displayName():String {
			return _displayName;
		}
		
		public function set displayName(displayName:String):void {
			this._displayName = displayName;
		}

        private function deleteItem(event:MouseEvent):void {
            dispatchEvent(new DeleteAnalysisItemEvent(analysisItemWrapper));
        }

        public function set analysisItems(value:ArrayCollection):void {
            _analysisItems = value;
        }

        private function editItem(event:MouseEvent):void {
			var analysisItemEditor:AnalysisItemEditWindow = new AnalysisItemEditWindow();
			analysisItemEditor.editorClass = AnalysisItemEditor; 			
			analysisItemEditor.analysisItem = analysisItemWrapper.analysisItem;
			analysisItemEditor.analysisItems = this._analysisItems;
			analysisItemEditor.addEventListener(AnalysisItemEditEvent.ANALYSIS_ITEM_EDIT, analysisItemEdited);
			PopUpManager.addPopUp(analysisItemEditor, this.parent);
			PopUpUtil.centerPopUp(analysisItemEditor);
        }
		
		private function analysisItemEdited(event:AnalysisItemEditEvent):void {
			this.analysisItemWrapper.analysisItem = event.analysisItem;
			this.analysisItemWrapper.displayName = event.analysisItem.displayName;
			this.displayName = event.analysisItem.displayName;			 
		}
		
		[Bindable]
		override public function set data(value:Object):void {
			this.analysisItemWrapper = value as AnalysisItemWrapper;
			this.displayName = analysisItemWrapper.displayName;
            if (analysisItemWrapper.analysisItem != null) {
                if (analysisItemWrapper.analysisItem.concrete) {
                    showEdit = false;
                    showDelete = false;
                } else {
                    showEdit = true;
                    showDelete = true;
                }
            }
		}
		
		override public function get data():Object {
			return this.analysisItemWrapper;
		}
	}
}