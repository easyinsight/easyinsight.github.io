package com.easyinsight.framework
{
	import com.easyinsight.listing.IPerspective;
	
	import flash.events.Event;

	public class NavigationEvent extends Event
	{
		public static const NAVIGATION:String = "navigation";
		
		public static const ACCOUNTS:String = "My Account";
		
		public var targetPage:String;
		public var targetPerspective:IPerspective;
		
		public function NavigationEvent(targetPage:String, targetPerspective:IPerspective = null)
		{
			super(NAVIGATION, true);
			this.targetPage = targetPage;
			this.targetPerspective = targetPerspective;
		}
		
		override public function clone():Event {
			return new NavigationEvent(this.targetPage, this.targetPerspective);
		}
	}
}