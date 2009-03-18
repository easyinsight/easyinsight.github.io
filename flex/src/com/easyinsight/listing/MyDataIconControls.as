package com.easyinsight.listing
{
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.customupload.FileFeedUpdateWindow;
	import com.easyinsight.customupload.RefreshWindow;
	import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.genredata.ModuleAnalyzeEvent;
import com.easyinsight.util.ProgressAlert;

import com.easyinsight.solutions.InsightDescriptor;
import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

	public class MyDataIconControls extends HBox
	{
		private var obj:Object;
		
		[Embed(source="../../../../assets/refresh.png")]
        public var refreshIcon:Class;
                
        [Embed(source="../../../../assets/businessman_edit.png")]
        public var adminIcon:Class;
        
        [Embed(source="../../../../assets/media_play_green.png")]
        public var playIcon:Class;

        [Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
        
        private var refreshButton:Button;
        private var adminButton:Button;
        private var analyzeButton:Button;
        private var deleteButton:Button;
        private var analysisService:RemoteObject;
        
		public function MyDataIconControls()
		{
			super();
			analyzeButton = new Button();
			analyzeButton.setStyle("icon", playIcon);
			analyzeButton.toolTip = "Analyze...";
			analyzeButton.addEventListener(MouseEvent.CLICK, analyzeCalled);
			addChild(analyzeButton);			
			refreshButton = new Button();
			refreshButton.setStyle("icon", refreshIcon);
			refreshButton.toolTip = "Refresh...";
			refreshButton.addEventListener(MouseEvent.CLICK, refreshCalled);
			addChild(refreshButton);
			adminButton = new Button();
			adminButton.setStyle("icon", adminIcon);
			adminButton.toolTip = "Administer...";
			adminButton.addEventListener(MouseEvent.CLICK, adminCalled);
			addChild(adminButton);
			deleteButton = new Button();
            deleteButton.setStyle("icon", deleteIcon);
            deleteButton.addEventListener(MouseEvent.CLICK, deleteCalled);
            addChild(deleteButton);
			
			this.setStyle("paddingLeft", 5);
			this.setStyle("paddingRight", 5);
		}
		
		private function refreshCalled(event:MouseEvent):void {
			if (obj is DataFeedDescriptor) {
				var feedDescriptor:DataFeedDescriptor = obj as DataFeedDescriptor;
				switch (feedDescriptor.feedType) {
					case DataFeedDescriptor.STATIC:
					case DataFeedDescriptor.EMPTY:
						fileData(feedDescriptor);
						break;
					default:
						refreshData(feedDescriptor);
						break;
				}
			}
		}

        private function deleteCalled(event:MouseEvent):void {
            dispatchEvent(new DeleteDataSourceEvent(obj));                        
        }
		
		private function analyzeCalled(event:MouseEvent):void {
			if (obj is DataFeedDescriptor) {
				var descriptor:DataFeedDescriptor = obj as DataFeedDescriptor;
				dispatchEvent(new ModuleAnalyzeEvent(new DescriptorAnalyzeSource(descriptor)));
			} else {
				var analysisDefinition:InsightDescriptor = obj as InsightDescriptor;
                analysisService = new RemoteObject();
                analysisService.destination = "analysisDefinition";
                analysisService.openAnalysisDefinition.addEventListener(ResultEvent.RESULT, gotReport);
                ProgressAlert.alert(this, "Retrieving report...", null, analysisService.openAnalysisDefinition);
                analysisService.openAnalysisDefinition.send(analysisDefinition.insightID);
			}
		}

        private function gotReport(event:ResultEvent):void {
            var analysisDefinition:AnalysisDefinition = analysisService.openAnalysisDefinition.lastResult as AnalysisDefinition;
            dispatchEvent(new ModuleAnalyzeEvent(new AnalysisDefinitionAnalyzeSource(analysisDefinition)));
        }
		
		private function refreshData(feedDescriptor:DataFeedDescriptor):void {
			var refreshWindow:RefreshWindow = RefreshWindow(PopUpManager.createPopUp(this.parent.parent.parent, RefreshWindow, true));
			refreshWindow.feedID = feedDescriptor.dataFeedID;
			PopUpManager.centerPopUp(refreshWindow);
		}
		
		private function fileData(feedDescriptor:DataFeedDescriptor):void {
			var feedUpdateWindow:FileFeedUpdateWindow = FileFeedUpdateWindow(PopUpManager.createPopUp(this.parent.parent.parent, FileFeedUpdateWindow, true));
			feedUpdateWindow.feedID = feedDescriptor.dataFeedID;
			PopUpManager.centerPopUp(feedUpdateWindow);
				
		}
		
		private function adminCalled(event:MouseEvent):void {
			if (obj is DataFeedDescriptor) {
				var descriptor:DataFeedDescriptor = obj as DataFeedDescriptor;
				dispatchEvent(new ModuleAnalyzeEvent(new FeedAdminAnalyzeSource(descriptor.dataFeedID)));
			}
		}
		
		override public function set data(value:Object):void {
			this.obj = value;
			if (value is DataFeedDescriptor) {
				var descriptor:DataFeedDescriptor = value as DataFeedDescriptor;
				refreshButton.setVisible(true);
				if (descriptor.role == DataFeedDescriptor.OWNER) {
					adminButton.setVisible(true);
				} else {
					adminButton.setVisible(false);
				}
			} else {
				refreshButton.setVisible(false);
				adminButton.setVisible(false);
			}			
		}
		
		override public function get data():Object {
			return this.obj;
		}
	}
}