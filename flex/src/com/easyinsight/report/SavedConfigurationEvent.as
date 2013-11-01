/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/28/13
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.report {
import flash.events.Event;

public class SavedConfigurationEvent extends Event {

    public static const CONFIGURATION_ADD:String = "addConfiguration";
    public static const CONFIGURATION_UPDATE:String = "updateConfiguration";

    public var configuration:SavedConfiguration;

    public function SavedConfigurationEvent(type:String, configuration:SavedConfiguration) {
        super(type);
        this.configuration = configuration;
    }

    override public function clone():Event {
        return new SavedConfigurationEvent(type, configuration);
    }
}
}
