package com.easyinsight.administration.items
{
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemEditEvent;
	import com.easyinsight.analysis.AnalysisItemEditor;

import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;
	
	import mx.controls.Button;
	import mx.managers.PopUpManager;

	public class AnalysisItemEditorButton extends Button
	{
		private var analysisItem:AnalysisItem;
		
		public function AnalysisItemEditorButton()
		{
			super();
			label = "Edit...";
			addEventListener(MouseEvent.CLICK, editItem);
		}
		
		private function editItem(event:MouseEvent):void {
			var editor:AnalysisItemEditor = AnalysisItemEditor(PopUpManager.createPopUp(this.parent, AnalysisItemEditor, true));
			editor.analysisItem = analysisItem;
			editor.addEventListener(AnalysisItemEditEvent.ANALYSIS_ITEM_EDIT, onEdit);
			PopUpUtil.centerPopUp(editor);
		}
		
		private function onEdit(event:AnalysisItemEditEvent):void {
			parent.dispatchEvent(event);
		}		
		
		override public function set data(object:Object):void {
			this.analysisItem = object as AnalysisItem;
		}
		
		override public function get data():Object {
			return analysisItem;
		}
	}
}