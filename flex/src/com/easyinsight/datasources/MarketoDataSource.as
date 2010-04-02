package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.MarketoDataSourceCreation;
import com.easyinsight.listing.DataFeedDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.marketo.MarketoDataSource")]
public class MarketoDataSource extends ServerDataSourceDefinition {
    public function MarketoDataSource() {
        super();
        feedName = "Marketo";
    }

    override public function isLiveData():Boolean {
        return false;
    }

    override public function getFeedType():int {
        return DataFeedDescriptor.MARKETO;
    }

    override public function configClass():Class {
        return MarketoDataSourceCreation;
    }
}
}