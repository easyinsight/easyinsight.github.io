package com.easyinsight.administration.feed
{
	public interface IFeedAdminDetail
	{
		function updateDataSource(feedDefinition:FeedDefinitionData):void;
		function validate():Boolean;
	}
}