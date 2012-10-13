package com.easyinsight.export;

import com.easyinsight.analysis.AnalysisStorage;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.UserStub;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.security.SecurityUtil;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;
import org.hibernate.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jun 6, 2010
 * Time: 10:58:02 AM
 */
public class ReportDelivery extends ScheduledDelivery {

    public static final int EXCEL = 1;
    public static final int PNG = 2;
    public static final int PDF = 3;
    public static final int HTML_TABLE = 4;
    public static final int EXCEL_2007 = 5;

    private int reportFormat;
    private long reportID;
    private String reportName;
    private String subject;
    private String body;
    private boolean htmlEmail;
    private int timezoneOffset;
    private long senderID;
    private long dataSourceID;
    private String deliveryLabel;
    private boolean sendIfNoData;

    private List<FilterDefinition> customFilters;

    public boolean isSendIfNoData() {
        return sendIfNoData;
    }

    public void setSendIfNoData(boolean sendIfNoData) {
        this.sendIfNoData = sendIfNoData;
    }

    public String getDeliveryLabel() {
        return deliveryLabel;
    }

    public void setDeliveryLabel(String deliveryLabel) {
        this.deliveryLabel = deliveryLabel;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public List<FilterDefinition> getCustomFilters() {
        return customFilters;
    }

    public void setCustomFilters(List<FilterDefinition> customFilters) {
        this.customFilters = customFilters;
    }

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public boolean isHtmlEmail() {
        return htmlEmail;
    }

    public void setHtmlEmail(boolean htmlEmail) {
        this.htmlEmail = htmlEmail;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    public int getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(int reportFormat) {
        this.reportFormat = reportFormat;
    }

    @Override
    public int retrieveType() {
        return ScheduledActivity.REPORT_DELIVERY;
    }

    public void fromXML(Element root, XMLImportMetadata xmlImportMetadata) {
        reportID = Long.parseLong(root.getAttribute("reportID").getValue());
        xmlImportMetadata.setAdditionalReportItems(new AnalysisStorage().getAnalysisDefinition(reportID).getAddedItems());
        subject = xmlImportMetadata.getValue(root, "subject/text()");
        deliveryLabel = xmlImportMetadata.getValue(root, "deliveryLabel/text()");
        body = xmlImportMetadata.getValue(root, "body/text()");
        reportFormat = Integer.parseInt(root.getAttribute("reportFormat").getValue());
        timezoneOffset = Integer.parseInt(root.getAttribute("timezoneOffset").getValue());
        htmlEmail = Boolean.parseBoolean(root.getAttribute("htmlEmail").getValue());
        sendIfNoData = Boolean.parseBoolean(root.getAttribute("sendIfNoData").getValue());
        Nodes filterNodes = root.query("filters/filter");
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        for (int i = 0; i < filterNodes.size(); i++) {
            Element filterNode = (Element) filterNodes.get(i);
            filters.add(FilterDefinition.fromXML(filterNode, xmlImportMetadata));
        }
        customFilters = filters;
        Nodes userStubNodes = root.query("userStubs/userStub/text()");
        for (int i = 0; i < userStubNodes.size(); i++) {
            long userID = Long.parseLong(userStubNodes.get(i).getValue());
            UserStub userStub = new UserStub();
            userStub.setUserID(userID);
            getUsers().add(userStub);
        }
        Nodes emailNodes = root.query("emails/email/text()");
        for (int i = 0; i < emailNodes.size(); i++) {
            String email = emailNodes.get(i).getValue();
            getEmails().add(email);
        }
        setScheduleType(ScheduleType.fromXML((Element) root.query("scheduleType").get(0)));
    }

    public Element toXML(XMLMetadata xmlMetadata) {
        Element root = new Element("reportDelivery");
        root.addAttribute(new Attribute("dataSourceID", String.valueOf(dataSourceID)));
        root.appendChild(getScheduleType().toXML());
        root.addAttribute(new Attribute("reportID", String.valueOf(reportID)));
        root.addAttribute(new Attribute("subject", subject));
        Element body = new Element("body");
        body.appendChild(this.body);
        root.appendChild(body);
        Element subject = new Element("subject");
        subject.appendChild(this.subject);
        root.appendChild(subject);
        Element deliveryLabel = new Element("deliveryLabel");
        deliveryLabel.appendChild(this.deliveryLabel);
        root.appendChild(deliveryLabel);
        root.addAttribute(new Attribute("reportFormat", String.valueOf(reportFormat)));
        root.addAttribute(new Attribute("timezoneOffset", String.valueOf(timezoneOffset)));
        root.addAttribute(new Attribute("htmlEmail", String.valueOf(htmlEmail)));
        root.addAttribute(new Attribute("sendIfNoData", String.valueOf(sendIfNoData)));
        Element filters = new Element("filters");
        root.appendChild(filters);
        for (FilterDefinition filterDefinition : getCustomFilters()) {
            filters.appendChild(filterDefinition.toXML(xmlMetadata));
        }
        Element userStubs = new Element("userStubs");
        for (UserStub userStub : getUsers()) {
            Element stub = new Element("userStub");
            stub.appendChild(String.valueOf(userStub.getUserID()));
            userStubs.appendChild(stub);
        }
        root.appendChild(userStubs);
        Element emails = new Element("emails");
        for (String emailAddress : getEmails()) {
            Element email = new Element("email");
            email.appendChild(emailAddress);
            emails.appendChild(email);
        }
        root.appendChild(emails);
        return root;
    }

    protected void customSave(EIConnection conn, int utcOffset) throws SQLException {
        super.customSave(conn, utcOffset);
        setTimezoneOffset(utcOffset);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM REPORT_DELIVERY WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        clearStmt.setLong(1, getScheduledActivityID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO REPORT_DELIVERY (REPORT_ID, delivery_format, subject, body, " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID, html_email, timezone_offset, sender_user_id, delivery_label, send_if_no_data) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, reportID);
        insertStmt.setInt(2, reportFormat);
        insertStmt.setString(3, subject);
        insertStmt.setString(4, body);
        insertStmt.setLong(5, getScheduledActivityID());
        insertStmt.setBoolean(6, htmlEmail);
        insertStmt.setInt(7, timezoneOffset);
        if (senderID > 0) {
            insertStmt.setLong(8, senderID);
        } else {
            insertStmt.setNull(8, Types.BIGINT);
        }
        insertStmt.setString(9, deliveryLabel);
        insertStmt.setBoolean(10, sendIfNoData);
        insertStmt.execute();
        long deliveryID = Database.instance().getAutoGenKey(insertStmt);
        insertStmt.close();
        PreparedStatement insertFilterStmt = conn.prepareStatement("INSERT INTO DELIVERY_TO_FILTER_DEFINITION (REPORT_DELIVERY_ID, FILTER_ID) VALUES (?, ?)");
        Session session = Database.instance().createSession(conn);
        try {
            for (FilterDefinition customFilter : customFilters) {
                customFilter.beforeSave(session);
                session.saveOrUpdate(customFilter);
                session.flush();
                insertFilterStmt.setLong(1, deliveryID);
                insertFilterStmt.setLong(2, customFilter.getFilterID());
                insertFilterStmt.execute();
            }
        } finally {
            session.close();
        }
        insertFilterStmt.close();
    }

    protected void customLoad(EIConnection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DELIVERY_FORMAT, REPORT_ID, SUBJECT, BODY, HTML_EMAIL, ANALYSIS.TITLE, " +
                "timezone_offset, SENDER_USER_ID, REPORT_DELIVERY_ID, ANALYSIS.DATA_FEED_ID, DELIVERY_LABEL, SEND_IF_NO_DATA FROM " +
                "REPORT_DELIVERY, ANALYSIS WHERE " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID = ? AND REPORT_DELIVERY.REPORT_ID = ANALYSIS.ANALYSIS_ID");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            reportFormat = rs.getInt(1);
            reportID = rs.getLong(2);
            subject = rs.getString(3);
            body = rs.getString(4);
            htmlEmail = rs.getBoolean(5);
            reportName = rs.getString(6);
            timezoneOffset = rs.getInt(7);
            long senderID = rs.getLong(8);
            if (!rs.wasNull()) {
                this.senderID = senderID;
            }
            customFilters = new ArrayList<FilterDefinition>();
            long reportDeliveryID = rs.getLong(9);
            dataSourceID = rs.getLong(10);
            deliveryLabel = rs.getString(11);
            sendIfNoData = rs.getBoolean(12);
            Session session = Database.instance().createSession(conn);
            try {
                PreparedStatement filterStmt = conn.prepareStatement("SELECT FILTER_ID FROM DELIVERY_TO_FILTER_DEFINITION WHERE REPORT_DELIVERY_ID = ?");
                filterStmt.setLong(1, reportDeliveryID);
                ResultSet filterRS = filterStmt.executeQuery();
                while (filterRS.next()) {
                    long filterID = filterRS.getLong(1);
                    List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
                    if (results.size() > 0) {
                        FilterDefinition filter = (FilterDefinition) results.get(0);
                        filter.afterLoad();
                        customFilters.add(filter);
                    }
                }
                filterStmt.close();
            }  finally {
                session.close();
            }
            queryStmt.close();
        } else {
            queryStmt.close();
            throw new RuntimeException("Orphan activity");
        }
    }

    @Override
    public boolean authorize() {
        try {
            SecurityUtil.authorizeInsight(reportID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void taskNow(EIConnection connection) throws Exception {
        DeliveryScheduledTask deliveryScheduledTask = new DeliveryScheduledTask();
        deliveryScheduledTask.setActivityID(getScheduledActivityID());
        deliveryScheduledTask.execute(new Date(), connection);
    }

    @Override
    public void setup(EIConnection conn) throws SQLException {
        // nuke the existing generator
        PreparedStatement queryStmt = conn.prepareStatement("SELECT TASK_GENERATOR_ID FROM delivery_task_generator where SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        queryStmt.setLong(1, getScheduledActivityID());
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TASK_GENERATOR WHERE TASK_GENERATOR_ID = ?");
            deleteStmt.setLong(1, rs.getLong(1));
            deleteStmt.executeUpdate();
            deleteStmt.close();
            PreparedStatement arghStmt = conn.prepareStatement("DELETE FROM delivery_task_generator WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
            arghStmt.setLong(1, getScheduledActivityID());
            arghStmt.executeUpdate();
            arghStmt.close();
        }
        queryStmt.close();
        Session session = Database.instance().createSession(conn);
        try {
            DeliveryTaskGenerator generator = new DeliveryTaskGenerator();
            generator.setStartTaskDate(new Date());
            generator.setActivityID(getScheduledActivityID());
            generator.setTaskInterval(24 * 1000 * 60 * 60);
            session.save(generator);
            session.flush();
        } finally {
            session.close();
        }
    }
}
