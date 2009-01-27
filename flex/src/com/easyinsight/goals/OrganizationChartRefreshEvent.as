package com.easyinsight.goals
{
	import flash.events.Event;

	public class OrganizationChartRefreshEvent extends Event
	{
		public static const ORG_CHART_REFRESH:String = "orgChartRefresh";
		
		public function OrganizationChartRefreshEvent()
		{
			super(ORG_CHART_REFRESH, true);
		}
		
		override public function clone():Event {
			return new OrganizationChartRefreshEvent();
		}
	}
}