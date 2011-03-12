/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/9/11
 * Time: 9:25 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import flash.events.Event;

public class DeleteFederatedEvent extends Event {

    public static const DELETE_FEDERATED:String = "deleteFederated";

    public var source:FederationSource;

    public function DeleteFederatedEvent(source:FederationSource) {
        super(DELETE_FEDERATED, true);
        this.source = source;
    }

    override public function clone():Event {
        return new DeleteFederatedEvent(source);
    }
}
}
