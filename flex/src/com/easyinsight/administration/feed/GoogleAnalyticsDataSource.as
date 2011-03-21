package com.easyinsight.administration.feed {
import com.easyinsight.customupload.GoogleAnalyticsSourceCreation;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.ganalytics.GoogleAnalyticsDataSource")]
public class GoogleAnalyticsDataSource extends ServerDataSourceDefinition{

    public var pin:String;
    public var tokenKey:String;
    public var tokenSecret:String;

    public function GoogleAnalyticsDataSource() {
        super();
        this.feedName = "Google Analytics";
    }

    override public function configClass():Class {
        return GoogleAnalyticsSourceCreation;
    }
    
    /*override public function isLiveData():Boolean {
        return true;
    }*/
}
}