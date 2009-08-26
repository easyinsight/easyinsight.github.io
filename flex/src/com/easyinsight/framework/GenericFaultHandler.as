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
            Alert.show(event.fault.faultDetail, event.fault.faultString);			
		}
	}
}