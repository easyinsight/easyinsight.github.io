/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/6/13
 * Time: 11:49 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.util.RolloverLabel;

public class DescriptorRolloverLabel extends RolloverLabel {
    public function DescriptorRolloverLabel() {
        super();
    }

    override protected function isLinkable(value:Object):Boolean {
        return true;
    }
}
}
