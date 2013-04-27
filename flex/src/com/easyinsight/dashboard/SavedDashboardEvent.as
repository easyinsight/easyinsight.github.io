package com.easyinsight.dashboard
{

import flash.events.Event;

public class SavedDashboardEvent extends Event
	{
		public static const SAVED_DASHBOARD:String = "dashboardSaveAsDone";
		public var dashboard:Dashboard;
		
		public function SavedDashboardEvent(dashboard:Dashboard)
		{
			super(SAVED_DASHBOARD);
			this.dashboard = dashboard;
		}
		
		override public function clone():Event {
			return new SavedDashboardEvent(dashboard);
		}
	}
}