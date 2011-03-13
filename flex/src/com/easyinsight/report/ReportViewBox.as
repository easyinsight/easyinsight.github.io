/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 3/11/11
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.report {
import com.easyinsight.analysis.AnalysisDefinition;

import com.easyinsight.datasources.DataSourceDisplay;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.TextArea;

public class ReportViewBox extends HBox {

    private var _report:AnalysisDefinition;

    private var _description:String;

    private var _originReportID:int;

    public function ReportViewBox() {
    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    override protected function createChildren():void {
        super.createChildren();
        var descVBox:VBox = new VBox();
        var descVBoxLabel:Label = new Label();
        descVBoxLabel.text = "What is this report?";
        var descVBoxArea:TextArea = new TextArea();
        BindingUtils.bindProperty(descVBoxArea, "text", this, "description");
        descVBox.addChild(descVBoxLabel);
        descVBox.addChild(descVBoxArea);

        var exchangeVBox:VBox = new VBox();
        var exchangeVBoxArea:TextArea = new TextArea();
        exchangeVBoxArea.text = "Want to save this report to your account? You can use the report as it stands or just use it as a starting point and change fields, add filters, update styling, or anything else!";
        var exchangeButton:Button = new Button();
        exchangeButton.label = "Save to my Account";
        exchangeVBox.addChild(exchangeVBoxArea);
        exchangeVBox.addChild(exchangeButton);

        var dataSourceDisplay:DataSourceDisplay;

    }
}
}
