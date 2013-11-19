package com.easyinsight.analysis;

import com.easyinsight.dashboard.SavedConfiguration;
import com.easyinsight.preferences.ImageDescriptor;

import java.util.List;

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
}
