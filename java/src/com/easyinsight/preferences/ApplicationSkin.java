package com.easyinsight.preferences;

import com.easyinsight.analysis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Nov 17, 2010
 * Time: 10:26:46 PM
 */
public class ApplicationSkin {
    private ImageDescriptor coreAppBackgroundImage;
    private int coreAppBackgroundColor;
    private String coreAppBackgroundSize;
    private int headerBarBackgroundColor;
    private ImageDescriptor headerBarLogo;
    private int headerBarDividerColor;
    private int centerCanvasBackgroundColor;
    private double centerCanvasBackgroundAlpha;
    private ImageDescriptor reportBackground;
    private String reportBackgroundSize;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ApplicationSkinSettings toSettings() {
        ApplicationSkinSettings settings = new ApplicationSkinSettings();
        List<ReportProperty> properties = new ArrayList<ReportProperty>();
        if (coreAppBackgroundImage != null) {
            properties.add(new ReportImageProperty("coreAppBackgroundImage", coreAppBackgroundImage));
        }
        if (headerBarLogo != null) {
            properties.add(new ReportImageProperty("headerBarLogo", headerBarLogo));
        }
        if (reportBackground != null) {
            properties.add(new ReportImageProperty("reportBackground", reportBackground));
        }
        properties.add(new ReportNumericProperty("coreAppBackgroundColor", coreAppBackgroundColor));
        properties.add(new ReportNumericProperty("headerBarBackgroundColor", headerBarBackgroundColor));
        properties.add(new ReportNumericProperty("headerBarDividerColor", headerBarDividerColor));
        properties.add(new ReportNumericProperty("centerCanvasBackgroundColor", centerCanvasBackgroundColor));
        properties.add(new ReportNumericProperty("centerCanvasBackgroundAlpha", centerCanvasBackgroundAlpha));
        properties.add(new ReportStringProperty("coreAppBackgroundSize", coreAppBackgroundSize));
        properties.add(new ReportStringProperty("reportBackgroundSize", reportBackgroundSize));
        settings.setSkinID(id);
        settings.setProperties(properties);
        return settings;
    }

    public void populateProperties(List<ReportProperty> properties) {
        coreAppBackgroundImage = findImage(properties, "coreAppBackgroundImage", null);
        coreAppBackgroundColor = (int) findNumberProperty(properties, "coreAppBackgroundColor", 0);
        headerBarBackgroundColor = (int) findNumberProperty(properties, "headerBarBackgroundColor", 0);
        centerCanvasBackgroundColor = (int) findNumberProperty(properties, "centerCanvasBackgroundColor", 0);
        centerCanvasBackgroundAlpha = findNumberProperty(properties, "centerCanvasBackgroundAlpha", 1);
        headerBarDividerColor = (int) findNumberProperty(properties, "headerBarDividerColor", 0);
        coreAppBackgroundSize = findStringProperty(properties, "coreAppBackgroundSize", "100%");
        reportBackground = findImage(properties, "reportBackground", null);
        reportBackgroundSize = findStringProperty(properties, "reportBackgroundSize", "100%");
    }

    public ImageDescriptor getCoreAppBackgroundImage() {
        return coreAppBackgroundImage;
    }

    public void setCoreAppBackgroundImage(ImageDescriptor coreAppBackgroundImage) {
        this.coreAppBackgroundImage = coreAppBackgroundImage;
    }

    public int getCoreAppBackgroundColor() {
        return coreAppBackgroundColor;
    }

    public void setCoreAppBackgroundColor(int coreAppBackgroundColor) {
        this.coreAppBackgroundColor = coreAppBackgroundColor;
    }

    public String getCoreAppBackgroundSize() {
        return coreAppBackgroundSize;
    }

    public void setCoreAppBackgroundSize(String coreAppBackgroundSize) {
        this.coreAppBackgroundSize = coreAppBackgroundSize;
    }

    public int getHeaderBarBackgroundColor() {
        return headerBarBackgroundColor;
    }

    public void setHeaderBarBackgroundColor(int headerBarBackgroundColor) {
        this.headerBarBackgroundColor = headerBarBackgroundColor;
    }

    public ImageDescriptor getHeaderBarLogo() {
        return headerBarLogo;
    }

    public void setHeaderBarLogo(ImageDescriptor headerBarLogo) {
        this.headerBarLogo = headerBarLogo;
    }

    public int getHeaderBarDividerColor() {
        return headerBarDividerColor;
    }

    public void setHeaderBarDividerColor(int headerBarDividerColor) {
        this.headerBarDividerColor = headerBarDividerColor;
    }

    public int getCenterCanvasBackgroundColor() {
        return centerCanvasBackgroundColor;
    }

    public void setCenterCanvasBackgroundColor(int centerCanvasBackgroundColor) {
        this.centerCanvasBackgroundColor = centerCanvasBackgroundColor;
    }

    public double getCenterCanvasBackgroundAlpha() {
        return centerCanvasBackgroundAlpha;
    }

    public void setCenterCanvasBackgroundAlpha(double centerCanvasBackgroundAlpha) {
        this.centerCanvasBackgroundAlpha = centerCanvasBackgroundAlpha;
    }

    public ImageDescriptor getReportBackground() {
        return reportBackground;
    }

    public void setReportBackground(ImageDescriptor reportBackground) {
        this.reportBackground = reportBackground;
    }

    public String getReportBackgroundSize() {
        return reportBackgroundSize;
    }

    public void setReportBackgroundSize(String reportBackgroundSize) {
        this.reportBackgroundSize = reportBackgroundSize;
    }

    protected String findStringProperty(List<ReportProperty> properties, String property, String defaultValue) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                ReportStringProperty reportStringProperty = (ReportStringProperty) reportProperty;
                return reportStringProperty.getValue() != null ? reportStringProperty.getValue() : defaultValue;
            }
        }
        return defaultValue;
    }

    protected boolean findBooleanProperty(List<ReportProperty> properties, String property, boolean defaultValue) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                ReportBooleanProperty reportBooleanProperty = (ReportBooleanProperty) reportProperty;
                return reportBooleanProperty.getValue();
            }
        }
        return defaultValue;
    }

    protected double findNumberProperty(List<ReportProperty> properties, String property, double defaultValue) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                ReportNumericProperty reportNumericProperty = (ReportNumericProperty) reportProperty;
                return reportNumericProperty.getValue();
            }
        }
        return defaultValue;
    }

    protected ImageDescriptor findImage(List<ReportProperty> properties, String property, ImageDescriptor defaultValue) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                ReportImageProperty reportImageProperty = (ReportImageProperty) reportProperty;
                return reportImageProperty.createImageDescriptor();
            }
        }
        return defaultValue;
    }
}
