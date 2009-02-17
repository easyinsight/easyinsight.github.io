package com.easyinsight.datasources {
import com.easyinsight.administration.feed.FeedDefinitionData;
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.jira.JiraDataSource")]
public class JiraDataSource extends FeedDefinitionData{

    public var url:String;

    public function JiraDataSource() {
        super();
        this.feedName = "JIRA";
    }
}
}