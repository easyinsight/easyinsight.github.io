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
    
    public static final int APPLICATION = 1;
    public static final int ACCOUNT = 2;
    public static final int USER = 3;

    private ImageDescriptor coreAppBackgroundImage;
    private boolean coreAppBackgroundImageEnabled;
    private int coreAppBackgroundColor;
    private boolean coreAppBackgroundColorEnabled;
    private String coreAppBackgroundSize;
    private boolean coreAppBackgroundSizeEnabled;
    private int headerBarBackgroundColor;
    private boolean headerBarBackgroundColorEnabled;
    private ImageDescriptor headerBarLogo;
    private ImageDescriptor reportHeaderImage;
    private int headerBarDividerColor;
    private boolean headerBarDividerColorEnabled;
    private int centerCanvasBackgroundColor;
    private boolean centerCanvasBackgroundColorEnabled;
    private double centerCanvasBackgroundAlpha;
    private boolean centerCanvasBackgroundAlphaEnabled;
    private boolean reportBackgroundEnabled;
    private boolean reportBackgroundSizeEnabled;
    private int reportBackgroundColor;
    private int reportTextColor;
    private boolean reportHeader;
    private boolean myDataName;
    private boolean myDataSize;
    private boolean myDataOwner;
    private boolean myDataCreationDate;
    private boolean myDataLastTime;
    private boolean myDataCombine;
    private boolean myDataNewScorecard;
    private boolean myDataNewKPITree;
    private boolean myDataNewDashboard;
    private boolean myDataLookupTable;
    private boolean myDataAccountVisible;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ApplicationSkinSettings toSettings(int mode) {
        ApplicationSkinSettings settings = new ApplicationSkinSettings();
        List<ReportProperty> properties = new ArrayList<ReportProperty>();
        if (mode == APPLICATION || mode == ACCOUNT) {
            if (coreAppBackgroundImage != null) {
                properties.add(new ReportImageProperty("coreAppBackgroundImage", coreAppBackgroundImage, coreAppBackgroundImageEnabled));
            }
            if (headerBarLogo != null) {
                properties.add(new ReportImageProperty("headerBarLogo", headerBarLogo, true));
            }
            if (reportHeaderImage != null) {
                properties.add(new ReportImageProperty("reportHeaderImage", reportHeaderImage, true));
            }
            properties.add(new ReportNumericProperty("coreAppBackgroundColor", coreAppBackgroundColor, coreAppBackgroundColorEnabled));
            properties.add(new ReportNumericProperty("headerBarBackgroundColor", headerBarBackgroundColor, headerBarBackgroundColorEnabled));
            properties.add(new ReportNumericProperty("headerBarDividerColor", headerBarDividerColor, headerBarDividerColorEnabled));
            properties.add(new ReportNumericProperty("centerCanvasBackgroundColor", centerCanvasBackgroundColor, centerCanvasBackgroundColorEnabled));
            properties.add(new ReportNumericProperty("centerCanvasBackgroundAlpha", centerCanvasBackgroundAlpha, centerCanvasBackgroundAlphaEnabled));
            properties.add(new ReportStringProperty("coreAppBackgroundSize", coreAppBackgroundSize, coreAppBackgroundSizeEnabled));
            properties.add(new ReportNumericProperty("reportBackgroundColor", reportBackgroundColor, true));
            properties.add(new ReportNumericProperty("reportTextColor", reportTextColor, true));
            properties.add(new ReportBooleanProperty("reportHeader", reportHeader, true));
        }
        if (mode == APPLICATION || mode == USER) {
            properties.add(new ReportBooleanProperty("myDataName", myDataName));
            properties.add(new ReportBooleanProperty("myDataSize", myDataSize));
            properties.add(new ReportBooleanProperty("myDataOwner", myDataOwner));
            properties.add(new ReportBooleanProperty("myDataCreationDate", myDataCreationDate));
            properties.add(new ReportBooleanProperty("myDataLastTime", myDataLastTime));
            properties.add(new ReportBooleanProperty("myDataCombine", myDataCombine));
            properties.add(new ReportBooleanProperty("myDataScorecard", myDataNewScorecard));
            properties.add(new ReportBooleanProperty("myDataKPITree", myDataNewKPITree));
            properties.add(new ReportBooleanProperty("myDataDashboard", myDataNewDashboard));
            properties.add(new ReportBooleanProperty("myDataLookupTable", myDataLookupTable));
            properties.add(new ReportBooleanProperty("myDataAccountVisible", myDataAccountVisible));
        }
        settings.setSkinID(id);
        settings.setProperties(properties);
        return settings;
    }

    public void populateProperties(List<ReportProperty> properties) {
        coreAppBackgroundImage = findImage(properties, "coreAppBackgroundImage", null);
        headerBarLogo = findImage(properties, "headerBarLogo", null);
        coreAppBackgroundImageEnabled = propertyEnabled(properties, "coreAppBackgroundImage");
        coreAppBackgroundColor = (int) findNumberProperty(properties, "coreAppBackgroundColor", 0);
        coreAppBackgroundColorEnabled = propertyEnabled(properties, "coreAppBackgroundColor");
        headerBarBackgroundColor = (int) findNumberProperty(properties, "headerBarBackgroundColor", 0);
        headerBarBackgroundColorEnabled = propertyEnabled(properties, "headerBarBackgroundColor");
        centerCanvasBackgroundColor = (int) findNumberProperty(properties, "centerCanvasBackgroundColor", 0);
        centerCanvasBackgroundColorEnabled = propertyEnabled(properties, "centerCanvasBackgroundColor");
        centerCanvasBackgroundAlpha = findNumberProperty(properties, "centerCanvasBackgroundAlpha", 1);
        centerCanvasBackgroundAlphaEnabled = propertyEnabled(properties, "centerCanvasBackgroundAlpha");
        headerBarDividerColor = (int) findNumberProperty(properties, "headerBarDividerColor", 0);
        headerBarDividerColorEnabled = propertyEnabled(properties, "headerBarDividerColor");
        coreAppBackgroundSize = findStringProperty(properties, "coreAppBackgroundSize", "100%");
        coreAppBackgroundSizeEnabled = propertyEnabled(properties, "coreAppBackgroundSize");
        reportBackgroundEnabled = propertyEnabled(properties, "reportBackground");
        reportBackgroundSizeEnabled = propertyEnabled(properties, "reportBackgroundSize");
        reportBackgroundColor = (int) findNumberProperty(properties, "reportBackgroundColor", 0);
        reportTextColor = (int) findNumberProperty(properties, "reportTextColor", 0);
        reportHeader = findBooleanProperty(properties, "reportHeader", false);
        reportHeaderImage = findImage(properties, "reportHeaderImage", null);
        myDataName = findBooleanProperty(properties, "myDataName", true);
        myDataSize = findBooleanProperty(properties, "myDataSize", false);
        myDataOwner = findBooleanProperty(properties, "myDataOwner", false);
        myDataCreationDate = findBooleanProperty(properties, "myDataCreationDate", false);
        myDataLastTime = findBooleanProperty(properties, "myDataLastTime", false);
        myDataCombine = findBooleanProperty(properties, "myDataCombine", false);
        myDataNewScorecard = findBooleanProperty(properties, "myDataScorecard", true);
        myDataNewKPITree = findBooleanProperty(properties, "myDataKPITree", false);
        myDataNewDashboard = findBooleanProperty(properties, "myDataDashboard", true);
        myDataLookupTable = findBooleanProperty(properties, "myDataLookupTable", false);
        myDataAccountVisible = findBooleanProperty(properties, "myDataAccountVisible", false);
    }

    public ImageDescriptor getReportHeaderImage() {
        return reportHeaderImage;
    }

    public void setReportHeaderImage(ImageDescriptor reportHeaderImage) {
        this.reportHeaderImage = reportHeaderImage;
    }

    public boolean isReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(boolean reportHeader) {
        this.reportHeader = reportHeader;
    }

    public int getReportBackgroundColor() {
        return reportBackgroundColor;
    }

    public void setReportBackgroundColor(int reportBackgroundColor) {
        this.reportBackgroundColor = reportBackgroundColor;
    }

    public int getReportTextColor() {
        return reportTextColor;
    }

    public void setReportTextColor(int reportTextColor) {
        this.reportTextColor = reportTextColor;
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

    public boolean isCoreAppBackgroundImageEnabled() {
        return coreAppBackgroundImageEnabled;
    }

    public void setCoreAppBackgroundImageEnabled(boolean coreAppBackgroundImageEnabled) {
        this.coreAppBackgroundImageEnabled = coreAppBackgroundImageEnabled;
    }

    public boolean isCoreAppBackgroundColorEnabled() {
        return coreAppBackgroundColorEnabled;
    }

    public void setCoreAppBackgroundColorEnabled(boolean coreAppBackgroundColorEnabled) {
        this.coreAppBackgroundColorEnabled = coreAppBackgroundColorEnabled;
    }

    public boolean isCoreAppBackgroundSizeEnabled() {
        return coreAppBackgroundSizeEnabled;
    }

    public void setCoreAppBackgroundSizeEnabled(boolean coreAppBackgroundSizeEnabled) {
        this.coreAppBackgroundSizeEnabled = coreAppBackgroundSizeEnabled;
    }

    public boolean isHeaderBarBackgroundColorEnabled() {
        return headerBarBackgroundColorEnabled;
    }

    public void setHeaderBarBackgroundColorEnabled(boolean headerBarBackgroundColorEnabled) {
        this.headerBarBackgroundColorEnabled = headerBarBackgroundColorEnabled;
    }

    public boolean isHeaderBarDividerColorEnabled() {
        return headerBarDividerColorEnabled;
    }

    public void setHeaderBarDividerColorEnabled(boolean headerBarDividerColorEnabled) {
        this.headerBarDividerColorEnabled = headerBarDividerColorEnabled;
    }

    public boolean isCenterCanvasBackgroundColorEnabled() {
        return centerCanvasBackgroundColorEnabled;
    }

    public void setCenterCanvasBackgroundColorEnabled(boolean centerCanvasBackgroundColorEnabled) {
        this.centerCanvasBackgroundColorEnabled = centerCanvasBackgroundColorEnabled;
    }

    public boolean isCenterCanvasBackgroundAlphaEnabled() {
        return centerCanvasBackgroundAlphaEnabled;
    }

    public void setCenterCanvasBackgroundAlphaEnabled(boolean centerCanvasBackgroundAlphaEnabled) {
        this.centerCanvasBackgroundAlphaEnabled = centerCanvasBackgroundAlphaEnabled;
    }

    public boolean isReportBackgroundEnabled() {
        return reportBackgroundEnabled;
    }

    public void setReportBackgroundEnabled(boolean reportBackgroundEnabled) {
        this.reportBackgroundEnabled = reportBackgroundEnabled;
    }

    public boolean isReportBackgroundSizeEnabled() {
        return reportBackgroundSizeEnabled;
    }

    public void setReportBackgroundSizeEnabled(boolean reportBackgroundSizeEnabled) {
        this.reportBackgroundSizeEnabled = reportBackgroundSizeEnabled;
    }

    public boolean isMyDataName() {
        return myDataName;
    }

    public void setMyDataName(boolean myDataName) {
        this.myDataName = myDataName;
    }

    public boolean isMyDataSize() {
        return myDataSize;
    }

    public void setMyDataSize(boolean myDataSize) {
        this.myDataSize = myDataSize;
    }

    public boolean isMyDataOwner() {
        return myDataOwner;
    }

    public void setMyDataOwner(boolean myDataOwner) {
        this.myDataOwner = myDataOwner;
    }

    public boolean isMyDataCreationDate() {
        return myDataCreationDate;
    }

    public void setMyDataCreationDate(boolean myDataCreationDate) {
        this.myDataCreationDate = myDataCreationDate;
    }

    public boolean isMyDataLastTime() {
        return myDataLastTime;
    }

    public void setMyDataLastTime(boolean myDataLastTime) {
        this.myDataLastTime = myDataLastTime;
    }

    public boolean isMyDataCombine() {
        return myDataCombine;
    }

    public void setMyDataCombine(boolean myDataCombine) {
        this.myDataCombine = myDataCombine;
    }

    public boolean isMyDataNewScorecard() {
        return myDataNewScorecard;
    }

    public void setMyDataNewScorecard(boolean myDataNewScorecard) {
        this.myDataNewScorecard = myDataNewScorecard;
    }

    public boolean isMyDataAccountVisible() {
        return myDataAccountVisible;
    }

    public void setMyDataAccountVisible(boolean myDataAccountVisible) {
        this.myDataAccountVisible = myDataAccountVisible;
    }

    public boolean isMyDataNewKPITree() {
        return myDataNewKPITree;
    }

    public void setMyDataNewKPITree(boolean myDataNewKPITree) {
        this.myDataNewKPITree = myDataNewKPITree;
    }

    public boolean isMyDataNewDashboard() {
        return myDataNewDashboard;
    }

    public void setMyDataNewDashboard(boolean myDataNewDashboard) {
        this.myDataNewDashboard = myDataNewDashboard;
    }

    public boolean isMyDataLookupTable() {
        return myDataLookupTable;
    }

    public void setMyDataLookupTable(boolean myDataLookupTable) {
        this.myDataLookupTable = myDataLookupTable;
    }

    protected boolean propertyEnabled(List<ReportProperty> properties, String property) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                return reportProperty.isEnabled();
            }
        }
        return false;
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
