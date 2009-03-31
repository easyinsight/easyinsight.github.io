package com.easyinsight.datasources {
import com.easyinsight.administration.feed.FeedDefinitionData;
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.basecamp.BaseCampDataSource")]
public class BaseCampDataSource extends FeedDefinitionData{

    public var url:String;

    public function BaseCampDataSource() {
        super();
        this.feedName = "BaseCamp";
    }
}
}