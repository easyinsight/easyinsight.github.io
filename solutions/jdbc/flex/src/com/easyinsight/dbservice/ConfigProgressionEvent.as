package com.easyinsight.dbservice {
import flash.events.Event;
import flash.events.Event;
public class ConfigProgressionEvent extends Event{
    public static const DB_CONFIGURED:String = "dbConfigured";

    public var dbConfig:DatabaseConfiguration;
    public var eiConfig:EIConfiguration;

    public function ConfigProgressionEvent(dbConfig:DatabaseConfiguration, eiConfig:EIConfiguration = null) {
        super(DB_CONFIGURED);
        this.dbConfig = dbConfig;
        this.eiConfig = eiConfig;
    }


    override public function clone():Event {
        return new ConfigProgressionEvent(dbConfig, eiConfig);
    }
}
}