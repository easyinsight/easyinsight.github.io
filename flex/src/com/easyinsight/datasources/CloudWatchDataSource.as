package com.easyinsight.datasources
{
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.cloudwatch.CloudWatchDataSource")]
	public class CloudWatchDataSource extends ServerDataSourceDefinition
	{
		public function CloudWatchDataSource()
		{
			super();
		    this.feedName = "CloudWatch";
        }


    override public function configClass():Class {
        return CloudWatchDataSourceCreation;
    }

    override public function isLiveData():Boolean {
            return true;
        }
	}
}