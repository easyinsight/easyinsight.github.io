package com.easyinsight.customupload {
import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.administration.feed.IFeedAdminDetail;

import mx.controls.Button;

public interface IServerDataSourceCreation extends IFeedAdminDetail {
    function set dataSourceDefinition(feedDefinition:FeedDefinitionData):void;
    function get dataSourceDefinition():FeedDefinitionData;
    function setupButton(button:Button):Function;
}
}