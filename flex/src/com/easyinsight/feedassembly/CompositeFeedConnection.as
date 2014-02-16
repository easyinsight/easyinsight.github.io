package com.easyinsight.feedassembly
{
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.Key;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.CompositeFeedConnection")]
	public class CompositeFeedConnection
	{
        public static const ONE:int = 0;
        public static const MANY:int = 1;

		public var sourceJoin:Key;
		public var targetJoin:Key;
        public var sourceReportID:int;
        public var targetReportID:int;
		public var sourceFeedID:int;
		public var targetFeedID:int;
        public var sourceItem:AnalysisItem;
        public var targetItem:AnalysisItem;
        public var sourceFeedName:String;
		public var targetFeedName:String;
        public var sourceOuterJoin:Boolean;
        public var targetOuterJoin:Boolean;
        public var sourceJoinOnOriginal:Boolean;
        public var targetJoinOnOriginal:Boolean;
        public var sourceCardinality:int = ONE;
        public var targetCardinality:int = ONE;
        public var forceOuterJoin:int;
        public var marmotScript:String;
        public var selected:Boolean;

		public function CompositeFeedConnection()
			{
			super();
		}

        public function get display():String {
            return sourceFeedName + " " + sourceDisplay + " to " + targetFeedName + " " + targetDisplay;
        }

        public function get sourceDisplay():String {
            if (sourceItem != null) {
                return sourceItem.unqualifiedDisplay;
            }
            return sourceJoin.createString();
        }

        public function get targetDisplay():String {
            if (targetItem != null) {
                return targetItem.unqualifiedDisplay;
            }
            return targetJoin.createString();
        }
	}
}