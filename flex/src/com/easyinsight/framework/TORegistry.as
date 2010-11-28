package com.easyinsight.framework {
import com.easyinsight.administration.feed.GoogleAnalyticsDataSource;
import com.easyinsight.administration.feed.GoogleFeedDefinition;
import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItemFault;
import com.easyinsight.analysis.AnalysisLatitude;
import com.easyinsight.analysis.AnalysisList;
import com.easyinsight.analysis.AnalysisLongitude;
import com.easyinsight.analysis.AnalysisRangeResultMetadata;
import com.easyinsight.analysis.AnalysisStep;
import com.easyinsight.analysis.AnalysisTagsResultMetadata;
import com.easyinsight.analysis.AnalysisText;
import com.easyinsight.analysis.AnalysisZipCode;
import com.easyinsight.analysis.CoordinateValue;
import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.DerivedAnalysisDimension;
import com.easyinsight.analysis.DrillThrough;
import com.easyinsight.analysis.DrillThroughEvent;
import com.easyinsight.analysis.EmptyValue;
import com.easyinsight.analysis.FeedFolder;
import com.easyinsight.analysis.FolderNode;
import com.easyinsight.analysis.NumericValue;
import com.easyinsight.analysis.ServerError;
import com.easyinsight.analysis.SixSigmaMeasure;
import com.easyinsight.analysis.StringValue;
import com.easyinsight.analysis.URLLink;
import com.easyinsight.customupload.CsvFileUploadFormat;
import com.easyinsight.customupload.ExcelUploadFormat;
import com.easyinsight.customupload.FlatFileUploadFormat;
import com.easyinsight.customupload.RedirectDataSource;
import com.easyinsight.customupload.UploadResponse;
import com.easyinsight.customupload.XSSFExcelUploadFormat;
import com.easyinsight.customupload.wizard.FieldUploadInfo;
import com.easyinsight.customupload.wizard.FlatFileUploadContext;
import com.easyinsight.datasources.BaseCampDataSource;
import com.easyinsight.datasources.CloudWatchDataSource;
import com.easyinsight.datasources.ConstantContactDataSource;
import com.easyinsight.datasources.FreshbooksDataSource;
import com.easyinsight.datasources.HighRiseDataSource;
import com.easyinsight.datasources.HighriseAdditionalToken;
import com.easyinsight.datasources.LinkedInDataSource;
import com.easyinsight.datasources.MeetupDataSource;
import com.easyinsight.datasources.PivotalTrackerBaseSource;
import com.easyinsight.datasources.SendGridDataSource;
import com.easyinsight.datasources.WholeFoodsDataSource;
import com.easyinsight.filtering.FilterDateRangeDefinition;
import com.easyinsight.filtering.FilterPatternDefinition;
import com.easyinsight.filtering.FilterRangeDefinition;
import com.easyinsight.filtering.FilterValueDefinition;
import com.easyinsight.filtering.FirstValueFilterDefinition;
import com.easyinsight.filtering.LastValueFilterDefinition;
import com.easyinsight.filtering.NullValueFilterDefinition;
import com.easyinsight.filtering.RollingDateRangeFilterDefinition;
import com.easyinsight.goals.KPITreeWrapper;
import com.easyinsight.google.GoogleSpreadsheetUploadContext;
import com.easyinsight.google.Spreadsheet;
import com.easyinsight.google.Worksheet;
import com.easyinsight.icons.Icon;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUser;
import com.easyinsight.salesforce.SalesforceFeedDefinition;

import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.solutions.SolutionInstallInfo;

import mx.managers.DragManager;
import mx.managers.PopUpManager;

public class TORegistry {
    public function TORegistry() {
    }

    public static function registerTypes():void {

        var dm:DragManager;
        var pm:PopUpManager;
        

        var analysisStep:AnalysisStep;
        var analysisLat:AnalysisLatitude;
        var analysisLong:AnalysisLongitude;
        var analysisText:AnalysisText;
        var analysisZip:AnalysisZipCode;
        var sigma:SixSigmaMeasure;
        var tags:AnalysisList;
        var solutionInstallInfo:SolutionInstallInfo;
        var hierarchy:AnalysisHierarchyItem;
        var calc:AnalysisCalculation;
        var derived:DerivedAnalysisDimension;
        var urlLink:URLLink;
        var drillthrough:DrillThrough;
        var coordinate:CoordinateValue;
        var stringValue:StringValue;
        var emptyValue:EmptyValue;
        var numValue:NumericValue;
        var drill:DrillThroughEvent;
        var filter1:FilterValueDefinition;
        var filter2:FilterDateRangeDefinition;
        var filter3:FilterRangeDefinition;
        var filter4:FilterPatternDefinition;
        var filter5:RollingDateRangeFilterDefinition;
        var filter6:FirstValueFilterDefinition;
        var filter7:LastValueFilterDefinition;
        var filter8:NullValueFilterDefinition;
        var icon:Icon;
        var scorecard:Scorecard;
        var kpi:KPI;
        var folderNode:FolderNode;
        var feedFolder:FeedFolder;
        var kpiWrapper:KPITreeWrapper;
        var excelUploadFormat:ExcelUploadFormat;
        var xssfExcelUploadFormat:XSSFExcelUploadFormat;
        var flatFileUploadFormat:FlatFileUploadFormat;
        var context:FlatFileUploadContext;
        var googleContext:GoogleSpreadsheetUploadContext;
        var csvFileUploadFormat:CsvFileUploadFormat;
        var basecamp:BaseCampDataSource;
        var highrise:HighRiseDataSource;
        var token:HighriseAdditionalToken;
        var google:GoogleAnalyticsDataSource;
        var salesforce:SalesforceFeedDefinition;
        var cloudwatch:CloudWatchDataSource;
        var googleDoc:GoogleFeedDefinition;
        var pivotal:PivotalTrackerBaseSource;
        var sendGrid:SendGridDataSource;
        var meetup:MeetupDataSource;
        var linked:LinkedInDataSource;
        var freshbooks:FreshbooksDataSource;
        var redirect:RedirectDataSource;
        var constantContact:ConstantContactDataSource;
        var wf:WholeFoodsDataSource;
        var fault:DataSourceConnectivityReportFault;
        var serverError:ServerError;
        var analysisItemFault:AnalysisItemFault;
        var metadata:AnalysisTagsResultMetadata;
        var metadata2:AnalysisRangeResultMetadata;
        var kpiUser:KPIUser;
        var info:FieldUploadInfo;
        var response:UploadResponse;
        var spreadsheet:Spreadsheet;
        var worksheet:Worksheet;
    }
}
}