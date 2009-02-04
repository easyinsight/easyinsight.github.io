package com.easyinsight.dbservice {
import flash.events.Event;
public class DBValidationEvent extends Event {
    public static const DATABASE_VALIDATION:String = "databaseValidation";

    public var databaseConfiguration:DatabaseConfiguration;


    public function DBValidationEvent(databaseConfiguration:DatabaseConfiguration) {
        super(DATABASE_VALIDATION);
        this.databaseConfiguration = databaseConfiguration;
    }


    override public function clone():Event {
        return DBValidationEvent(databaseConfiguration);
    }
}
}