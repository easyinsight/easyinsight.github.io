/**
 * Created by jamesboe on 1/8/15.
 */
package com.easyinsight.schedule {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.ActivitySequence")]
public class ActivitySequence extends ScheduledActivity {

    public var activities:ArrayCollection;

    public function ActivitySequence() {
    }

    override public function get activityDisplay():String {
        return describe;
    }

    override public function get describe():String {
        return "Sequence of activities";
    }
}
}
