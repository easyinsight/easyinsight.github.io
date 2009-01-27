package com.easyinsight.framework
{
	import flash.display.Sprite;
	import flash.external.ExternalInterface;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.events.CloseEvent;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class GenericFaultHandler
	{
		public function GenericFaultHandler()
		{
		}

		public static function genericFault(event:FaultEvent):void {
			var fatal:Boolean = false;
        	if (event.fault.faultString.indexOf("com.easyinsight.security.SecurityException") != -1) {
        		Alert.show("Your session has timed out. You'll need to log in again.", "Session Timeout", 4, Application.application as Sprite, closeHandler);
        		//var channelSet:ChannelSet = ServerConfig.getChannelSet("login");
        		//if (channelSet.authenticated) {
        			// session expired
        			fatal = true;
        		/*} else {
        			
        		}   */         		
        	}
        	if (!fatal) {
        		Alert.show(event.fault.name, "A server error occurred.");
        	}
			
			//var loginDialog:LoginDialog = LoginDialog(PopUpManager.createPopUp(Application.application as DisplayObject, LoginDialog, true));
        	//PopUpManager.centerPopUp(loginDialog);
		}
		
		private static function closeHandler(event:CloseEvent):void {
			ExternalInterface.call("window.location.reload()");
		}
	}
}