package com.easyinsight.customupload {
import com.easyinsight.administration.feed.FeedDefinitionData;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.redirect.RedirectDataSource")]
public class RedirectDataSource extends FeedDefinitionData {

    public var redirectDataSourceID:int;

    public function RedirectDataSource() {
        super();
    }

    
}
}