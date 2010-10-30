package com.easyinsight.listing
{	
	import com.easyinsight.LoginDialog;
	import com.easyinsight.customupload.UploadPolicy;
	import com.easyinsight.framework.User;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.messaging.Consumer;
	import mx.messaging.events.MessageEvent;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class SubscribeButton extends Button
	{
		private var _data:DataFeedDescriptor;
		private var remoteService:RemoteObject;
		private var consumer:Consumer;
		
		public function SubscribeButton()
		{
			label = "Subscribe";
			
		}

		override public function set data(value:Object):void {			
			var descriptor:DataFeedDescriptor = value as DataFeedDescriptor;
			_data = descriptor;
			var buyable:Boolean = (descriptor.role != DataFeedDescriptor.OWNER && descriptor.role != DataFeedDescriptor.SUBSCRIBER && 
				descriptor.policy.getPolicyType() == UploadPolicy.COMMERCIAL);
			if (buyable) {
				label = "Buy Now";
				addEventListener(MouseEvent.CLICK, buyConfirmationAlert);
			} else {
				addEventListener(MouseEvent.CLICK, subscribe);
			}			
			if (descriptor.role != DataFeedDescriptor.OWNER && descriptor.role != DataFeedDescriptor.SUBSCRIBER) {
				enabled = true;
			} else {
				enabled = false;
			}
		}
		
		private function buyConfirmationAlert(event:Event):void {
			Alert.show("Are you sure you want to pay for this subscription?", "", 3, this, alertClickHandler);
		}
		
		override public function get data():Object {
			return _data;
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
				remoteService.subscribe(_data.id);
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
				remoteService.buy.send(_data.id, 1);
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
			this.enabled = false;
		}
		
		private function successfulSubscription(event:ResultEvent):void {
			//_data.subscribable = false;
			//_data.analyzeable = true;
			_data.role = DataFeedDescriptor.SUBSCRIBER;
		}
		
		private function failedSubscription(event:FaultEvent):void {
			Alert.show(event.fault.message);
		}
	}
}