package com.easyinsight.groups
{
	import com.easyinsight.LoginDialog;
	import com.easyinsight.framework.LoginEvent;
import com.easyinsight.framework.NavigationEvent;
import com.easyinsight.framework.User;

import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;
	import flash.events.EventDispatcher;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class DelayedGroupLink extends EventDispatcher
	{
		private var groupID:int;
		private var groupService:RemoteObject;
		
		public function DelayedGroupLink(groupID:int)
		{
			this.groupID = groupID;
			this.groupService = new RemoteObject();
			groupService.destination = "groupService";
			groupService.openGroupIfPossible.addEventListener(ResultEvent.RESULT, gotFeed);
			groupService.openGroupIfPossible.addEventListener(FaultEvent.FAULT, fault);
		}
		
		private function fault(event:FaultEvent):void {
			Alert.show(event.fault.message);
		}
		
		public function execute():void {
			groupService.openGroupIfPossible.send(groupID);
		}

		private function gotFeed(event:ResultEvent):void {
        	var groupResponse:GroupResponse = groupService.openGroupIfPossible.lastResult as GroupResponse;
        	if (groupResponse.status == GroupResponse.SUCCESS) {
                var groupDetail:GroupDetail = new GroupDetail();
                groupDetail.groupID = groupID;
                User.getEventNotifier().dispatchEvent(new NavigationEvent(null, groupDetail));
            } else if (groupResponse.status == GroupResponse.NEED_LOGIN) {
                var loginDialog:LoginDialog = LoginDialog(PopUpManager.createPopUp(Application.application as DisplayObject, LoginDialog, true));
        		loginDialog.addEventListener(LoginEvent.LOGIN, delayedFeed);
                PopUpUtil.centerPopUp(loginDialog);
        	} else {
        		// tried to access a data source they don't have rights to, silently fail
        	}        	            
        }  
        
        private function delayedFeed(event:LoginEvent):void {
        	groupService.openGroupIfPossible.send(groupID);
        }
	}
}