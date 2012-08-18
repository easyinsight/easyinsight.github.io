package com.easyinsight.analysis {
import com.easyinsight.administration.feed.FeedDefinitionData;

import com.easyinsight.customupload.ProblemDataEvent;
import com.easyinsight.customupload.ProblemDataSource;
import com.easyinsight.datasources.IServerDataSourceDefinition;

import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.DataSourceConnectivityReportFault")]
public class DataSourceConnectivityReportFault extends ReportFault {

    public var dataSource:FeedDefinitionData;
    public var message:String;

    public function DataSourceConnectivityReportFault() {
        super();
    }
    
    override public function createFaultWindow():UIComponent {
        var sds:IServerDataSourceDefinition = dataSource as IServerDataSourceDefinition;
        var configClass:Class = sds.configClass();
        var admin:ProblemDataSource = new ProblemDataSource();
        admin.addEventListener(ProblemDataEvent.PROBLEM_RESOLVED, onResolved);
        admin.dataSourceClass = configClass;
        admin.dataSourceDefinition = dataSource;
        admin.problemMessage = message;
        return admin;
    }

    override public function canResolve():Boolean {
        return true;
    }

    private function onResolved(event:ProblemDataEvent):void {
        dispatchEvent(event);
    }

    override public function getMessage():String {
        return message;
    }
}
}