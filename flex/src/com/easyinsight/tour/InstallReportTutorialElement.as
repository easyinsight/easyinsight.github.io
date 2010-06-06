package com.easyinsight.tour {
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.genredata.ExchangeReportData;
import com.easyinsight.genredata.SolutionReportExchangeItem;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.ProgressAlert;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class InstallReportTutorialElement extends TutorialElement {

    private var _solutionID:int;
    private var solutionService:RemoteObject;

    private var selectedReport:SolutionReportExchangeItem;

    public function InstallReportTutorialElement(solutionID:int) {
        super();
        _solutionID = solutionID;
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.getSolutionReports.addEventListener(ResultEvent.RESULT, gotReports);
        solutionService.determineDataSource.addEventListener(ResultEvent.RESULT, determinedDataSource);
        solutionService.installReport.addEventListener(ResultEvent.RESULT, installedReport);
    }

    override public function forwardExecute():void {
        ProgressAlert.alert(this, "Installing report into your account...", null, solutionService.getSolutionReports,
                solutionService.determineDataSource, solutionService.installReport);
        solutionService.getSolutionReports.send();
    }

    private function gotReports(event:ResultEvent):void {
        var reports:ArrayCollection = solutionService.getSolutionReports.lastResult as ArrayCollection;
        var maxRating:Number = -1;
        var dataSourceID:int = 0;
        for each (var solutionItem:SolutionReportExchangeItem in reports) {
            if (solutionItem.solutionID == _solutionID && solutionItem.exchangeData is ExchangeReportData) {
                var exchangeReportData:ExchangeReportData = solutionItem.exchangeData as ExchangeReportData;
                if (solutionItem.ratingAverage > maxRating) {
                    selectedReport = solutionItem;
                    dataSourceID = exchangeReportData.dataSourceID;
                }
            }
        }
        solutionService.determineDataSource.send(dataSourceID);
    }

    private function determinedDataSource(event:ResultEvent):void {
        var dataSources:ArrayCollection = solutionService.determineDataSource.lastResult as ArrayCollection;
        solutionService.installReport.send(selectedReport.id, dataSources.getItemAt(0).id);
    }

    private function installedReport(event:ResultEvent):void {
        var insightDescriptor:InsightDescriptor = solutionService.installReport.lastResult as InsightDescriptor;
        // has to emit special property here to let us decide whether or not we want to keep this report
        dispatchEvent(new NoteEvent(NoteEvent.EVENT_NOTE, null, new AnalyzeEvent(new ReportAnalyzeSource(insightDescriptor, null, true, 0, selectedReport.id, selectedReport.ratingAverage))));
    }
}
}