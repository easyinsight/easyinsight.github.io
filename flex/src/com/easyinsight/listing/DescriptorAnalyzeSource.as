package com.easyinsight.listing
{
	import com.easyinsight.DataAnalysisContainer;
	import com.easyinsight.FullScreenPage;
	import com.easyinsight.customupload.UploadPolicy;
	import com.easyinsight.framework.DataService;
	import com.easyinsight.framework.LoginEvent;
	import com.easyinsight.framework.User;
	
	import mx.managers.BrowserManager;
	import mx.rpc.remoting.RemoteObject;
	
	public class DescriptorAnalyzeSource implements AnalyzeSource
	{
		private var dataFeedDescriptor:DataFeedDescriptor;
		private var feedService:RemoteObject;
		private var preview:Boolean;
		private var loginRequired:Boolean;
		
		public function DescriptorAnalyzeSource(descriptor:DataFeedDescriptor, preview:Boolean = false)
		{
			this.dataFeedDescriptor = descriptor;
			if (descriptor.policy.publiclyVisible) {
				// all is happy...
			} else {
				var user:User = User.getInstance();
				if (user == null) {
					// must log in and authorize access
					loginRequired = true;
				} else {
					// we're all good if it authorized...
				}
			}
		}
		
		private function loggedIn(event:LoginEvent):void {
			loginRequired = false;
		}

		public function createAnalysisPopup():FullScreenPage {
			//this.parentObject = parent;
			if (loginRequired) {
				//var loginDialog:LoginDialog = LoginDialog(PopUpManager.createPopUp(parent, LoginDialog, true));
				//loginDialog.addEventListener(LoginEvent.LOGIN, loggedIn);
				//PopUpManager.centerPopUp(loginDialog);
				return null;	
			} else {
				//var canvas:MDICanvas = parent as MDICanvas;
				var dataService:DataService = new DataService();
				dataService.dataFeedID = dataFeedDescriptor.dataFeedID;										
				var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();												
				dataAnalysisContainer.dataService = dataService;
				//dataAnalysisContainer.title = "New Insight";
				dataAnalysisContainer.previewMode = preview;
				
				var feedService:RemoteObject = new RemoteObject();
				feedService.destination = "feeds";
				feedService.addView.send(dataFeedDescriptor.dataFeedID);
				
				BrowserManager.getInstance().setTitle("Easy Insight - " + dataFeedDescriptor.name);
				
				return dataAnalysisContainer;
			}										
		}		
	}
}