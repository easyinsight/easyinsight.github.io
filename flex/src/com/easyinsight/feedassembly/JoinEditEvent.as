/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/21/11
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import flash.events.Event;

public class JoinEditEvent extends Event {

    public static const JOIN_EDIT:String = "joinEdit";

    public var connection:CompositeFeedConnection;

    public function JoinEditEvent(connection:CompositeFeedConnection) {
        super(JOIN_EDIT);
        this.connection = connection;
    }

    override public function clone():Event {
        return new JoinEditEvent(connection);
    }
}
}
