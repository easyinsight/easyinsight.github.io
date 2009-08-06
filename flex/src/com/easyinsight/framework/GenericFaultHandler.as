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
        		Alert.show(event.fault.faultDetail, "A server error occurred.");
			
			//var loginDialog:LoginDialog = LoginDialog(PopUpManager.createPopUp(Application.application as DisplayObject, LoginDialog, true));
        	//PopUpUtil.centerPopUp(loginDialog);
		}
		
		private static function closeHandler(event:CloseEvent):void {
			ExternalInterface.call("window.location.reload()");
		}
	}
}