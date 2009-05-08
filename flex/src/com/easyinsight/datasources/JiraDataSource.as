package com.easyinsight.datasources {
import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.jira.JiraDataSource")]
public class JiraDataSource extends ServerDataSourceDefinition {

    public var url:String;

    public function JiraDataSource() {
        super();
        this.feedName = "JIRA";
    }
}
}