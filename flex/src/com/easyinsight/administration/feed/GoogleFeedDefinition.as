package com.easyinsight.administration.feed
{
import com.easyinsight.customupload.GoogleSpreadsheetSourceCreation;

[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.google.GoogleFeedDefinition")]
	public class GoogleFeedDefinition extends ServerDataSourceDefinition
	{
		public var worksheetURL:String;
        public var tokenKey:String;
        public var tokenSecret:String;
        public var pin:String;
		
		public function GoogleFeedDefinition()
		{
			super();
		}

        override public function configClass():Class {
            return GoogleSpreadsheetSourceCreation;
        }
    }
}