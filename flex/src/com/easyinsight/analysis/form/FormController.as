package com.easyinsight.analysis.form {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.IReportController;
import com.easyinsight.analysis.service.ListDataService;

public class FormController implements IReportController {
    public function FormController() {
    }

    public function createDataView():DataViewFactory {
        var factory:DataViewFactory = new DataViewFactory();
        factory.reportDataService = ListDataService;
        factory.reportControlBar = FormControlBar;
        factory.reportRenderer = "FormModule.swf";
        factory.newDefinition = FormReport;
        return factory;
    }
}
}