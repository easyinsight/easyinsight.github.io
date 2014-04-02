package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.tag.Tag;
import org.hibernate.Session;

import java.sql.*;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/29/14
 * Time: 1:28 PM
 */
public class FieldRule {
    public static final int TAG_IN = 1;
    public static final int FIELD_IN = 2;

    private Link link;
    private ReportFieldExtension extension;
    private long fieldRuleID;

    private AnalysisItemHandle explicitField;
    private Tag tag;
    private int type;
    private boolean all;
    private long dataSourceID;
    private String dataSourceName;
    private String drillthroughName;
    private String defaultDate;

    public long getFieldRuleID() {
        return fieldRuleID;
    }

    public void setFieldRuleID(long fieldRuleID) {
        this.fieldRuleID = fieldRuleID;
    }

    public String getDrillthroughName() {
        return drillthroughName;
    }

    public void setDrillthroughName(String drillthroughName) {
        this.drillthroughName = drillthroughName;
    }

    public String getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(String defaultDate) {
        this.defaultDate = defaultDate;
    }

    public static List<FieldRule> load(EIConnection conn, long dataSourceID) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT FIELD_RULE_ID, FIELD_TYPE, ALL_FIELDS, LINK_ID, EXTENSION_ID, TAG_ID, DISPLAY_NAME, RULE_DATA_SOURCE_ID, DEFAULT_DATE FROM FIELD_RULE WHERE DATA_SOURCE_ID = ? ORDER BY FIELD_ORDER ASC");
        PreparedStatement getTagStmt = conn.prepareStatement("SELECT TAG_NAME FROM ACCOUNT_TAG WHERE ACCOUNT_TAG_ID = ?");
        loadStmt.setLong(1, dataSourceID);
        ResultSet rs = loadStmt.executeQuery();
        List<FieldRule> rules = new ArrayList<FieldRule>();
        while (rs.next()) {
            //long id = rs.getLong(1);
            int type = rs.getInt(2);
            boolean all = rs.getBoolean(3);
            long linkID = rs.getLong(4);
            long extensionID = rs.getLong(5);
            long tagID = rs.getLong(6);
            String displayName = rs.getString(7);
            FieldRule fieldRule = new FieldRule();
            fieldRule.setType(type);
            fieldRule.setAll(all);
            if (linkID > 0) {
                // load link
                Session session = Database.instance().createSession(conn);
                try {
                    Link link = (Link) session.createQuery("from Link where linkID = ?").setLong(0, linkID).list().get(0);
                    link.afterLoad();
                    fieldRule.setLink(link);
                } finally {
                    session.close();
                }
                if (fieldRule.getLink() != null && fieldRule.getLink() instanceof DrillThrough) {
                    DrillThrough drillThrough = (DrillThrough) fieldRule.getLink();
                    if (drillThrough.getReportID() != null && drillThrough.getReportID() > 0) {
                        PreparedStatement nameStmt = conn.prepareStatement("SELECT TITLE FROM ANALYSIS WHERE ANALYSIS_ID = ?");
                        nameStmt.setLong(1, drillThrough.getReportID());
                        ResultSet nameRS = nameStmt.executeQuery();
                        if (nameRS.next()) {
                            fieldRule.setDrillthroughName(nameRS.getString(1));
                        }
                        nameStmt.close();
                    } else if (drillThrough.getDashboardID() != null && drillThrough.getDashboardID() > 0) {
                        PreparedStatement nameStmt = conn.prepareStatement("SELECT DASHBOARD_NAME FROM DASHBOARD WHERE DASHBOARD_ID = ?");
                        nameStmt.setLong(1, drillThrough.getDashboardID());
                        ResultSet nameRS = nameStmt.executeQuery();
                        if (nameRS.next()) {
                            fieldRule.setDrillthroughName(nameRS.getString(1));
                        }
                        nameStmt.close();
                    }
                }
            }
            if (extensionID > 0) {
                Session session = Database.instance().createSession(conn);
                try {
                    ReportFieldExtension extension = (ReportFieldExtension) session.createQuery("from ReportFieldExtension where reportFieldExtensionID = ?").setLong(0, extensionID).list().get(0);
                    extension.afterLoad();
                    fieldRule.setExtension(extension);
                } finally {
                    session.close();
                }
            }
            if (displayName != null) {
                AnalysisItemHandle handle = new AnalysisItemHandle();
                handle.setName(displayName);
                fieldRule.setExplicitField(handle);
            }
            if (tagID > 0) {
                getTagStmt.setLong(1, tagID);
                ResultSet tagRS = getTagStmt.executeQuery();
                tagRS.next();
                String tagName = tagRS.getString(1);
                fieldRule.setTag(new Tag(tagID, tagName, false, false, false));
            }
            long dataSourceRuleID = rs.getLong(8);
            if (!rs.wasNull()) {
                PreparedStatement nameStmt = conn.prepareStatement("SELECT FEED_NAME FROM DATA_FEED WHERE DATA_FEED_ID = ?");
                nameStmt.setLong(1, dataSourceRuleID);
                nameStmt.executeQuery();
                ResultSet nameRS = nameStmt.executeQuery();
                if (nameRS.next()) {
                    fieldRule.setDataSourceName(nameRS.getString(1));
                    fieldRule.setDataSourceID(dataSourceRuleID);
                }
                nameStmt.close();
            }
            fieldRule.setDefaultDate(rs.getString(9));
            rules.add(fieldRule);
        }
        loadStmt.close();
        getTagStmt.close();
        return rules;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public void save(EIConnection conn, long dataSourceID, int order) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO FIELD_RULE (DATA_SOURCE_ID, FIELD_TYPE, ALL_FIELDS, LINK_ID, FIELD_ORDER, " +
                "EXTENSION_ID, TAG_ID, DISPLAY_NAME, rule_data_source_id, default_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, dataSourceID);
        ps.setInt(2, type);
        ps.setBoolean(3, all);
        if (link == null) {
            ps.setNull(4, Types.BIGINT);
        } else {
            Session session = Database.instance().createSession(conn);
            try {
                link.beforeSave(session);
                session.saveOrUpdate(link);
                session.flush();
            } finally {
                session.close();
            }
            ps.setLong(4, link.getLinkID());
        }
        ps.setInt(5, order);
        if (extension == null) {
            ps.setNull(6, Types.BIGINT);
        } else {
            Session session = Database.instance().createSession(conn);
            try {
                extension.reportSave(session);
                session.saveOrUpdate(extension);
                session.flush();
            } finally {
                session.close();
            }
            ps.setLong(6, extension.getReportFieldExtensionID());
        }
        if (tag != null) {
            ps.setLong(7, tag.getId());
        } else {
            ps.setNull(7, Types.BIGINT);
        }
        if (explicitField != null) {
            ps.setString(8, explicitField.getName());
        } else {
            ps.setNull(8, Types.VARCHAR);
        }
        if (dataSourceID > 0) {
            ps.setLong(9, getDataSourceID());
        } else {
            ps.setNull(9, Types.BIGINT);
        }
        ps.setString(10, getDefaultDate());
        ps.execute();
        ps.close();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public ReportFieldExtension getExtension() {
        return extension;
    }

    public void setExtension(ReportFieldExtension extension) {
        this.extension = extension;
    }

    public AnalysisItemHandle getExplicitField() {
        return explicitField;
    }

    public void setExplicitField(AnalysisItemHandle explicitField) {
        this.explicitField = explicitField;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public boolean matches(AnalysisItem analysisItem) {
        if (getTag() != null) {
            Tag tag = getTag();
            if (analysisItem.getTags() != null) {
                for (Tag test : analysisItem.getTags()) {
                    if (tag.getId() == test.getId()) {
                        return true;
                    }
                }
            }
        } else if (getExplicitField() != null) {
            AnalysisItemHandle field = getExplicitField();
            if (field.getName().equals(analysisItem.toDisplay()) || field.getName().equals(analysisItem.toOriginalDisplayName())) {
                return true;
            }
        } else if (getType() > 0 && analysisItem.hasType(getType())) {
            return true;
        } else if (getDataSourceID() > 0) {
            if (analysisItem.getKey() instanceof DerivedKey) {
                DerivedKey derivedKey = (DerivedKey) analysisItem.getKey();
                if (derivedKey.getFeedID() == getDataSourceID()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void update(AnalysisItem analysisItem, WSAnalysisDefinition report, InsightRequestMetadata insightRequestMetadata) {
        try {
            if (link != null) {
                if (analysisItem.getLinks() != null && analysisItem.getLinks().size() > 0 && !(analysisItem.getLinks().get(0)).isDefinedByRule() &&
                        !(analysisItem.getLinks().get(0)).isCodeGenerated()) {
                    // already has a link
                } else {
                    if (link instanceof DrillThrough) {
                        DrillThrough drillThrough = (DrillThrough) link;
                        if (drillThrough.getReportID() != null && drillThrough.getReportID() == report.getAnalysisID()) {
                            return;
                        }
                    }
                    Link clonedLink = link.clone();
                    clonedLink.setDefinedByRule(true);
                    List<Link> list = new ArrayList<Link>();
                    list.add(clonedLink);
                    analysisItem.setLinks(list);
                    insightRequestMetadata.addAudit(analysisItem, "Field rule added link.");
                }
            }
            if (extension != null) {
                if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension().getFromFieldRuleID() == 0 &&
                        analysisItem.getReportFieldExtension().extensionType() == report.extensionType()) {
                    // already has an extension defined
                } else {
                    ReportFieldExtension extension = this.extension;
                    if (report.extensionType() == extension.extensionType()) {
                        ReportFieldExtension clone = extension.clone();
                        clone.setFromFieldRuleID(1);
                        analysisItem.setReportFieldExtension(clone);
                        insightRequestMetadata.addAudit(analysisItem, "Field rule added field extension.");
                    }
                }
            }
            if (defaultDate != null && !"".equals(defaultDate)) {
                analysisItem.setDefaultDate(defaultDate);
                insightRequestMetadata.addAudit(analysisItem, "Field rule set default date to " + defaultDate);
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
