package com.easyinsight.customupload {
import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.administration.feed.IFeedAdminDetail;
import com.easyinsight.framework.Credentials;

public interface IServerDataSourceCreation extends IFeedAdminDetail {
    function set dataSourceDefinition(feedDefinition:FeedDefinitionData):void;
    function get dataSourceDefinition():FeedDefinitionData;
    function get credentials():Credentials;
}
}