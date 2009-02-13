package com.easyinsight.analysis.scrub
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.scrubbing.LookupTablePair")]
	public class LookupTablePair
	{
		public var pairID:int;
		public var sourceValue:String;
		public var replaceValue:String;
        private var tabIndex:int;
		
		public function LookupTablePair()
		{
		}

        // using hack getter/setter here to avoid sending tabIndex over blazeds, flex specific transient property

        public function setTabIndex(index:int):void {
            this.tabIndex = index;
        }

        public function getTabIndex():int {
            return this.tabIndex;
        }
	}
}