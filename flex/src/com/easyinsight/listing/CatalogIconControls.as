package com.easyinsight.listing
{
	import com.easyinsight.LoginDialog;
	import com.easyinsight.customupload.UploadPolicy;
	import com.easyinsight.framework.User;
	import com.easyinsight.genredata.AnalyzeEvent;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.containers.HBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.messaging.Consumer;
	import mx.messaging.events.MessageEvent;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class CatalogIconControls extends HBox
	{
		[Embed(source="../../../../assets/media_play_green.png")]
        public var playIcon:Class;
        
        [Embed(source="../../../../assets/credit_card.png")]
        public var buyIcon:Class;
        
        [Embed(source="../../../../assets/data_next.png")]
        public var subscribeIcon:Class;
        
        private var playButton:Button;
        private var buyButton:Button;
        private var subscribeButton:Button;
        
        private var buyShowing:Boolean = false;
        private var subscribeShowing:Boolean = false;
        
        private var feedDescriptor:DataFeedDescriptor;
        private var preview:Boolean;
        
        private var remoteService:RemoteObject;
		private var consumer:Consumer;        
        
		public function CatalogIconControls()
		{
			super();
			playButton = new Button();
			playButton.setStyle("icon", playIcon);
			playButton.toolTip = "Analyze";
			//addChild(playButton);
			playButton.addEventListener(MouseEvent.CLICK, analyze);
			buyButton = new Button();
			buyButton.setStyle("icon", buyIcon);
			buyButton.toolTip = "Buy Now!";
			buyButton.addEventListener(MouseEvent.CLICK, buyConfirmationAlert);
			//addChild(buyButton);
			subscribeButton = new Button();
			subscribeButton.setStyle("icon", subscribeIcon);
			subscribeButton.toolTip = "Subscribe";
			subscribeButton.addEventListener(MouseEvent.CLICK, subscribe);
			//addChild(subscribeButton);			
			this.setStyle("paddingLeft", 5);
			this.setStyle("paddingRight", 5);
		}
		
		override public function set data(value:Object):void {
			removeAllChildren();
			addChild(playButton);
			this.feedDescriptor = value as DataFeedDescriptor;
			this.preview = (feedDescriptor.role != DataFeedDescriptor.OWNER && feedDescriptor.role != DataFeedDescriptor.SUBSCRIBER);
			if (feedDescriptor.role != DataFeedDescriptor.OWNER && feedDescriptor.role != DataFeedDescriptor.SUBSCRIBER) {
				var buyable:Boolean = (feedDescriptor.role != DataFeedDescriptor.OWNER && feedDescriptor.role != DataFeedDescriptor.SUBSCRIBER && 
					feedDescriptor.policy.getPolicyType() == UploadPolicy.COMMERCIAL);
				if (buyable) {
					addChild(buyButton);
					buyShowing = true;
					//buyButton.visible = true;
					if (subscribeShowing) {
						subscribeShowing = false;					
						//removeChild(subscribeButton);
					}		
					//subscribeButton.visible = false;		
				} else {								
					addChild(subscribeButton);
					subscribeShowing = true;
					if (buyShowing) {
						buyShowing = false;
						//removeChild(buyButton);
					}
				}
			}					 		
		}
		
		private function buyConfirmationAlert(event:Event):void {
			Alert.show("Are you sure you want to pay for this subscription?", "", 3, this, alertClickHandler);
		}
		
		private function analyze(event:MouseEvent):void {
			dispatchEvent(new AnalyzeEvent(new DescriptorAnalyzeSource(this.feedDescriptor, preview)));
		}
		
		override public function get data():Object {
			return this.feedDescriptor;
		}
		
		public function subscribe(event:Event):void {
			var user:User = User.getInstance();
			if (user == null) {
				var loginContainer:LoginDialog = LoginDialog(PopUpManager.createPopUp(this, LoginDialog, true));
				//loginContainer.addEventListener(LoginEvent.LOGIN, loggedIn);
			} else {
				var remoteService:RemoteObject = new RemoteObject();
				remoteService.destination = "userUpload";
				remoteService.subscribe.addEventListener(ResultEvent.RESULT, successfulSubscription);
				remoteService.subscribe.addEventListener(FaultEvent.FAULT, failedSubscription);
				remoteService.subscribe(feedDescriptor.dataFeedID);
			}
		}
		
		private function alertClickHandler(event:CloseEvent):void {
            if (event.detail==Alert.YES)
                buy();            
        }
		
		public function buy():void {
			var user:User = User.getInstance();
			if (user == null) {
				var loginContainer:LoginDialog = LoginDialog(PopUpManager.createPopUp(this, LoginDialog, true));
				//loginContainer.addEventListener(LoginEvent.LOGIN, loggedIn);
			} else {
				remoteService = new RemoteObject();
				remoteService.destination = "store";
				remoteService.buy.addEventListener(FaultEvent.FAULT, failedSubscription);
				remoteService.buy.send(feedDescriptor.dataFeedID, 1);
				//remoteService.createURL.addEventListener(FaultEvent.FAULT, failedSubscription);
				//remoteService.createURL.addEventListener(ResultEvent.RESULT, gotBillingURL);
				//remoteService.createURL.send();
			}
		}
		
		private function gotBillingURL(event:ResultEvent):void {
			consumer = new Consumer();
			consumer.destination = "orderManagement";
			consumer.subtopic = "x";
			consumer.addEventListener(MessageEvent.MESSAGE, gotBillingMessage);			
			consumer.subscribe();
			var billingURL:String = remoteService.createURL.lastResult as String;
			/*var amazonPaymentWindow:AmazonPaymentWindow = AmazonPaymentWindow(PopUpManager.createPopUp(this, AmazonPaymentWindow, true));
			amazonPaymentWindow.amazonURL = billingURL;*/
			navigateToURL(new URLRequest(billingURL));
		} 
		
		private function gotBillingMessage(event:MessageEvent):void {
			var url:String = event.message.body.tokenID;
			trace(url);
			var params:String = url.split("?")[1];
			var tokenID:String;
			var callerReference:String;
			var paramArray:Array = params.split("&");
			for each (var paramString:String in paramArray) {
				var paramBreakdown:Array = paramString.split("=");
				var paramKey:String = paramBreakdown[0];
				if ("tokenID" == paramKey) {
					tokenID = paramBreakdown[1];
				} else if ("callerReference" == paramKey) {
					callerReference = paramBreakdown[1];
				}
				//var paramValue:String = paramBreakdown[1];
			}						
			//var callerReference:String = event.message.body.callerReference;
			trace(tokenID);
			trace(callerReference);			
			consumer.removeEventListener(MessageEvent.MESSAGE, gotBillingMessage);
			consumer.disconnect();
			remoteService.amazonBuy.send(tokenID, callerReference);
		}
		
		private function bought(event:ResultEvent):void {
			this.label = "Subscribe";
			subscribeButton.visible = true;
			buyButton.visible = false;
		}
		
		private function successfulSubscription(event:ResultEvent):void {
			//_data.subscribable = false;
			//_data.analyzeable = true;
			if (buyShowing) {
				removeChild(buyButton);
				buyShowing = false;
			} else if (subscribeShowing) {
				removeChild(subscribeButton);
				subscribeShowing = false;
			}
			feedDescriptor.role = DataFeedDescriptor.SUBSCRIBER;
		}
		
		private function failedSubscription(event:FaultEvent):void {
			Alert.show(event.fault.message);
		}
	}
}