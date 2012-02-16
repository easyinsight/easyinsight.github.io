package com.easyinsight.datasources
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.cloudwatch.CloudWatchDataSource")]
	public class CloudWatchDataSource extends CompositeServerDataSource
	{
        public var cwUserName:String;
        public var cwPassword:String;

		public function CloudWatchDataSource()
		{
			super();
		    this.feedName = "CloudWatch";
        }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return CloudWatchDataSourceCreation;
    }
	}
}