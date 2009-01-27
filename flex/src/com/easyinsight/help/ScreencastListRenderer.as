package com.easyinsight.help
{
	import flash.events.MouseEvent;
	
	import mx.controls.LinkButton;
	import mx.managers.PopUpManager;

	public class ScreencastListRenderer extends LinkButton
	{
		private var screencast:Screencast;
		
		public function ScreencastListRenderer()
		{
			super();
			setStyle("textDecoration", "underline");
			setStyle("fontSize", 14);
			addEventListener(MouseEvent.CLICK, showMovie);
		}
		
		private function showMovie(event:MouseEvent):void {
			var screencastWindow:ScreencastWindow = ScreencastWindow(PopUpManager.createPopUp(this.parent.parent, ScreencastWindow, false));
			screencastWindow.screencast = screencast;
			PopUpManager.centerPopUp(screencastWindow);
		}
		
		override public function set data(value:Object):void {
			this.screencast = value as Screencast;
			this.label = screencast.name;			
		}
		
		override public function get data():Object {
			return this.screencast;
		}
	}
}