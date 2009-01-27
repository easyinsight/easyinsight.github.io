package com.easyinsight.administration.system
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.admin.HealthInfo")]
	public class HealthInfo
	{
		public var currentMemory:int;
		public var freeMemory:int;
		public var threadCount:int;
		public var activeDBConnections:int;
		public var idleDBConnections:int;
		public var activeUsers:int;
		public var majorCollectionCount:int;
		public var minorCollectionCount:int;
		public var majorCollectionTime:int;
		public var minorCollectionTime:int;
		public var systemLoadAverage:Number;
		public var compilationTime:int;
		public var clientCount:int;
		
		public function HealthInfo()
		{
		}
	}
}