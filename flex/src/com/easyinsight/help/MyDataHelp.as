package com.easyinsight.help
{
	import mx.collections.ArrayCollection;
	import mx.containers.ApplicationControlBar;
	import mx.containers.VBox;
	import mx.controls.Label;
	import mx.controls.List;
	import mx.controls.Text;
	import mx.controls.TextArea;
	
	public class MyDataHelp extends VBox
	{
		private var textArea:Text;
		private var screencastBar:ApplicationControlBar;
		private var screencastList:List;
		private var detail:TextArea;
		
		public function MyDataHelp()
		{
			this.percentWidth = 100;
			this.percentHeight = 100;
		}
		
		override protected function createChildren():void {
			super.createChildren();
			if (textArea == null) {
				textArea = new Text();
				textArea.percentWidth = 100;
				
				textArea.setStyle("fontSize", 12);
				textArea.setStyle("borderThickness", 0);
				textArea.setStyle("borderStyle", "off");
				textArea.text = getSummary();				
			}
			addChild(textArea);
			if (screencasts.length > 0) {
				if (screencastBar == null) {					
					screencastBar = new ApplicationControlBar();
					screencastBar.setStyle("horizontalAlign", "center");
					var screencastLabel:Label = new Label();
					screencastLabel.setStyle("fontSize", 14);
					screencastLabel.text = "Screencasts";
					screencastBar.addChild(screencastLabel);					
				}
				addChild(screencastBar);
				if (screencastList == null) {
					screencastList = new List();
					screencastList.percentWidth = 100;
					screencastList.dataProvider = screencasts;
					screencastList.rowCount = screencasts.length;							
				}
				addChild(screencastList);
			}
			if (detail == null) {
				detail = new TextArea();
				detail.percentWidth = 100;
				detail.text = getDetail();
				detail.setStyle("borderThickness", 0);
				detail.setStyle("borderStyle", "off"); 
			}
			addChild(detail);
		}
		
		protected function getSummary():String {
			return "The My Data page is your location for data you've uploaded and insights you've created. From this page, you can:\n" + 
					"\tUpload Excel spreadsheets and delimited text files\n" + 
					"\tPull in data from Google Spreadsheets, Wesabe, and Salesforce.com\n" + 
					"\tJoin feeds together into a new single feed\n" + 
					"\tManage the APIs to your feeds\n";
		}
		
		private function get screencasts():ArrayCollection {
			return new ArrayCollection(createScreencastArray());
		}
		
		protected function createScreencastArray():Array {
			return [  ];
		}
		
		protected function getDetail():String {
			return "";
		}
	}
}