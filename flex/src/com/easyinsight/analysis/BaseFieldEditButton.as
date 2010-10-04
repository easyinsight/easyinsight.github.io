package com.easyinsight.analysis
{
import com.easyinsight.AnalysisItemDeleteEvent;

import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.Application;
import mx.core.UIComponent;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class BaseFieldEditButton extends UIComponent implements IListItemRenderer
	{
		private var analysisItemWrapper:AnalysisItemWrapper;
		private var _displayName:String;
		private var _analysisItems:ArrayCollection;
		private var button:Button;
		private var deleteButton:Button;
		private var copyButton:Button;


        public function set analysisItems(value:ArrayCollection):void {
            _analysisItems = value;
        }

        [Bindable]
		[Embed(source="../../../../assets/clipboard.png")]
        public var editIcon:Class;

        [Bindable]
		[Embed(source="../../../../assets/copy.png")]
        public var copyIcon:Class;

        [Bindable]
		[Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;

        private var analysisService:RemoteObject;
        
		public function BaseFieldEditButton()
		{
			super();	
			setStyle("horizontalAlign", "center");
			this.percentWidth = 100;		
		}
		
		override protected function createChildren():void {
			if (button == null) {
				button = new Button();
				button.setStyle("icon", editIcon);
				button.toolTip = "Edit Field";
				//button.addEventListener(MouseEvent.CLICK, editItem);
			}
            addChild(button);
            if (copyButton == null) {
				copyButton = new Button();
				copyButton.setStyle("icon", copyIcon);
				copyButton.toolTip = "Copy...";
                copyButton.addEventListener(MouseEvent.CLICK, copy);
			}
            addChild(copyButton);
            if (deleteButton == null) {
                deleteButton = new Button();
                deleteButton.setStyle("icon", deleteIcon);
                deleteButton.toolTip = "Delete Field";
                deleteButton.addEventListener(MouseEvent.CLICK, deleteItem);
            }
            addChild(deleteButton);
		}

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var buttonWidth:int = 40;
        var buttonHeight:int = 22;
        var padding:int = 5;
        button.move((padding),0);
        button.setActualSize(buttonWidth, buttonHeight);
        copyButton.move((padding * 2) + (buttonWidth),0);
        copyButton.setActualSize(buttonWidth, buttonHeight);
        deleteButton.move((padding * 3) + (buttonWidth * 2),0);
        deleteButton.setActualSize(buttonWidth, buttonHeight);
    }

        private function copy(event:MouseEvent):void {
            event.stopPropagation();
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
            dispatchEvent(new AnalysisItemDeleteEvent(analysisItemWrapper));
        }
		
		private function analysisItemEdited(event:AnalysisItemEditEvent):void {
			this.analysisItemWrapper.analysisItem = event.analysisItem;
			this.analysisItemWrapper.displayName = event.analysisItem.display;
			this.displayName = event.analysisItem.displayName;			 
		}
		
		[Bindable("dataChange")]
		public function set data(value:Object):void {
			this.analysisItemWrapper = value as AnalysisItemWrapper;
			this.displayName = analysisItemWrapper.displayName;
            dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
		}
		
		public function get data():Object {
			return this.analysisItemWrapper;
		}
	}
}