/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 2/28/11
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import flash.events.Event;

public class ConnectionDeleteEvent extends Event {

    public static const CONNECTION_DELETE:String = "connectionDelete";

    public var connection:CompositeFeedConnection;

    public function ConnectionDeleteEvent(connection:CompositeFeedConnection) {
        super(CONNECTION_DELETE, true);
        this.connection = connection;
    }

    override public function clone():Event {
        return new ConnectionDeleteEvent(connection);
    }
}
}
