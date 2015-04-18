package com.easyinsight.datasources {
import com.easyinsight.customupload.BasecampNextConfiguration;
import com.easyinsight.customupload.BasecampNextDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.surveygizmo.SurveyGizmoCompositeSource")]
public class SurveyGizmoCompositeSource extends CompositeServerDataSource {

    public var sgToken:String;
    public var sgSecret:String;
    public var formID:String;

    public function SurveyGizmoCompositeSource() {
        super();
    }

    override public function getFeedType():int {
        return DataSourceType.SURVEY_GIZMO;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return BasecampNextDataSourceCreation;
    }

    override public function requiresMoreSetupAfterAuth():Boolean {
        return true;
    }
}
}