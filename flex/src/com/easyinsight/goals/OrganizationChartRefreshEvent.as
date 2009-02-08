package com.easyinsight.goals
{
	import flash.events.Event;
import ilog.orgchart.OrgChartItem;

	public class OrganizationChartRefreshEvent extends Event
	{
		public static const ORG_CHART_REFRESH:String = "orgChartRefresh";

        public var newNode:OrgChartItem;
		
		public function OrganizationChartRefreshEvent(newNode:OrgChartItem = null)
		{
			super(ORG_CHART_REFRESH, true);
            this.newNode = newNode;
		}
		
		override public function clone():Event {
			return new OrganizationChartRefreshEvent(newNode);
		}
	}
}