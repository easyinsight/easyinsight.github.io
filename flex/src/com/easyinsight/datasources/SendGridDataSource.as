package com.easyinsight.datasources
{
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.sendgrid.SendGridDataSource")]
	public class SendGridDataSource extends ServerDataSourceDefinition
	{
        public var sgUserName:String;
        public var sgPassword:String;

		public function SendGridDataSource()
		{
			super();
		    this.feedName = "SendGrid";
        }


    override public function configClass():Class {
        return SendGridDataSourceCreation;
    }

    override public function isLiveData():Boolean {
            return true;
        }
	}
}