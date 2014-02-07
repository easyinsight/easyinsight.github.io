package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.KashooDataSourceCreation;
import com.easyinsight.customupload.PivotalTrackerDataSourceCreation;
import com.easyinsight.customupload.Solve360DataSourceCreation;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.solve360.Solve360CompositeSource")]
public class Solve360CompositeDataSource extends CompositeServerDataSource {

    public var userEmail:String;
    public var authKey:String;

    public function Solve360CompositeDataSource() {
        super();
        feedName = "Solve360";
    }

    override public function getFeedType():int {
        return DataSourceType.SOLVE360;
    }

    override public function configClass():Class {
        return Solve360DataSourceCreation;
    }
}
}