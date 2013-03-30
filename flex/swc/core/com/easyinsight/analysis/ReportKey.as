package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.ReportKey")]
	public class ReportKey extends Key
	{
		public var parentKey:Key;
		public var reportID:int;
		
		public function ReportKey()
		{
			super();
		}
		
		override public function createString():String {
			return parentKey.createString();
		}

        override public function toBaseKey():Key {
            return parentKey.toBaseKey();
        }

        override public function internalString():String {
            return reportID + "-" + parentKey.internalString();
        }
	}
}