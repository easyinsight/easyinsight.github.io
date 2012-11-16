/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/12/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DataSourceRefreshResult")]
public class DataSourceRefreshResult {

    public var date:Date;
    public var warning:ReportFault;

    public function DataSourceRefreshResult() {
    }
}
}
