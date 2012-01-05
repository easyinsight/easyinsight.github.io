/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/2/12
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.charts {
import mx.controls.List;
import mx.core.ClassFactory;

public class EILegend extends List {
    public function EILegend() {
        itemRenderer = new ClassFactory(EILegendRenderer);
        setStyle("borderStyle", "none");
        //this.width = 200;
    }
}
}
