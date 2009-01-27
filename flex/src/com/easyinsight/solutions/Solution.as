package com.easyinsight.solutions
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.solutions.Solution")]
	public class Solution
	{
		public var solutionID:int;
		public var name:String;
		public var description:String;
		public var author:String = "Easy Insight";
		public var industry:String;
		public var copyData:Boolean;
		public var solutionArchiveName:String;
        public var goalTreeID:int;
        public var installable:Boolean;
		
		public function Solution()
		{
		}

	}
}