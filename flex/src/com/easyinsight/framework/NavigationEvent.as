package com.easyinsight.framework
{
	import com.easyinsight.listing.IPerspective;
	
	import flash.events.Event;

	public class NavigationEvent extends Event
	{
		public static const NAVIGATION:String = "navigation";
		
		public static const ACCOUNTS:String = "Accounts";
		
		public var targetPage:String;
		public var targetPerspective:IPerspective;
        public var properties:Object;
		
		public function NavigationEvent(targetPage:String, targetPerspective:IPerspective = null, properties:Object = null)
		{
			super(NAVIGATION, true);
			this.targetPage = targetPage;
			this.targetPerspective = targetPerspective;
            this.properties = properties;
		}
		
		override public function clone():Event {
			return new NavigationEvent(this.targetPage, this.targetPerspective, this.properties);
		}
	}
}