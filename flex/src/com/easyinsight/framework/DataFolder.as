/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/28/11
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
import com.easyinsight.quicksearch.EIDescriptor;

import mx.collections.ArrayCollection;

public class DataFolder extends EIDescriptor {

    public var children:ArrayCollection;

    public function DataFolder(name:String = null, id:int = 0) {
        this.name = name;
        this.id = id;
    }

    override public function getType():int {
        return EIDescriptor.FOLDER;
    }

    override public function get accountVisibleDisplay():String {
        return "";
    }
}
}
