package com.easyinsight.datasources {
import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.BaseCampDataSourceCreation;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.basecamp.BaseCampDataSource")]
public class BaseCampDataSource extends ServerDataSourceDefinition{

    public var url:String;

    public function BaseCampDataSource() {
        super();
        this.feedName = "BaseCamp";
    }


    override public function configClass():Class {
        return BaseCampDataSourceCreation;
    }
}
}