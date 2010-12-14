package com.easyinsight.administration.feed
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.google.GoogleFeedDefinition")]
	public class GoogleFeedDefinition extends ServerDataSourceDefinition
	{
		public var worksheetURL:String;
        public var tokenKey:String;
        public var tokenSecret:String;
		
		public function GoogleFeedDefinition()
		{
			super();
		}

        /*override public function isLiveData():Boolean {
            return true;
        }*/
	}
}