/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/23/13
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
public interface IReportPostProcessor {
    function processReport(report:AnalysisDefinition):void;
}
}
