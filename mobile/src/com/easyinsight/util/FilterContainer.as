/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/1/11
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {

import spark.components.Group;
import spark.components.SkinnableContainer;

public class FilterContainer extends SkinnableContainer {
    public function FilterContainer() {
    }

    [SkinPart(required="false")]
    public var toggleGroup:Group;

    private var _toggleContent:Array = [];

    [ArrayElementType("mx.core.IVisualElement")]
    public function get toggleContent():Array {
        return _toggleContent;
    }

    public function set toggleContent(value:Array):void {
        _toggleContent = value;
        if (toggleGroup) {
            toggleGroup.mxmlContent = value;
        }
    }

    override protected function partAdded(partName:String, instance:Object):void {
        super.partAdded(partName, instance);
        if (instance == toggleGroup) {
            toggleGroup.mxmlContent = _toggleContent;
        }
    }

    override protected function partRemoved(partName:String, instance:Object):void {
        super.partRemoved(partName, instance);
        if (instance == toggleGroup) {
            toggleGroup.mxmlContent = null;
        }
    }
}
}
