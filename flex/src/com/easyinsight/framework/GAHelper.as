package com.easyinsight.framework {
import com.google.analytics.components.FlexTracker;

public class GAHelper {

    private static var helper:GAHelper;

    public var tracker:FlexTracker;

    public function GAHelper() {
    }

    public static function instance():GAHelper {
        return helper;
    }

    public static function initialize(tracker:FlexTracker):void {
        helper = new GAHelper();
        helper.tracker = tracker;
    }
}
}