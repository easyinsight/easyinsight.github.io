package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterHTMLMetadata;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.preferences.ImageDescriptor;
import com.easyinsight.scorecard.Scorecard;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:25:06 PM
 */
public abstract class DashboardElement implements Cloneable {

    public static final int GRID = 1;
    public static final int REPORT = 2;
    public static final int STACK = 3;
    public static final int IMAGE = 4;
    public static final int SCORECARD = 5;
    public static final int TEXT = 6;

    private long elementID;
    private String label;
    private List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
    private String filterBorderStyle = "solid";
    private int filterBorderColor;
    private int filterBackgroundColor;
    private double filterBackgroundAlpha;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;
    private ImageDescriptor headerBackground;
    private int headerBackgroundColor;
    private double headerBackgroundAlpha;

    private DashboardElement parentElement;

    private int preferredWidth;
    private int preferredHeight;

    public DashboardElement getParentElement() {
        return parentElement;
    }

    public void setParentElement(DashboardElement parentElement) {
        this.parentElement = parentElement;
    }

    public String toHTML(FilterHTMLMetadata filterHTMLMetadata) {
        return "";
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public void setPreferredWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth;
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }

    public void setPreferredHeight(int preferredHeight) {
        this.preferredHeight = preferredHeight;
    }

    public double getFilterBackgroundAlpha() {
        return filterBackgroundAlpha;
    }

    public void setFilterBackgroundAlpha(double filterBackgroundAlpha) {
        this.filterBackgroundAlpha = filterBackgroundAlpha;
    }

    public ImageDescriptor getHeaderBackground() {
        return headerBackground;
    }

    public void setHeaderBackground(ImageDescriptor headerBackground) {
        this.headerBackground = headerBackground;
    }

    public int getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(int headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }

    public double getHeaderBackgroundAlpha() {
        return headerBackgroundAlpha;
    }

    public void setHeaderBackgroundAlpha(double headerBackgroundAlpha) {
        this.headerBackgroundAlpha = headerBackgroundAlpha;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public String getFilterBorderStyle() {
        return filterBorderStyle;
    }

    public void setFilterBorderStyle(String filterBorderStyle) {
        this.filterBorderStyle = filterBorderStyle;
    }

    public int getFilterBorderColor() {
        return filterBorderColor;
    }

    public void setFilterBorderColor(int filterBorderColor) {
        this.filterBorderColor = filterBorderColor;
    }

    public int getFilterBackgroundColor() {
        return filterBackgroundColor;
    }

    public void setFilterBackgroundColor(int filterBackgroundColor) {
        this.filterBackgroundColor = filterBackgroundColor;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DashboardElement clone() throws CloneNotSupportedException {
        DashboardElement dashboardElement = (DashboardElement) super.clone();
        dashboardElement.setElementID(0);
        return dashboardElement;
    }

    public long getElementID() {
        return elementID;
    }

    public void setElementID(long elementID) {
        this.elementID = elementID;
    }

    public abstract int getType();

    public void loadElement(long elementID, EIConnection conn) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT LABEL, FILTER_BORDER_STYLE, FILTER_BORDER_COLOR, filter_background_color, filter_background_alpha," +
                "padding_left, padding_right, padding_top, padding_bottom, header_image_id, user_image.image_name, header_background_color, header_background_alpha," +
                "preferred_width, preferred_height, dashboard_element_id from dashboard_element " +
                "left join user_image on dashboard_element.header_image_id = user_image.user_image_id where " +
                "dashboard_element_id = ?");
        loadStmt.setLong(1, elementID);
        ResultSet rs = loadStmt.executeQuery();
        rs.next();
        int i = 1;
        setLabel(rs.getString(i++));
        String borderStyle = rs.getString(i++);
        if (borderStyle == null) {
            borderStyle = "solid";
        }
        setFilterBorderStyle(borderStyle);
        setFilterBorderColor(rs.getInt(i++));
        setFilterBackgroundColor(rs.getInt(i++));
        setFilterBackgroundAlpha(rs.getDouble(i++));
        setPaddingLeft(rs.getInt(i++));
        setPaddingRight(rs.getInt(i++));
        setPaddingTop(rs.getInt(i++));
        setPaddingBottom(rs.getInt(i++));
        long headerImageID = rs.getLong(i++);
        String headerImageName = rs.getString(i++);
        if (!rs.wasNull()) {
            ImageDescriptor imageDescriptor = new ImageDescriptor();
            imageDescriptor.setId(headerImageID);
            imageDescriptor.setName(headerImageName);
            setHeaderBackground(imageDescriptor);
        }
        setHeaderBackgroundColor(rs.getInt(i++));
        setHeaderBackgroundAlpha(rs.getDouble(i++));
        setPreferredWidth(rs.getInt(i++));
        setPreferredHeight(rs.getInt(i++));
        setElementID(rs.getLong(i));
        loadStmt.close();
    }

    public long save(EIConnection conn) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_ELEMENT (ELEMENT_TYPE, LABEL, filter_border_style, filter_border_color," +
                "filter_background_color, filter_background_alpha, padding_left, padding_right, padding_top, padding_bottom, header_image_id," +
                "header_background_color, header_background_alpha, preferred_width, preferred_height) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
        int i = 1;
        insertStmt.setInt(i++, getType());
        insertStmt.setString(i++, label);
        insertStmt.setString(i++, filterBorderStyle);
        insertStmt.setInt(i++, filterBorderColor);
        insertStmt.setInt(i++, filterBackgroundColor);
        insertStmt.setDouble(i++, filterBackgroundAlpha);
        insertStmt.setInt(i++, paddingLeft);
        insertStmt.setInt(i++, paddingRight);
        insertStmt.setInt(i++, paddingTop);
        insertStmt.setInt(i++, paddingBottom);
        if (headerBackground == null) {
            insertStmt.setNull(i++, Types.BIGINT);
        } else {
            insertStmt.setLong(i++, headerBackground.getId());
        }
        insertStmt.setInt(i++, headerBackgroundColor);
        insertStmt.setDouble(i++, headerBackgroundAlpha);
        insertStmt.setInt(i++, preferredWidth);
        insertStmt.setInt(i, preferredHeight);
        insertStmt.execute();
        setElementID(Database.instance().getAutoGenKey(insertStmt));
        insertStmt.close();

        Session session = Database.instance().createSession(conn);
        try {
            for (FilterDefinition filterDefinition : getFilters()) {
                filterDefinition.beforeSave(session);
                session.saveOrUpdate(filterDefinition);
            }
            session.flush();
        } finally {
            session.close();
        }

        PreparedStatement filterStmt = conn.prepareStatement("INSERT INTO dashboard_element_to_filter (dashboard_element_id, FILTER_ID) VALUES (?, ?)");
        for (FilterDefinition filterDefinition : getFilters()) {
            filterStmt.setLong(1, getElementID());
            filterStmt.setLong(2, filterDefinition.getFilterID());
            filterStmt.execute();
        }
        filterStmt.close();
        return elementID;
    }

    public abstract Set<Long> containedReports();
    public abstract void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap);

    public abstract Set<Long> containedScorecards();
    public abstract void updateScorecardIDs(Map<Long, Scorecard> scorecardReplacementMap);

    public abstract void visit(IDashboardVisitor dashboardVisitor);

    public List<EIDescriptor> allItems(List<AnalysisItem> dataSourceItems) {
        return new ArrayList<EIDescriptor>();
    }

    public String refreshFunction() {
        return "";
    }

    public void populateFilters(List<FilterDefinition> parentFilters) {
        if (getParentElement() != null) {
            getParentElement().populateFilters(parentFilters);
        }
    }

    public Collection<? extends FilterDefinition> filtersForReport(long reportID) {
        return null;
    }

    public List<String> jsIncludes() {
        return new ArrayList<String>();
    }

    public List<String> cssIncludes() {
        return new ArrayList<String>();
    }
}
