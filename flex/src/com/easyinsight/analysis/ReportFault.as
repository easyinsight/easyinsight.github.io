package com.easyinsight.analysis {
import com.easyinsight.customupload.ProblemDataEvent;

import com.easyinsight.util.PopUpUtil;

import mx.core.UIComponent;
import mx.managers.PopUpManager;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportFault")]
public class ReportFault {
    public function ReportFault() {
    }

    public function createFaultWindow():UIComponent {
        return null;
    }

    public function popup(parent:UIComponent, onProblem:Function):void {
        var window:UIComponent = createFaultWindow();
        window.addEventListener(ProblemDataEvent.PROBLEM_RESOLVED, onProblem, false, 0, true);
        PopUpManager.addPopUp(window, parent, true);
        PopUpUtil.centerPopUp(window);
    }
}
}