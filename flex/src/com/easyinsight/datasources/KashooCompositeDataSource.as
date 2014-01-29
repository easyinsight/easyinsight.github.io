package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.KashooDataSourceCreation;
import com.easyinsight.customupload.PivotalTrackerDataSourceCreation;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.kashoo.KashooCompositeSource")]
public class KashooCompositeDataSource extends CompositeServerDataSource {

    public var ksUserName:String;
    public var ksPassword:String;

    public function KashooCompositeDataSource() {
        super();
        feedName = "Kashoo";
    }

    override public function getFeedType():int {
        return DataSourceType.KASHOO;
    }

    override public function configClass():Class {
        return KashooDataSourceCreation;
    }
}
}