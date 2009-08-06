package com.easyinsight.solutions
{
import com.easyinsight.genredata.AnalyzeEvent;

	import flash.events.MouseEvent;

	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.rpc.remoting.RemoteObject;

	public class AvailableSolutionIcons extends HBox
	{
		private var solution:Solution;
		
        [Embed(source="../../../../assets/component_blue_add.png")]
        private var installIcon:Class;

        private var installButton:Button;
        private var solutionService:RemoteObject;


		public function AvailableSolutionIcons()
		{
			super();
			installButton = new Button();
			installButton.setStyle("icon", installIcon);
			installButton.toolTip = "View Solution";
			installButton.addEventListener(MouseEvent.CLICK, installCalled);
			addChild(installButton);
            /*archiveButton = new Button();
			archiveButton.setStyle("icon", archiveIcon);
			archiveButton.toolTip = "Download Solution Files";
			archiveButton.addEventListener(MouseEvent.CLICK, archiveCalled);
			addChild(archiveButton);*/
			setStyle("horizontalAlign", "center");
            this.percentWidth = 100;
		}
		
		private function installCalled(event:MouseEvent):void {
            dispatchEvent(new SolutionSelectionEvent(solution));
			/*var window:SolutionInstallationWindow = SolutionInstallationWindow(PopUpManager.createPopUp(this.parent, SolutionInstallationWindow, true));
			window.solution = this.solution;
			PopUpUtil.centerPopUp(window);*/
		}

		override public function set data(value:Object):void {
			this.solution = value as Solution;
            //installButton.visible = solution.installable;
		}
		
		override public function get data():Object {
			return this.solution;
		}		
	}
}