/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/29/13
 * Time: 1:05 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import mx.collections.ArrayCollection;

public class CompositeFeedCompositeConnection extends CompositeFeedConnection {

    public var pairs:ArrayCollection = new ArrayCollection();

    public function CompositeFeedCompositeConnection() {
    }

    override public function get display():String {
        var str:String = "";
        for each (var pair:CompositeConnectionPair in pairs) {
            str += pair.sourceFieldDisplay + " to " + pair.targetFieldDisplay + ", ";
        }
        return str;
    }
}
}
