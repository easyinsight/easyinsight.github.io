package com.easyinsight.analysis;

import com.easyinsight.dashboard.SavedConfiguration;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.preferences.ImageDescriptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: James Boe
 * Date: Jun 21, 2009
 * Time: 11:54:19 AM
 */
public class ReportInfo {
    private WSAnalysisDefinition report;
    private boolean admin;
    private boolean accessDenied;
    private ImageDescriptor headerImage;
    private int backgroundColor;
    private int textColor;
    private List<SavedConfiguration> configurations;

    public List<SavedConfiguration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<SavedConfiguration> configurations) {
        this.configurations = configurations;
    }

    public ImageDescriptor getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(ImageDescriptor headerImage) {
        this.headerImage = headerImage;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isAccessDenied() {
        return accessDenied;
    }

    public void setAccessDenied(boolean accessDenied) {
        this.accessDenied = accessDenied;
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public JSONObject toJSON(HTMLReportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("report", getReport().toJSON(md, new ArrayList<FilterDefinition>()));
        jo.put("configurations", new JSONArray(getConfigurations().stream().map((a) -> {
            try {
                return a.toJSON();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList())));

        return jo;
    }
}
