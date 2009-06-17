package com.easyinsight.administration.feed {
import com.easyinsight.customupload.GoogleAnalyticsSourceCreation;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.ganalytics.GoogleAnalyticsDataSource")]
public class GoogleAnalyticsDataSource extends ServerDataSourceDefinition{
    public function GoogleAnalyticsDataSource() {
        super();
        this.feedName = "Google Analytics";
    }

    override public function configClass():Class {
        return GoogleAnalyticsSourceCreation;
    }
}
}