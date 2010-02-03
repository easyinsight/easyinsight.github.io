package com.easyinsight.kpi {
import com.easyinsight.administration.feed.FeedConsumer;

[Bindable]
[RemoteClass(alias="com.easyinsight.kpi.KPIUser")]
public class KPIUser {

    public var feedConsumer:FeedConsumer;
    public var owner:Boolean;
    public var responsible:Boolean;

    public function KPIUser() {
    }
}
}