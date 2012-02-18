package com.easyinsight.framework {
import com.easyinsight.analysis.DelayedAPIKeyLink;
import com.easyinsight.analysis.DelayedDeepLink;
import com.easyinsight.analysis.DelayedFeedAdminLink;
import com.easyinsight.analysis.DelayedFeedLink;
import com.easyinsight.analysis.DelayedNewDashboardLink;
import com.easyinsight.analysis.DelayedReportLink;
import com.easyinsight.dashboard.DelayedDashboardViewLink;
import com.easyinsight.etl.DelayedLookupTableLink;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.genredata.DelayedReportTemplate;
import com.easyinsight.goals.DelayedDashboardAdminLink;
import com.easyinsight.goals.DelayedScorecardAdminLink;
import com.easyinsight.goals.DelayedScorecardViewLink;
import com.easyinsight.listing.ListingChangeEvent;
import com.easyinsight.solutions.DelayedSolutionLink;

public class FragmentParser {

    private var parsers:Array;

    public function FragmentParser() {
        parsers = [
            new FragmentTester("analysisID", function(key:String, workspace:PrimaryWorkspace, o:Object):void {
                var deepAnalysisLink:DelayedDeepLink = new DelayedDeepLink(key);
                deepAnalysisLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepAnalysisLink.execute();
            }),
            new FragmentTester("feedID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var deepFeedLink:DelayedFeedLink = new DelayedFeedLink(key);
                deepFeedLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepFeedLink.execute();
            }),
            new FragmentTester("newDashboardID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var deepFeedLink:DelayedNewDashboardLink = new DelayedNewDashboardLink(key);
                deepFeedLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepFeedLink.execute();
            }),
            new FragmentTester("feedAdminID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var deepFeedLink:DelayedFeedAdminLink = new DelayedFeedAdminLink(key);
                deepFeedLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepFeedLink.execute();
            }),
            new FragmentTester("feedKey", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var deepAPILink:DelayedAPIKeyLink = new DelayedAPIKeyLink(key);
                deepAPILink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepAPILink.execute();
            }),
            new FragmentTester("solutionID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var solutionLink:DelayedSolutionLink = new DelayedSolutionLink(Number(key));
                solutionLink.addEventListener(ListingChangeEvent.LISTING_CHANGE, workspace.changePerspective);
                solutionLink.execute();
            }),
            new FragmentTester("reportID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var deepReportLink:DelayedReportLink = new DelayedReportLink(key);
                deepReportLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepReportLink.execute();
            }),
            new FragmentTester("scorecardAdminID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var delayedScorecardAdminLink:DelayedScorecardAdminLink = new DelayedScorecardAdminLink(key);
                delayedScorecardAdminLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                delayedScorecardAdminLink.execute();
            }),
            new FragmentTester("scorecardViewID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var delayedScorecardViewLink:DelayedScorecardViewLink = new DelayedScorecardViewLink(key);
                delayedScorecardViewLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                delayedScorecardViewLink.execute();
            }),
            new FragmentTester("dashboardAdminID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var delayedDashboardAdminLink:DelayedDashboardAdminLink = new DelayedDashboardAdminLink(key);
                delayedDashboardAdminLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                delayedDashboardAdminLink.execute();
            }),
            new FragmentTester("dashboardID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var delayedDashboardAdminLink:DelayedDashboardViewLink = new DelayedDashboardViewLink(key);
                delayedDashboardAdminLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                delayedDashboardAdminLink.execute();
            }),
            new FragmentTester("lookupTableID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var delayedLookupTableLink:DelayedLookupTableLink = new DelayedLookupTableLink(key);
                delayedLookupTableLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                delayedLookupTableLink.execute();
            }),
            new FragmentTester("reportTemplateID", function(key:String, workspace:PrimaryWorkspace, o:Object):void {
                var delayedTemplate:DelayedReportTemplate = new DelayedReportTemplate(key);
                delayedTemplate.addEventListener(ListingChangeEvent.LISTING_CHANGE, workspace.changePerspective);
                delayedTemplate.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                delayedTemplate.execute();
            }),
            new FragmentTester("connectionConfig", function(key:String, workspace:PrimaryWorkspace, o:Object):void {
                var config:ConnectionConfig = new ConnectionConfig(key);
                config.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                config.execute();
            }),
            new FragmentTester("redirectID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                if (User.getInstance().guestUser) return;
                var redirectType:int = int(key);
                var sessionToken:String = String(o.token);
                var redirector:TokenRedirector = new TokenRedirector();
                redirector.solutionID = int(o.refSolutionID);
                redirector.type = redirectType;
                redirector.onURL(sessionToken);
                redirector.addEventListener(ListingChangeEvent.LISTING_CHANGE, workspace.changePerspective);
            }),
            new FragmentTester("oauthRedirectID", function(key:String, workspace:PrimaryWorkspace, o:Object):void {
                if (User.getInstance().guestUser) return;
                var oauthRedirectID:int = int(key);
                var token:String = String(o.token);
                var secret:String = String(o.secret);
                var refSolutionID:int = int(o.refSolutionID);
                var properties:Object = new Object();
                properties["token"] = token;
                properties["secret"] = secret;
            })/*,
            new FragmentTester("resetPassword", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                new PasswordReset(key, workspace).reset();
                workspace.navigation(new NavigationEvent("Home"));
            })*/
        ];
    }

    public function parseFragmentObject(object:Object, primaryWorkspace:PrimaryWorkspace):Boolean {
        var matched:Boolean = false;
        for each (var tester:FragmentTester in parsers) {
            matched = (tester.test(object, primaryWorkspace));
            if (matched) {
                break;
            }
        }
        return matched;
    }
}
}