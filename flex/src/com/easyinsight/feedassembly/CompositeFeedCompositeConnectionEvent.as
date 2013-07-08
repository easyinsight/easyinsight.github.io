/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/29/13
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import flash.events.Event;

public class CompositeFeedCompositeConnectionEvent extends Event {

    public static const SAVE_CONNECTION:String = "saveConnection";

    public var connection:CompositeFeedCompositeConnection;

    public function CompositeFeedCompositeConnectionEvent(type:String,  connection:CompositeFeedCompositeConnection = null) {
        super(type);
        this.connection = connection;
    }

    override public function clone():Event {
        return new CompositeFeedCompositeConnectionEvent(type, connection);
    }
}
}
