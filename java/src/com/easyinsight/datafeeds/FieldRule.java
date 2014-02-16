package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
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

    // we have the field rules
    // copying the cached addons
    // copying report level calculations
    // "calculation" report

    private Link link;
    private ReportFieldExtension extension;

    private AnalysisItemHandle explicitField;
    private Tag tag;
    private int type;
    private boolean all;

    public static List<FieldRule> load(EIConnection conn, long dataSourceID) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT FIELD_RULE_ID, FIELD_TYPE, ALL_FIELDS, LINK_ID, EXTENSION_ID, TAG_ID, DISPLAY_NAME FROM FIELD_RULE WHERE DATA_SOURCE_ID = ? ORDER BY FIELD_ORDER ASC");
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
            rules.add(fieldRule);
        }
        return rules;
    }

    public void save(EIConnection conn, long dataSourceID, int order) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO FIELD_RULE (DATA_SOURCE_ID, FIELD_TYPE, ALL_FIELDS, LINK_ID, FIELD_ORDER, " +
                "EXTENSION_ID, TAG_ID, DISPLAY_NAME) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
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
        }
        return false;
    }

    public void update(AnalysisItem analysisItem, WSAnalysisDefinition report) {
        try {
            if (link != null) {
                if (analysisItem.getLinks() != null && analysisItem.getLinks().size() > 0 && !(analysisItem.getLinks().get(0)).isDefinedByRule() &&
                        !(analysisItem.getLinks().get(0)).isCodeGenerated()) {
                    // already has a link
                } else {
                    Link clonedLink = link.clone();
                    clonedLink.setDefinedByRule(true);
                    analysisItem.setLinks(Arrays.asList(clonedLink));
                }
            }
            if (extension != null) {
                if (analysisItem.getReportFieldExtension() != null && analysisItem.getReportFieldExtension().getFromFieldRuleID() == 0) {
                    // already has an extension defined
                } else {
                    ReportFieldExtension extension = this.extension;
                    if (report.extensionType() == extension.extensionType()) {
                        ReportFieldExtension clone = extension.clone();
                        clone.setFromFieldRuleID(1);
                        analysisItem.setReportFieldExtension(clone);
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
