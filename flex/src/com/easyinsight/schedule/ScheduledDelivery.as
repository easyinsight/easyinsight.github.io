package com.easyinsight.schedule {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.ScheduledDelivery")]
public class ScheduledDelivery extends ScheduledActivity {

    public var users:ArrayCollection = new ArrayCollection();
    public var emails:ArrayCollection = new ArrayCollection();

    public function ScheduledDelivery() {
        super();
    }
}
}