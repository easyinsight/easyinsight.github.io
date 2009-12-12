package com.easyinsight.reportpackage {
import flash.events.Event;

public class PackageEvent extends Event {

    public static const PACKAGE_ADDED:String = "packageAdded";
    public static const PACKAGE_UPDATED:String = "packageUpdated";
    public static const PACKAGE_DELETED:String = "packageDeleted";

    public var reportPackage:ReportPackage;

    public function PackageEvent(type:String, reportPackage:ReportPackage) {
        super(type);
        this.reportPackage = reportPackage;
    }

    override public function clone():Event {
        return new PackageEvent(type, reportPackage);
    }
}
}