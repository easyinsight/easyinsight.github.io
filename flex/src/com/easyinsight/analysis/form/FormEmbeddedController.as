package com.easyinsight.analysis.form {
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;

public class FormEmbeddedController implements IEmbeddedReportController {
    public function FormEmbeddedController() {
    }

    public function createEmbeddedView():EmbeddedViewFactory {
        var factory:EmbeddedViewFactory = new EmbeddedViewFactory();
        factory.reportRenderer = "FormModule.swf";
        return factory;
    }
}
}