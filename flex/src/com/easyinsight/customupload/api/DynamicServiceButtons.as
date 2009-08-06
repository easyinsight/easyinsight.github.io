package com.easyinsight.customupload.api
{
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.managers.PopUpManager;
	import mx.rpc.remoting.RemoteObject;

	public class DynamicServiceButtons extends HBox
	{
		[Embed(source="../../../../../assets/application_enterprise_information.png")]
        public var playIcon:Class;

        [Embed(source="../../../../../assets/pencil.png")]
        public var editIcon:Class;

        [Embed(source="../../../../../assets/application_enterprise_delete.png")]
        public var closeIcon:Class;
        
        [Embed(source="../../../../../assets/refresh.png")]
        public var refreshIcon:Class;
        
        private var dynamicServiceDefinition:DynamicServiceDescriptor;
		
		public function DynamicServiceButtons()
		{
			super();
			this.setStyle("paddingLeft", 5);
			this.setStyle("paddingRight", 5);
			var propertiesButton:Button = new Button();
			propertiesButton.setStyle("icon", playIcon);
			propertiesButton.toolTip = "Properties";
			propertiesButton.addEventListener(MouseEvent.CLICK, propertiesClick);
			addChild(propertiesButton);
			var refreshButton:Button = new Button();
			refreshButton.setStyle("icon", editIcon);
			refreshButton.toolTip = "Edit";
			refreshButton.addEventListener(MouseEvent.CLICK, redeploy);
			addChild(refreshButton);
			var deleteButton:Button = new Button();
			deleteButton.setStyle("icon", closeIcon);
			deleteButton.toolTip = "Delete";
			deleteButton.addEventListener(MouseEvent.CLICK, deleteService);
			addChild(deleteButton);
		}
		
		private function redeploy(event:MouseEvent):void {
			/*var remoteObject:RemoteObject = new RemoteObject();
			remoteObject.destination = "apiService";
			remoteObject.deployService.send(dynamicServiceDefinition.feedID);*/
            var window:DynamicServiceEditWindow = new DynamicServiceEditWindow();
            window.dynamicServiceDefinition = dynamicServiceDefinition.dynamicServiceDefinition;
            window.feedID = dynamicServiceDefinition.feedID;
            PopUpManager.addPopUp(window, this.parent, true);
            PopUpUtil.centerPopUp(window);
		}
		
		override public function set data(value:Object):void {
			this.dynamicServiceDefinition = value as DynamicServiceDescriptor;			
		}
		
		private function deleteService(event:MouseEvent):void {
			var remoteObject:RemoteObject = new RemoteObject();
			remoteObject.destination = "apiService";
			remoteObject.undeployService.send(dynamicServiceDefinition.feedID);
			dispatchEvent(new ServiceDefinitionEvent(ServiceDefinitionEvent.SERVICE_REMOVED, null));
		}
		
		override public function get data():Object {
			return this.dynamicServiceDefinition;
		}
		
		private function propertiesClick(event:MouseEvent):void {
			var serviceWindow:ServiceProperties = ServiceProperties(PopUpManager.createPopUp(this.parent, ServiceProperties));
			serviceWindow.serviceDescriptor = dynamicServiceDefinition;
			PopUpUtil.centerPopUp(serviceWindow);
		}
	}
}