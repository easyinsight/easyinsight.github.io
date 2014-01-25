package com.easyinsight.api.v3;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import nu.xom.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 8/27/13
 * Time: 1:19 PM
 */
public class ListDataSourcesServlet extends JSONServlet {
    @Override
    protected ResponseInfo processJSON(net.minidev.json.JSONObject document, EIConnection conn, HttpServletRequest request) throws Exception {
        String customField1 = request.getParameter("customField1");
        String customField2 = request.getParameter("customField2");
        String dataSourceTag = request.getParameter("dataSourceTag");
        PreparedStatement queryStmt;
        PreparedStatement tagStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID FROM ACCOUNT_TAG WHERE ACCOUNT_ID = ? AND TAG_NAME = ?");
        tagStmt.setLong(1, SecurityUtil.getAccountID());
        tagStmt.setString(2, dataSourceTag);
        ResultSet tagRS = tagStmt.executeQuery();
        tagRS.next();
        long tagID = tagRS.getLong(1);
        queryStmt = conn.prepareStatement("SELECT ANALYSIS_ID, URL_KEY, TITLE FROM ANALYSIS, DATA_SOURCE_TO_TAG WHERE " +
                "ANALYSIS.DATA_FEED_ID = DATA_SOURCE_TO_TAG.DATA_SOURCE_ID AND DATA_SOURCE_TO_TAG.ACCOUNT_TAG_ID = ?");
        queryStmt.setLong(1, tagID);

        PreparedStatement findPropsStmt = conn.prepareStatement("SELECT PROPERTY_NAME, PROPERTY_VALUE FROM REPORT_TO_REPORT_PROPERTY, REPORT_PROPERTY, REPORT_STRING_PROPERTY WHERE " +
                "REPORT_TO_REPORT_PROPERTY.ANALYSIS_ID = ? AND REPORT_TO_REPORT_PROPERTY.REPORT_PROPERTY_ID = REPORT_PROPERTY.REPORT_PROPERTY_ID AND " +
                "REPORT_PROPERTY.REPORT_PROPERTY_ID = REPORT_STRING_PROPERTY.REPORT_PROPERTY_ID");

        ResultSet dsRS = queryStmt.executeQuery();
        JSONArray arr = new JSONArray();
        while (dsRS.next()) {
            long reportID = dsRS.getLong(1);
            findPropsStmt.setLong(1, reportID);
            ResultSet propRS = findPropsStmt.executeQuery();
            String reportCustomField1 = null;
            String reportCustomField2 = null;
            while (propRS.next()) {
                String customFieldName = propRS.getString(1);
                String customFieldValue = propRS.getString(2);
                if ("customField1".equals(customFieldName)) {
                    reportCustomField1 = customFieldValue;
                } else if ("customField2".equals(customFieldName)) {
                    reportCustomField2 = customFieldValue;
                }
            }
            String urlKey = dsRS.getString(2);

            String title = dsRS.getString(3);
            boolean valid;
            if (customField1 != null && customField2 != null) {
                valid = (customField1.equals(reportCustomField1) && customField2.equals(reportCustomField2));
            } else if (customField1 != null) {
                valid = customField1.equals(reportCustomField1);
            } else if (customField2 != null) {
                valid = customField2.equals(reportCustomField2);
            } else {
                valid = true;
            }
            if (valid) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("reportKey", urlKey);
                jsonObject.put("customField1", reportCustomField1);
                jsonObject.put("customField2", reportCustomField2);
                jsonObject.put("name", title);
                arr.put(jsonObject);
            }
        }
        queryStmt.close();
        findPropsStmt.close();
        return new ResponseInfo(ResponseInfo.ALL_GOOD, arr.toString());
    }
}
