package com.easyinsight.dashboard {
import com.easyinsight.analysis.CheckBoxReportFormItem;
import com.easyinsight.scorecard.ScorecardDescriptor;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardScorecard")]
public class DashboardScorecard extends DashboardElement {

    public var scorecard:ScorecardDescriptor;
    public var labelPlacement:int;
    public var showLabel:Boolean;

    public function DashboardScorecard() {
        super();
    }

    override public function createEditorComponent(dashboardEditorMetadata:DashboardEditorMetadata):UIComponent {
        var comp:DashboardScorecardEditorComponent = new DashboardScorecardEditorComponent();
        comp.scorecard = this;
        return comp;
    }

    override public function createViewComponent(dashboardEditorMetadata:DashboardEditorMetadata):UIComponent {
        var comp:DashboardScorecardViewComponent = new DashboardScorecardViewComponent();
        comp.dashboardScorecard = this;
        return comp;
    }
}
}