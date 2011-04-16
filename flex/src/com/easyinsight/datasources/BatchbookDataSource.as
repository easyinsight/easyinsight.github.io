package com.easyinsight.datasources {
import com.easyinsight.customupload.BatchbookDataSourceCreation;



[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.batchbook.BatchbookCompositeSource")]
public class BatchbookDataSource extends CompositeServerDataSource {

    public var url:String;
    public var bbApiKey:String;

    public function BatchbookDataSource() {
        super();
        this.feedName = "Batchbook";
    }

    override public function getFeedType():int {
        return DataSourceType.BATCHBOOK;
    }

    override public function configClass():Class {
        return BatchbookDataSourceCreation;
    }
}
}