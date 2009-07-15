package com.easyinsight.salesforce
{
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.SalesforceDataSourceCreation;

[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.salesforce.SalesforceBaseDataSource")]
	public class SalesforceFeedDefinition extends ServerDataSourceDefinition
	{
		public function SalesforceFeedDefinition()
		{
			super();
		    this.feedName = "Salesforce";
        }


    override public function configClass():Class {
        return SalesforceDataSourceCreation;
    }
		
	}
}