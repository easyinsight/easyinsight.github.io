package com.easyinsight.listing
{
	import com.easyinsight.administration.analysis.AnalysisAdministrationWindow;
	import com.easyinsight.analysis.AnalysisDefinition;
	
	import flash.events.MouseEvent;
	
	import mx.controls.Button;
	import mx.managers.PopUpManager;

	public class AdministerAnalysisButton extends Button
	{
		private var analysis:AnalysisDefinition;
		
		public function AdministerAnalysisButton()
		{
			super();
			this.label = "Administer";
			addEventListener(MouseEvent.CLICK, administer);			
		}
		
		private function administer(event:MouseEvent):void {
			var window:AnalysisAdministrationWindow = AnalysisAdministrationWindow(PopUpManager.
                createPopUp(this.parent, AnalysisAdministrationWindow, true));
            window.analysisDefinition = analysis;
            PopUpManager.centerPopUp(window);
		}
		
		override public function get data():Object {
			return analysis;
		}
		
		override public function set data(object:Object):void {
			this.analysis = object as AnalysisDefinition;
		}
	}
}