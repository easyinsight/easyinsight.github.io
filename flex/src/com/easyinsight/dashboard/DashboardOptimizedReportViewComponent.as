/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import com.easyinsight.analysis.IReportRenderer;

import mx.collections.ArrayCollection;
import mx.containers.Canvas;

public class DashboardOptimizedReportViewComponent extends Canvas implements IDashboardViewComponent {

    private var reportRenderer:IReportRenderer;

    public function DashboardOptimizedReportViewComponent() {
    }

    public function refresh(filters:ArrayCollection):void {

    }

    public function updateAdditionalFilters(filters:ArrayCollection):void {
    }

    public function initialRetrieve():void {
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
    }
}
}
