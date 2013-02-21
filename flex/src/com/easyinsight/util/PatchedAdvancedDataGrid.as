/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/19/13
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.utils.Dictionary;

import mx.controls.AdvancedDataGrid;

public class PatchedAdvancedDataGrid extends AdvancedDataGrid {
    public function PatchedAdvancedDataGrid() {
    }

    public function clearRenderers():void {
        this.itemRendererToFactoryMap = new Dictionary(false);
        this.visibleData = new Object();
        this.listData = null;
    }
}
}
