package com.easyinsight.customupload {
import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.framework.Credentials;

public interface IServerDataSourceCreation {
    function get dataSourceDefinition():FeedDefinitionData;
    function updateDataSource(feedDefinition:FeedDefinitionData):void;
    function get credentials():Credentials;
}
}