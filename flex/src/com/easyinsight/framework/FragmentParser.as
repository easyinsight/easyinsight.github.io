package com.easyinsight.framework {
import com.easyinsight.analysis.DelayedAPIKeyLink;
import com.easyinsight.analysis.DelayedDeepLink;
import com.easyinsight.analysis.DelayedFeedLink;
import com.easyinsight.analysis.DelayedReportLink;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.goals.DelayedGoalAdminLink;
import com.easyinsight.goals.DelayedGoalLink;
import com.easyinsight.groups.DelayedGroupLink;
import com.easyinsight.listing.ListingChangeEvent;
import com.easyinsight.report.DelayedMultiReportLink;
import com.easyinsight.report.DelayedPackageLink;
import com.easyinsight.report.StaticReportSource;
import com.easyinsight.solutions.DelayedSolutionLink;

public class FragmentParser {

    private var parsers:Array;

    public function FragmentParser() {
        parsers = [
            new FragmentTester("analysisID", function(key:String, workspace:PrimaryWorkspace, o:Object):void {
                var deepAnalysisLink:DelayedDeepLink = new DelayedDeepLink(Number(key));
                deepAnalysisLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepAnalysisLink.execute();
            }),
            new FragmentTester("feedID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var deepFeedLink:DelayedFeedLink = new DelayedFeedLink(Number(key));
                deepFeedLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepFeedLink.execute();
            }),
            new FragmentTester("feedKey", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var deepAPILink:DelayedAPIKeyLink = new DelayedAPIKeyLink(key);
                deepAPILink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepAPILink.execute();
            }),
            new FragmentTester("groupID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var groupLink:DelayedGroupLink = new DelayedGroupLink(Number(key));
                groupLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                groupLink.execute();
            }),
            new FragmentTester("staticReportID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                workspace.internalAnalyze(new AnalyzeEvent(new StaticReportSource(Number(key))));
            }),
            new FragmentTester("goalTreeID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var goalLink:DelayedGoalLink = new DelayedGoalLink(Number(key));
                goalLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                goalLink.execute();
            }),
            new FragmentTester("activationString", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var accountActivator:AccountActivator = new AccountActivator(key, workspace);
                accountActivator.activate();
            }),
            new FragmentTester("solutionID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var solutionLink:DelayedSolutionLink = new DelayedSolutionLink(Number(key));
                solutionLink.addEventListener(ListingChangeEvent.LISTING_CHANGE, workspace.changePerspective);
                solutionLink.execute();
            }),
            new FragmentTester("multiReportID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var multiReportLink:DelayedMultiReportLink = new DelayedMultiReportLink(Number(key));
                multiReportLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                multiReportLink.execute();
            }),
            new FragmentTester("packageID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var delayedPackageLink:DelayedPackageLink = new DelayedPackageLink(Number(key));
                delayedPackageLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                delayedPackageLink.execute();
            }),
            new FragmentTester("reportID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var deepReportLink:DelayedReportLink = new DelayedReportLink(Number(key));
                deepReportLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                deepReportLink.execute();
            }),
            new FragmentTester("goalTreeAdminID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var delayedGoalAdminLink:DelayedGoalAdminLink = new DelayedGoalAdminLink(Number(key));
                delayedGoalAdminLink.addEventListener(AnalyzeEvent.ANALYZE, workspace.internalAnalyze);
                delayedGoalAdminLink.execute();
            }),
            new FragmentTester("redirectID", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                var redirectType:int = int(key);
                var sessionToken:String = String(o.token);
                var redirector:TokenRedirector = new TokenRedirector();
                redirector.solutionID = int(o.refSolutionID);
                redirector.type = redirectType;
                redirector.onURL(sessionToken);
                redirector.addEventListener(ListingChangeEvent.LISTING_CHANGE, workspace.changePerspective);
            }),
            new FragmentTester("resetPassword", function(key:String, workspace:PrimaryWorkspace, o:Object):void  {
                new PasswordReset(key, workspace).reset();
                workspace.navigation(new NavigationEvent("Home"));
            })
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