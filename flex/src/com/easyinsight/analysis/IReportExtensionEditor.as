/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/27/11
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

public interface IReportExtensionEditor {
    function save(analysisItem:AnalysisItem):void;
    function set analysisItem(analysisItem:AnalysisItem):void;
    function set analysisItems(analysisItems:ArrayCollection):void;
}
}
