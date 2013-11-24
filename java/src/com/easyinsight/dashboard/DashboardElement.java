package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterHTMLMetadata;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.preferences.ImageDescriptor;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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
public abstract class DashboardElement implements Cloneable, Serializable {

    public static final int GRID = 1;
    public static final int REPORT = 2;
    public static final int STACK = 3;
    public static final int IMAGE = 4;
    public static final int SCORECARD = 5;
    public static final int TEXT = 6;

    private long elementID;
    private String urlKey;
    private long elementServerID;
    private boolean forceScrollingOff;
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
    private List<DashboardFilterOverride> dashboardFilterOverrides = new ArrayList<DashboardFilterOverride>();

    private Map<String, FilterDefinition> overridenFilters;

    private DashboardElement parentElement;

    private int preferredWidth;
    private int preferredHeight;

    private int htmlWidth;

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public List<DashboardFilterOverride> getDashboardFilterOverrides() {
        return dashboardFilterOverrides;
    }

    public void setDashboardFilterOverrides(List<DashboardFilterOverride> dashboardFilterOverrides) {
        this.dashboardFilterOverrides = dashboardFilterOverrides;
    }

    public Map<String, FilterDefinition> getOverridenFilters() {
        return overridenFilters;
    }

    public void setOverridenFilters(Map<String, FilterDefinition> overridenFilters) {
        this.overridenFilters = overridenFilters;
    }

    public JSONObject toJSON(FilterHTMLMetadata filterHTMLMetadata, List<FilterDefinition> parentFilters) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", getUrlKey());
        if(getPreferredWidth() > 0)
            jo.put("preferredWidth", getPreferredWidth());
        else if (getHtmlWidth() > 0)
            jo.put("preferredWidth", getHtmlWidth());
        if(getPreferredHeight() > 0)
            jo.put("preferredHeight", getPreferredHeight());

        JSONArray filters = new JSONArray();

        if (getOverridenFilters() != null) {
            for (FilterDefinition filter : getFilters()) {
                FilterDefinition overrideFilter = getOverridenFilters().get(String.valueOf(filter.getFilterID()));
                if (overrideFilter != null) {
                    filter.override(overrideFilter);
                    filter.setEnabled(overrideFilter.isEnabled());
                }
            }
        }
        for(FilterDefinition f : getFilters()) {
            boolean found = false;
            for(FilterDefinition ff : parentFilters) {
                if(f.sameFilter(ff)) {
                    found = true;
                }
            }
            if(!found)
                filters.put(f.toJSON(filterHTMLMetadata));
        }


        JSONArray overrides = new JSONArray();
        for(DashboardFilterOverride fo : getDashboardFilterOverrides()) {
            if(fo.isHideFilter()) {
                overrides.put(fo.getFilterID());
            }
        }
        jo.put("overrides", overrides);
        jo.put("filters", filters);
        return jo;
    }

    public int getHtmlWidth() {
        return htmlWidth;
    }

    public void setHtmlWidth(int htmlWidth) {
        this.htmlWidth = htmlWidth;
    }

    public long getElementServerID() {
        return elementServerID;
    }

    public void setElementServerID(long elementServerID) {
        this.elementServerID = elementServerID;
    }

    public boolean isForceScrollingOff() {
        return forceScrollingOff;
    }

    public void setForceScrollingOff(boolean forceScrollingOff) {
        this.forceScrollingOff = forceScrollingOff;
    }

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
                "preferred_width, preferred_height, dashboard_element_id, force_scrolling_off, html_width, url_key from dashboard_element " +
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
        setElementID(rs.getLong(i++));
        setElementServerID(getElementID());
        setForceScrollingOff(rs.getBoolean(i++));
        setHtmlWidth(rs.getInt(i++));
        setUrlKey(rs.getString(i));
        if (getUrlKey() == null || "".equals(getUrlKey())) {
            setUrlKey(RandomTextGenerator.generateText(40));
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD_ELEMENT SET URL_KEY = ? WHERE DASHBOARD_ELEMENT_ID = ?");
            updateStmt.setString(1, getUrlKey());
            updateStmt.setLong(2, elementID);
            updateStmt.executeUpdate();
            updateStmt.close();
        }
        PreparedStatement filterStmt = conn.prepareStatement("SELECT FILTER_ID, HIDE_FILTER FROM dashboard_element_filter_setting WHERE DASHBOARD_ELEMENT_ID = ?");
        filterStmt.setLong(1, elementID);
        ResultSet filterRS = filterStmt.executeQuery();
        List<DashboardFilterOverride> overrides = new ArrayList<DashboardFilterOverride>();
        while (filterRS.next()) {
            long filterID = filterRS.getLong(1);
            boolean hideFilter = filterRS.getBoolean(2);
            overrides.add(new DashboardFilterOverride(filterID, hideFilter));
        }
        setDashboardFilterOverrides(overrides);
        filterStmt.close();
        loadStmt.close();
    }

    public long save(EIConnection conn) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_ELEMENT (ELEMENT_TYPE, LABEL, filter_border_style, filter_border_color," +
                "filter_background_color, filter_background_alpha, padding_left, padding_right, padding_top, padding_bottom, header_image_id," +
                "header_background_color, header_background_alpha, preferred_width, preferred_height, force_scrolling_off, html_width, url_key) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
        insertStmt.setInt(i++, preferredHeight);
        insertStmt.setBoolean(i++, forceScrollingOff);
        insertStmt.setInt(i++, htmlWidth);
        if (urlKey == null) {
            urlKey = RandomTextGenerator.generateText(40);
        }
        insertStmt.setString(i, urlKey);
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

        if (getDashboardFilterOverrides() != null) {
            PreparedStatement filterOverrideStmt = conn.prepareStatement("INSERT INTO dashboard_element_filter_setting (filter_id, hide_filter, dashboard_element_id) values (?, ?, ?)");
            for (DashboardFilterOverride override : getDashboardFilterOverrides()) {
                long filterID;
                if (override.getFilterDefinition() != null) {
                    filterID = override.getFilterDefinition().getFilterID();
                } else {
                    filterID = override.getFilterID();
                }
                if (filterID == 0) {
                    continue;
                }
                filterOverrideStmt.setLong(1, filterID);
                filterOverrideStmt.setBoolean(2, override.isHideFilter());
                filterOverrideStmt.setLong(3, getElementID());
                filterOverrideStmt.execute();
            }
        }

        return elementID;
    }

    public abstract Set<Long> containedReports();
    public abstract void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap, List<AnalysisItem> allFields, boolean changingDataSource, FeedDefinition dataSource);

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
        return new ArrayList<FilterDefinition>();
    }

    public List<String> jsIncludes() {
        return new ArrayList<String>();
    }

    public List<String> cssIncludes() {
        return new ArrayList<String>();
    }

    public Collection<? extends FilterDefinition> filtersToRender() {
        return new ArrayList<FilterDefinition>();
    }

    public DashboardUIProperties findHeaderImage() {
        return null;
    }

    public DashboardElement findElement(long dashboardElementID) {
        if (getElementID() == dashboardElementID) {
            return this;
        }
        return null;
    }

    public int requiredInitCount() {
        return 0;
    }
}
