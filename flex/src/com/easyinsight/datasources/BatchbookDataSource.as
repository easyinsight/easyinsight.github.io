package com.easyinsight.datasources {
import com.easyinsight.customupload.BatchbookDataSourceCreation;
import com.easyinsight.listing.DataFeedDescriptor;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.batchbook.BatchbookCompositeSource")]
public class BatchbookDataSource extends CompositeServerDataSource {

    public var url:String;
    public var bbApiKey:String;

    public function BatchbookDataSource() {
        super();
        this.feedName = "Batchbook";
    }

    override public function isLiveData():Boolean {
        return false;
    }

    override public function getFeedType():int {
        return DataFeedDescriptor.BATCHBOOK;
    }

    override public function configClass():Class {
        return BatchbookDataSourceCreation;
    }
}
}