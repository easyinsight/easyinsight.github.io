package com.easyinsight.administration.feed
{
	public interface IFeedAdminDetail
	{
		function updateFeedDefinition(feedDefinition:FeedDefinitionData):void;
		function validate():Boolean;
	}
}