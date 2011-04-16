package com.easyinsight.dashboard {
import com.easyinsight.analysis.CheckBoxReportFormItem;
import com.easyinsight.scorecard.ScorecardDescriptor;
import com.easyinsight.solutions.InsightDescriptor;

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

    override public function createEditorComponent():UIComponent {
        var comp:DashboardScorecardEditorComponent = new DashboardScorecardEditorComponent();
        comp.scorecard = this;
        return comp;
    }

    override public function createViewComponent():UIComponent {
        var comp:DashboardScorecardViewComponent = new DashboardScorecardViewComponent();
        comp.dashboardScorecard = this;
        return comp;
    }

    override public function editableProperties():ArrayCollection {
        var properties:ArrayCollection = new ArrayCollection();
        properties.addItem(new CheckBoxReportFormItem("Show Label", "showLabel", showLabel, this));
        return properties;
    }
}
}