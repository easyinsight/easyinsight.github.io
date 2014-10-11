package com.easyinsight.api.v3;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.AutoComposite;
import com.easyinsight.datafeeds.DataSourceSuggestion;
import com.easyinsight.export.ScheduledActivity;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SuggestedActionsServlet extends JSONServlet {

    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        JSONObject responseObject = new JSONObject();

        List<DataSourceSuggestion> suggestions = getDataSourceSuggestions(conn);


        JSONArray array = new JSONArray(suggestions.stream().sequential().map(DataSourceSuggestion::toJSON).toArray(JSONObject[]::new));
        responseObject.put("suggestions", array);
        return new ResponseInfo(ResponseInfo.ALL_GOOD, responseObject.toString());
    }

    public static List<DataSourceSuggestion> getDataSourceSuggestions(EIConnection conn) throws SQLException {
        List<DataSourceSuggestion> suggestions = new ArrayList<>();

        // does the user have any report deliveries set up?

        PreparedStatement ps = conn.prepareStatement("SELECT scheduled_account_activity.scheduled_account_activity_id FROM scheduled_account_activity WHERE " +
                "account_id = ? AND (scheduled_account_activity.activity_type = ? OR scheduled_account_activity.activity_type = ?)");
        ps.setLong(1, SecurityUtil.getAccountID());
        ps.setInt(2, ScheduledActivity.REPORT_DELIVERY);
        ps.setInt(3, ScheduledActivity.GENERAL_DELIVERY);
        ResultSet activityRS = ps.executeQuery();

        if (activityRS.next()) {
            // report delivery configured
        } else {
            PreparedStatement autoSetupPossibleStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, TITLE FROM ANALYSIS, USER_TO_ANALYSIS WHERE ANALYSIS.AUTO_SETUP_DELIVERY = ? AND " +
                    "ANALYSIS.ANALYSIS_ID = USER_TO_ANALYSIS.ANALYSIS_ID AND USER_TO_ANALYSIS.USER_ID = ? AND " +
                    "ANALYSIS.ACCOUNT_VISIBLE = ?");
            autoSetupPossibleStmt.setBoolean(1, true);
            autoSetupPossibleStmt.setLong(2, SecurityUtil.getUserID());
            autoSetupPossibleStmt.setBoolean(3, true);
            ResultSet rs = autoSetupPossibleStmt.executeQuery();
            boolean autoSetupPossible = rs.next();
            autoSetupPossibleStmt.close();
            if (autoSetupPossible) {
                suggestions.add(new DataSourceSuggestion(DataSourceSuggestion.SUGGEST_DELIVER_REPORTS, "Set up recurring emails of reports", "/app/quickReportDeliverySetup?action=create_email"));
            }
        }

        ps.close();

        int autoState = AutoComposite.autoDashboardable(conn);

        if (autoState == AutoComposite.NADA) {
            // no suggestion
        } else if (autoState == AutoComposite.CREATE_COMPOSITE) {
            suggestions.add(new DataSourceSuggestion(DataSourceSuggestion.SUGGEST_CREATE_COMPOSITE, "Combine sources into a dashboard", "/app/autoDataSourceAction?action=create_composite"));
        } else if (autoState == AutoComposite.ADD_TO_COMPOSITE) {
            suggestions.add(new DataSourceSuggestion(DataSourceSuggestion.SUGGEST_ADD_TO_COMPOSITE, "Add new connections to dashboard", "/app/autoDataSourceAction?action=add_to_composite"));
        } else if (autoState == AutoComposite.CREATE_DASHBOARD_ON_COMPOSITE) {
            suggestions.add(new DataSourceSuggestion(DataSourceSuggestion.SUGGEST_CREATE_DASHBOARD, "Create a combined dashboard", "/app/autoDataSourceAction?action=create_dashboard"));
        }
        return suggestions;
    }
}
