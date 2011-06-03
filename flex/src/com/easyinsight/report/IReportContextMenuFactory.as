/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/3/11
 * Time: 10:22 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.solutions.InsightDescriptor;

import flash.display.DisplayObject;

import flash.ui.ContextMenu;

public interface IReportContextMenuFactory {
    function createReportContextMenu(insightDescriptor:InsightDescriptor, viewFactory:EmbeddedViewFactory, dObj:DisplayObject):ContextMenu;
}
}
