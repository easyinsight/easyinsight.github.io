package com.easyinsight.export;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSMultiSummaryDefinition;
import com.easyinsight.benchmark.BenchmarkManager;
import com.easyinsight.benchmark.ScheduledTaskBenchmarkInfo;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.dashboard.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.email.UserStub;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.logging.LogClass;
import com.easyinsight.preferences.PreferencesService;
import com.easyinsight.preferences.UserDLS;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.itextpdf.text.DocumentException;
import com.xerox.amazonws.common.Result;
import com.xerox.amazonws.sns.NotificationService;
import com.xerox.amazonws.sns.SNSException;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSException;
import com.xerox.amazonws.sqs2.SQSUtils;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.jetbrains.annotations.Nullable;

import javax.mail.MessagingException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.*;

/**
 * User: jamesboe
 * Date: Jun 6, 2010
 * Time: 1:49:58 PM
 */
@Entity
@Table(name = "delivery_scheduled_task")
@PrimaryKeyJoinColumn(name = "scheduled_task_id")
public class DeliveryScheduledTask extends ScheduledTask {

    private static final String QUEUE_POLICY_FORMAT = "{\n" +
            "  \"Version\": \"2008-10-17\",\n" +
            "  \"Id\": \"arn:aws:sqs:us-east-1:808335860417:{0}/SQSDefaultPolicy\",\n" +
            "  \"Statement\": [\n" +
            "    {\n" +
            "      \"Sid\": \"Sid1363034700583\",\n" +
            "      \"Effect\": \"Allow\",\n" +
            "      \"Principal\": {\n" +
            "        \"AWS\": \"*\"\n" +
            "      },\n" +
            "      \"Action\": \"SQS:SendMessage\",\n" +
            "      \"Resource\": \"arn:aws:sqs:us-east-1:808335860417:{0}\",\n" +
            "      \"Condition\": {\n" +
            "        \"ArnEquals\": {\n" +
            "          \"aws:SourceArn\": \"arn:aws:sns:us-east-1:808335860417:{1}\"\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    @Column(name = "scheduled_account_activity_id")
    private long activityID;

    public long getActivityID() {
        return activityID;
    }

    public void setActivityID(long activityID) {
        this.activityID = activityID;
    }

    protected void execute(Date now, EIConnection conn) throws Exception {

        // identify the type of activity

        ScheduledTaskBenchmarkInfo info = new ScheduledTaskBenchmarkInfo();
        info.setStart(new Date());
        info.setType("Delivery");
        info.setDeliveryID(activityID);
        info.setServer(InetAddress.getLocalHost().getHostName());

        PreparedStatement typeStmt = conn.prepareStatement("SELECT SCHEDULED_ACCOUNT_ACTIVITY.activity_type FROM SCHEDULED_ACCOUNT_ACTIVITY WHERE " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        typeStmt.setLong(1, activityID);
        ResultSet typeRS = typeStmt.executeQuery();
        if (typeRS.next()) {
            int type = typeRS.getInt(1);
            if (type == ScheduledActivity.REPORT_DELIVERY) {
                reportDelivery(conn);
            } else if (type == ScheduledActivity.SCORECARD_DELIVERY) {
                scorecardDelivery(conn);
            } else if (type == ScheduledActivity.GENERAL_DELIVERY) {
                generalDelivery(conn);
            }
        }
        typeStmt.close();
        info.setEnd(new Date());
        BenchmarkManager.measureTask(info);
    }

    private static class DeliveryResult {
        private String body;
        private AttachmentInfo attachmentInfo;

        private DeliveryResult(String body) {
            this.body = body;
        }

        private DeliveryResult(AttachmentInfo attachmentInfo) {
            this.attachmentInfo = attachmentInfo;
        }
    }

    private class SenderInfo {
        private String senderName;
        private String senderEmail;

        private SenderInfo(String senderName, String senderEmail) {
            this.senderName = senderName;
            this.senderEmail = senderEmail;
        }
    }

    private SenderInfo getSenderInfo(long id, EIConnection conn) throws SQLException {
        PreparedStatement getSernderStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME FROM USER, GENERAL_DELIVERY WHERE GENERAL_DELIVERY.sender_user_id = user.user_id and " +
                "GENERAL_DELIVERY.scheduled_account_activity_id = ?");
        getSernderStmt.setLong(1, activityID);
        ResultSet senderRS = getSernderStmt.executeQuery();
        if (senderRS.next()) {
            String email = senderRS.getString(1);
            String userFirstName = senderRS.getString(2);
            String userLastName = senderRS.getString(3);
            String senderName = userFirstName + " " + userLastName;
            String senderEmail = email;
            return new SenderInfo(senderName, senderEmail);
        }
        getSernderStmt.close();
        return null;
    }

    private void generalDelivery(final EIConnection conn) throws Exception {
        GeneralDelivery generalDelivery = (GeneralDelivery) ScheduledActivity.createActivity(ScheduledActivity.GENERAL_DELIVERY, activityID, conn);

        if (generalDelivery != null) {
            List<WSAnalysisDefinition> reportList = new ArrayList<>();
            List<Dashboard> dashboardList = new ArrayList<>();

            for (DeliveryInfo info : generalDelivery.getDeliveryInfos()) {
                if (info.getType() == DeliveryInfo.REPORT) {
                    reportList.add(new AnalysisStorage().getAnalysisDefinition(info.getId(), conn));
                } else if (info.getType() == DeliveryInfo.DASHBOARD) {
                    dashboardList.add(new DashboardStorage().getDashboard(info.getId(), conn));
                }
            }

            long ownerID = generalDelivery.getUserID();

            final String subject = ExportService.filterTransform(reportList, dashboardList, generalDelivery.getSubject());
            final String body = ExportService.filterTransform(reportList, dashboardList, generalDelivery.getBody());

            String senderName = null;
            String senderEmail = null;
            if (generalDelivery.getSenderID() > 0) {
                SenderInfo senderInfo = getSenderInfo(generalDelivery.getSenderID(), conn);
                senderName = senderInfo.senderName;
                senderEmail = senderInfo.senderEmail;
            }

            PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, ACCOUNT.ACCOUNT_TYPE, USER.account_admin," +
                    "ACCOUNT.FIRST_DAY_OF_WEEK, USER.first_name, USER.name, USER.email, USER.ACCOUNT_ID, USER.PERSONA_ID, USER.TEST_ACCOUNT_VISIBLE FROM USER, ACCOUNT " +
                    "WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND (ACCOUNT.account_state = ? OR ACCOUNT.ACCOUNT_STATE = ?) AND USER.USER_ID = ?");
            queryStmt.setInt(1, Account.ACTIVE);
            queryStmt.setInt(2, Account.TRIAL);
            queryStmt.setLong(3, ownerID);
            ResultSet queryRS = queryStmt.executeQuery();
            if (!queryRS.next()) {
                return;
            }

            final String userName = queryRS.getString(1);
            final int accountType = queryRS.getInt(2);
            final boolean accountAdmin = queryRS.getBoolean(3);
            final int firstDayOfWeek = queryRS.getInt(4);
            final long accountID = queryRS.getLong(8);
            final long userPersonaID = queryRS.getLong("USER.persona_ID");
            final boolean accountReports = queryRS.getBoolean("USER.TEST_ACCOUNT_VISIBLE");

            UserInfo defaultUser = new UserInfo(queryRS.getString("USER.email"), queryRS.getString("USER.first_name"), queryRS.getString("USER.name"),
                    userName, ownerID, accountAdmin, userPersonaID);

            List<String> emails = generalDelivery.getEmails();


            Set<UserInfo> userInfoSet = new HashSet<>();
            getUsers(conn, generalDelivery.getUsers(), userInfoSet);

            getGroups(conn, generalDelivery.getGroups(), userInfoSet);

            generateForUsers(userInfoSet, conn, subject, body, generalDelivery.isHtmlEmail(), generalDelivery.getTimezoneOffset(), generalDelivery.getDeliveryInfos(), accountType, firstDayOfWeek, accountID, emails, defaultUser, senderEmail, senderName, true,
                    accountReports);
        }
    }

    private void getGroups(EIConnection conn, List<GroupDescriptor> groups, Set<UserInfo> userInfoSet) throws SQLException {
        PreparedStatement groupStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME, PERSONA_ID, USER.USER_ID, USER.ACCOUNT_ADMIN, USER.USERNAME " +
                "FROM USER, group_to_user_join WHERE " +
                "group_to_user_join.group_id = ? AND group_to_user_join.user_id = user.user_id");


        for (GroupDescriptor groupDescriptor : groups) {
            groupStmt.setLong(1, groupDescriptor.getGroupID());
            ResultSet groupRS = groupStmt.executeQuery();
            while (groupRS.next()) {
                String email = groupRS.getString(1);
                String firstName = groupRS.getString(2);
                String lastName = groupRS.getString(3);
                long personaID = groupRS.getLong(4);

                userInfoSet.add(new UserInfo(email, firstName, lastName, groupRS.getString(7), groupRS.getLong(5), groupRS.getBoolean(6), personaID));
            }
        }

        groupStmt.close();
    }

    private void getUsers(EIConnection conn, List<UserStub> users, Set<UserInfo> userInfoSet) throws SQLException {
        PreparedStatement userStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME, PERSONA_ID, USER.USER_ID, USER.ACCOUNT_ADMIN, USER.USERNAME FROM USER, delivery_to_user WHERE delivery_to_user.scheduled_account_activity_id = ? AND " +
                "delivery_to_user.user_id = user.user_id");
        userStmt.setLong(1, activityID);
        PreparedStatement userDetailStmt = conn.prepareStatement("SELECT PERSONA_ID, ACCOUNT_ADMIN FROM USER WHERE USER_ID = ?");

        for (UserStub user : users) {
            userDetailStmt.setLong(1, user.getUserID());
            ResultSet rs = userDetailStmt.executeQuery();
            rs.next();
            userInfoSet.add(new UserInfo(user.getEmail(), user.getFirstName(), user.getName(), user.getEmail(), user.getUserID(), rs.getBoolean(2), rs.getLong(1)));
        }

        userDetailStmt.close();
    }

    private void generateForUsers(Set<UserInfo> userInfoSet, EIConnection conn, String subject, String body, boolean htmlEmail, int timezoneOffset, List<DeliveryInfo> infos,
                                  int accountType, int firstDayOfWeek, long accountID, List<String> emails, UserInfo defaultUser, String senderEmail, String senderName, boolean sendIfNoData,
                                  boolean accountReports)
            throws SQLException, CloneNotSupportedException, MessagingException, InterruptedException, UnsupportedEncodingException {
        Map<List<UserDLS>, List<UserInfo>> personaMap = new HashMap<>();
        for (UserInfo userInfo : userInfoSet) {
            List<UserDLS> dls;
            if (userInfo.personaID > 0) {
                dls = new PreferencesService().getUserDLS(userInfo.userID, userInfo.personaID, conn);
            } else {
                dls = new ArrayList<>();
            }
            List<UserInfo> userInfos = personaMap.get(dls);
            if (userInfos == null) {
                userInfos = new ArrayList<>();
                personaMap.put(dls, userInfos);
            }
            userInfos.add(userInfo);
        }

        boolean sentEmails = false;
        for (Map.Entry<List<UserDLS>, List<UserInfo>> entry : personaMap.entrySet()) {
            final UserInfo firstUser = entry.getValue().get(0);
            PreparedStatement personaStmt = conn.prepareStatement("SELECT PERSONA.persona_name FROM USER, PERSONA WHERE USER.PERSONA_ID = PERSONA.PERSONA_ID AND USER.USER_ID = ?");
            personaStmt.setLong(1, firstUser.userID);
            ResultSet personaRS = personaStmt.executeQuery();

            String personaN = null;
            if (personaRS.next()) {
                personaN = personaRS.getString(1);
            }
            final String personaName = personaN;
            List<String> emailAddressesToSend = new ArrayList<>();
            if (entry.getKey().size() == 0) {
                emailAddressesToSend = emails;
                sentEmails = true;
            }
            sendEmails(conn, subject, body, htmlEmail, timezoneOffset, infos, accountType, firstDayOfWeek, accountID,
                    entry.getValue(), emailAddressesToSend, firstUser, personaName, senderEmail, senderName, sendIfNoData, accountReports);
        }
        if (!sentEmails) {
            sendEmails(conn, subject, body, htmlEmail, timezoneOffset, infos, accountType, firstDayOfWeek, accountID,
                    new ArrayList<>(), emails, defaultUser, null, senderEmail, senderName, sendIfNoData, accountReports);
        }
    }

    private void sendEmails(final EIConnection conn, String subject, String body, boolean htmlEmail, final int timezoneOffset,
                            List<DeliveryInfo> infos, final int accountType, final int firstDayOfWeek, final long accountID,
                            List<UserInfo> users, List<String> emails, final UserInfo firstUser, @Nullable final String personaName, String senderEmail, String senderName,
                            final boolean sendIfNoData, final boolean accountReports)
            throws InterruptedException, SQLException, MessagingException, UnsupportedEncodingException {
        String emailBody = body;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 5, 5, TimeUnit.MINUTES, queue);
        final CountDownLatch latch = new CountDownLatch(infos.size());
        final List<String> bodyElements = new ArrayList<>();
        final List<AttachmentInfo> attachmentInfos = new ArrayList<>();
        for (DeliveryInfo dInfo : infos) {
            final DeliveryInfo deliveryInfo = dInfo;
            tpe.execute(() -> {
                SecurityUtil.populateThreadLocal(firstUser.userName, firstUser.userID, accountID, accountType, firstUser.accountAdmin, firstDayOfWeek, personaName);
                EIConnection ourConn = Database.instance().getConnection();
                try {
                    DeliveryResult deliveryResult = handleDeliveryInfo(deliveryInfo, ourConn, timezoneOffset, sendIfNoData);
                    if (deliveryResult != null) {
                        if (deliveryResult.body != null) {
                            bodyElements.add(deliveryResult.body);
                        } else if (deliveryResult.attachmentInfo != null) {
                            attachmentInfos.add(deliveryResult.attachmentInfo);
                        }
                    }
                    latch.countDown();
                } catch (ReportException re) {
                    LogClass.info(re.getReportFault().toString());
                    re.printStackTrace();
                    latch.countDown();
                } catch (Exception e) {
                    LogClass.error(e);
                    latch.countDown();
                } finally {
                    SecurityUtil.clearThreadLocal();
                    Database.closeConnection(ourConn);
                }
            });
        }
        latch.await();
        tpe.shutdown();

        if (bodyElements.size() == 0 && attachmentInfos.size() == 0) {
            return;
        }
        for (String bodyElement : bodyElements) {
            emailBody += bodyElement;
        }

        PreparedStatement deliveryAuditStmt = conn.prepareStatement("INSERT INTO REPORT_DELIVERY_AUDIT (" +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID, SUCCESSFUL, TARGET_EMAIL, SEND_DATE) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        for (UserInfo userInfo : users) {
            deliveryAuditStmt.setLong(1, activityID);
            deliveryAuditStmt.setInt(2, 0);
            deliveryAuditStmt.setString(3, userInfo.email);
            deliveryAuditStmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            deliveryAuditStmt.execute();
            long auditID = Database.instance().getAutoGenKey(deliveryAuditStmt);
            new SendGridEmail().sendMultipleAttachmentsEmail(userInfo.email, subject, emailBody, htmlEmail, senderEmail, senderName, attachmentInfos, "ReportDelivery", auditID);
        }
        for (String email : emails) {
            deliveryAuditStmt.setLong(1, activityID);
            deliveryAuditStmt.setInt(2, 0);
            deliveryAuditStmt.setString(3, email);
            deliveryAuditStmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            deliveryAuditStmt.execute();
            long auditID = Database.instance().getAutoGenKey(deliveryAuditStmt);
            new SendGridEmail().sendMultipleAttachmentsEmail(email, subject, emailBody, htmlEmail, senderEmail, senderName, attachmentInfos, "ReportDelivery", auditID);
        }
        deliveryAuditStmt.close();
    }

    private DeliveryResult handleDeliveryInfo(DeliveryInfo deliveryInfo, EIConnection conn, int timezoneOffset, boolean sendIfNoData) throws Exception {
        InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
        insightRequestMetadata.setUtcOffset(timezoneOffset);

        if (deliveryInfo.getType() == DeliveryInfo.REPORT) {
            WSAnalysisDefinition analysisDefinition = new AnalysisStorage().getAnalysisDefinition(deliveryInfo.getId(), conn);
            analysisDefinition.updateMetadata();
            if (deliveryInfo.getLabel() != null && !"".equals(deliveryInfo.getLabel())) {
                analysisDefinition.setName(deliveryInfo.getLabel());
            }
            List<FilterDefinition> customFilters;
            if (deliveryInfo.getConfigurationID() > 0) {
                SavedConfiguration configuration = new DashboardService().getConfigurationForReport(deliveryInfo.getConfigurationID(), conn).getSavedConfiguration();
                customFilters = new ArrayList<>(configuration.getDashboardStackPositions().getFilterMap().values());
            } else {
                customFilters = deliveryInfo.getFilters();
            }
            updateReportWithCustomFilters(analysisDefinition, customFilters);

            if (deliveryInfo.getFormat() == ReportDelivery.EXCEL || deliveryInfo.getFormat() == ReportDelivery.EXCEL_2007) {
                System.out.println("Running report " + deliveryInfo.getId() + " for Excel delivery");
                byte[] bytes = new ExportService().toExcelEmail(analysisDefinition, conn, insightRequestMetadata, sendIfNoData, deliveryInfo.getFormat() == ReportDelivery.EXCEL_2007);
                if (bytes != null) {
                    String deliveryName;
                    if (deliveryInfo.getName() == null || "".equals(deliveryInfo.getName())) {
                        deliveryName = analysisDefinition.getName();
                    } else {
                        deliveryName = deliveryInfo.getName();
                    }
                    String ending;
                    if (deliveryInfo.getFormat() == ReportDelivery.EXCEL_2007) {
                        ending = "xlsx";
                    } else {
                        ending = "xls";
                    }
                    return new DeliveryResult(new AttachmentInfo(bytes, deliveryName + "." + ending, "application/xls"));
                }
            } else if (deliveryInfo.getFormat() == ReportDelivery.HTML_TABLE) {
                if (deliveryInfo.getType() == DeliveryInfo.REPORT) {
                    System.out.println("Running report " + deliveryInfo.getId() + " for HTML delivery");
                    String table = createHTMLTable(conn, analysisDefinition, insightRequestMetadata, sendIfNoData, true, new ExportProperties(true, true, null));
                    if (table != null) {
                        System.out.println("Returning HTML table for " + deliveryInfo.getId());
                        return new DeliveryResult(table);
                    } else {
                        System.out.println("No data found for HTML table for " + deliveryInfo.getId() + " with send if no data = " + sendIfNoData);
                    }
                } else if (deliveryInfo.getType() == DeliveryInfo.SCORECARD) {
                    System.out.println("Running scorecard " + deliveryInfo.getId() + " for HTML delivery");
                    return new DeliveryResult(ExportService.exportScorecard(deliveryInfo.getId(), insightRequestMetadata, conn));
                }
            } else if (deliveryInfo.getFormat() == ReportDelivery.PDF || deliveryInfo.getFormat() == ReportDelivery.PNG) {
                if (deliveryInfo.getFormat() == ReportDelivery.PDF && ExportService.toDirectPDF(analysisDefinition.getReportType())) {
                    System.out.println("Running report " + deliveryInfo.getId() + " for inline PDF delivery");
                    byte[] bytes = new ExportService().toPDFBytes(analysisDefinition, conn, insightRequestMetadata);
                    String reportName = analysisDefinition.getName();
                    return new DeliveryResult(new AttachmentInfo(bytes, reportName + ".pdf", "application/pdf"));
                } else {
                    System.out.println("Running report " + deliveryInfo.getId() + " for selenium PDF or PNG delivery");
                    long id = new SeleniumLauncher().requestSeleniumDrawForReport(deliveryInfo.getId(), activityID, SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn,
                            deliveryInfo.getFormat(), deliveryInfo.getDeliveryExtension());
                    System.out.println("launched " + id);
                    String topicName = ConfigLoader.instance().getReportDeliveryQueue();
                    String queueName = topicName + InetAddress.getLocalHost().getHostName().replace(".", "") + Thread.currentThread().getId();
                    System.out.println("Queue: " + queueName);
                    MessageQueue msgQueue = SQSUtils.connectToQueue(queueName, "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
                    NotificationService notificationService = new NotificationService("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
                    notificationService.createTopic(topicName);
                    Result<String> result = notificationService.subscribe("arn:aws:sns:us-east-1:808335860417:" + topicName, "sqs", "arn:aws:sqs:us-east-1:808335860417:" + queueName);

                    msgQueue.setQueueAttribute("Policy", QUEUE_POLICY_FORMAT.replace("{0}", queueName).replace("{1}", topicName));

                    msgQueue.setEncoding(false);
                    try {
                        int timeout = 0;
                        while (timeout < 300) {
                            Message message = msgQueue.receiveMessage();
                            if (message == null) {
                                timeout++;
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                    // ignore
                                }
                            } else {
                                timeout++;
                                JSONObject jo = (JSONObject) JSONValue.parse(message.getMessageBody());
                                String body = (String) jo.get("Message");
                                msgQueue.deleteMessage(message);
                                String[] parts = body.split("\\|");
                                long responseID = Long.parseLong(parts[0]);
                                System.out.println("got response of " + responseID + ", looking for " + id);
                                if (responseID == id) {
                                    long pdfID = Long.parseLong(parts[1]);
                                    System.out.println("retrieving and returning " + pdfID);
                                    PreparedStatement getStmt = conn.prepareStatement("SELECT PNG_IMAGE FROM PNG_EXPORT WHERE PNG_EXPORT_ID = ?");
                                    getStmt.setLong(1, pdfID);
                                    ResultSet rs = getStmt.executeQuery();
                                    rs.next();
                                    byte[] bytes = rs.getBytes(1);
                                    String deliveryName;
                                    if (deliveryInfo.getName() == null || "".equals(deliveryInfo.getName())) {
                                        deliveryName = "export";
                                    } else {
                                        deliveryName = deliveryInfo.getName();
                                    }
                                    if (deliveryInfo.getFormat() == ReportDelivery.PDF) {
                                        return new DeliveryResult(new AttachmentInfo(bytes, deliveryName + ".pdf", "application/pdf"));
                                    } else {
                                        return new DeliveryResult(new AttachmentInfo(bytes, deliveryName + ".png", "image/png"));
                                    }
                                } else {
                                    System.out.println("does not match, ignoring");
                                }
                            }
                        }
                    } finally {
                        try {
                            notificationService.unsubscribe(result.getResult());
                            msgQueue.deleteQueue();
                        } catch (Exception e) {
                            LogClass.error(e);
                        }
                    }
                    LogClass.error("Failed to generate Selenium report for report ID " + deliveryInfo.getId() + ", ended up timing out.");
                }
            }
        } else if (deliveryInfo.getType() == DeliveryInfo.DASHBOARD) {
            System.out.println("Running dashboard " + deliveryInfo.getId() + " for Selenium delivery");
            if (deliveryInfo.getDeliveryExtension() != null) {
                PDFDeliveryExtension deliveryExtension = (PDFDeliveryExtension) deliveryInfo.getDeliveryExtension();
                if (deliveryExtension.getGenerateByHTML() == 1) {
                    Dashboard dashboard = new DashboardService().getDashboardView(deliveryInfo.getId());
                    DashboardStackPositions positions;
                    if (deliveryInfo.getConfigurationID() > 0) {
                        PreparedStatement urlKeyStmt = conn.prepareStatement("SELECT saved_configuration.url_key FROM saved_configuration WHERE " +
                                "saved_configuration.saved_configuration_id = ?");
                        urlKeyStmt.setLong(1, deliveryInfo.getConfigurationID());
                        ResultSet urlRS = urlKeyStmt.executeQuery();
                        urlRS.next();
                        String key = urlRS.getString(1);

                        positions = new DashboardService().getConfigurationForDashboard(key).getSavedConfiguration().getDashboardStackPositions();
                    } else {
                        positions = new DashboardStackPositions();
                    }
                    byte[] bytes = new DashboardPDF().createPDF(dashboard, positions, new HashMap<>(),
                            insightRequestMetadata.getUtcOffset(), deliveryExtension.isShowHeader());
                    return new DeliveryResult(new AttachmentInfo(bytes, dashboard.getName() + ".pdf", "application/pdf"));
                } else {
                    return dashboardToPDFViaFlash(deliveryInfo, conn);
                }
            } else {
                return dashboardToPDFViaFlash(deliveryInfo, conn);
            }
        }
        return null;
    }

    private DeliveryResult dashboardToPDFViaFlash(DeliveryInfo deliveryInfo, EIConnection conn) throws SNSException, SQSException, UnknownHostException, SQLException {
        long id = new SeleniumLauncher().requestSeleniumDrawForDashboard(deliveryInfo.getId(), activityID, SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn,
                deliveryInfo.getFormat(), deliveryInfo.getDeliveryExtension());
        System.out.println("launched " + id);
        String topicName = ConfigLoader.instance().getReportDeliveryQueue();
        String queueName = topicName + InetAddress.getLocalHost().getHostName().replace(".", "") + Thread.currentThread().getId();
        System.out.println("Queue: " + queueName);
        MessageQueue msgQueue = SQSUtils.connectToQueue(queueName, "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
        NotificationService notificationService = new NotificationService("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
        notificationService.createTopic(topicName);
        Result<String> result = notificationService.subscribe("arn:aws:sns:us-east-1:808335860417:" + topicName, "sqs", "arn:aws:sqs:us-east-1:808335860417:" + queueName);
        msgQueue.setQueueAttribute("Policy", QUEUE_POLICY_FORMAT.replace("{0}", queueName).replace("{1}", topicName));

        msgQueue.setEncoding(false);

        try {
            int timeout = 0;
            while (timeout < 300) {
                Message message = msgQueue.receiveMessage();
                if (message == null) {
                    timeout++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                } else {
                    timeout++;
                    JSONObject jo = (JSONObject) JSONValue.parse(message.getMessageBody());
                    String body = (String) jo.get("Message");
                    msgQueue.deleteMessage(message);
                    String[] parts = body.split("\\|");
                    long responseID = Long.parseLong(parts[0]);
                    System.out.println("got response of " + responseID + ", looking for " + id);
                    if (responseID == id) {
                        long pdfID = Long.parseLong(parts[1]);
                        System.out.println("retrieving and returning " + pdfID);
                        PreparedStatement getStmt = conn.prepareStatement("SELECT PNG_IMAGE FROM PNG_EXPORT WHERE PNG_EXPORT_ID = ?");
                        getStmt.setLong(1, pdfID);
                        ResultSet rs = getStmt.executeQuery();
                        rs.next();
                        byte[] bytes = rs.getBytes(1);
                        String deliveryName;
                        if (deliveryInfo.getName() == null || "".equals(deliveryInfo.getName())) {
                            deliveryName = "export";
                        } else {
                            deliveryName = deliveryInfo.getName();
                        }
                        if (deliveryInfo.getFormat() == ReportDelivery.PDF) {
                            return new DeliveryResult(new AttachmentInfo(bytes, deliveryName + ".pdf", "application/pdf"));
                        } else {
                            return new DeliveryResult(new AttachmentInfo(bytes, deliveryName + ".png", "image/png"));
                        }
                    } else {
                        System.out.println("does not match, ignoring");
                    }
                }
            }
        } finally {
            try {
                notificationService.unsubscribe(result.getResult());
                msgQueue.deleteQueue();
            } catch (Exception e) {
                LogClass.error(e);
            }
        }
        LogClass.error("Failed to generate Selenium dashboard for " + deliveryInfo.getId() + ", ended up timing out.");
        return null;
    }

    private void scorecardDelivery(EIConnection conn) throws Exception {
        ScorecardDeliveryUtils.scorecardDelivery(conn, activityID);
    }

    private void reportDelivery(EIConnection conn) throws SQLException, IOException, MessagingException, DocumentException, CloneNotSupportedException, InterruptedException {

        ReportDelivery reportDelivery = (ReportDelivery) ScheduledActivity.createActivity(ScheduledActivity.REPORT_DELIVERY, activityID, conn);
        if (reportDelivery != null) {
            final int deliveryFormat = reportDelivery.getReportFormat();
            long reportID = reportDelivery.getReportID();
            String subjectLine = reportDelivery.getSubject();
            WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            final String subject = ExportService.filterTransformForReport(report, subjectLine);
            String bodyStart = reportDelivery.getBody();
            final String body = ExportService.filterTransformForReport(report, bodyStart);
            final boolean htmlEmail = reportDelivery.isHtmlEmail();
            int timezoneOffset = reportDelivery.getTimezoneOffset();


            DeliveryInfo deliveryInfo = new DeliveryInfo();
            deliveryInfo.setId(reportID);
            deliveryInfo.setType(DeliveryInfo.REPORT);
            deliveryInfo.setFormat(deliveryFormat);

            deliveryInfo.setFilters(reportDelivery.getCustomFilters());
            deliveryInfo.setDeliveryExtension(reportDelivery.getDeliveryExtension());

            boolean sendIfNoData = reportDelivery.isSendIfNoData();

            long configurationID = reportDelivery.getConfigurationID();

            deliveryInfo.setConfigurationID(configurationID);

            PreparedStatement findOwnerStmt = conn.prepareStatement("SELECT USER_ID FROM user_to_analysis WHERE analysis_id = ?");

            findOwnerStmt.setLong(1, reportID);
            ResultSet ownerRS = findOwnerStmt.executeQuery();
            long ownerID;
            if (ownerRS.next()) {
                ownerID = ownerRS.getLong(1);
            } else {
                return;
            }
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, ACCOUNT.ACCOUNT_TYPE, USER.account_admin," +
                    "ACCOUNT.FIRST_DAY_OF_WEEK, USER.first_name, USER.name, USER.email, USER.ACCOUNT_ID, USER.PERSONA_ID, USER.TEST_ACCOUNT_VISIBLE FROM USER, ACCOUNT " +
                    "WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND (ACCOUNT.account_state = ? OR ACCOUNT.ACCOUNT_STATE = ?) AND USER.USER_ID = ?");
            queryStmt.setInt(1, Account.ACTIVE);
            queryStmt.setInt(2, Account.TRIAL);
            queryStmt.setLong(3, ownerID);
            ResultSet queryRS = queryStmt.executeQuery();
            if (!queryRS.next()) {
                return;
            }

            final String userName = queryRS.getString(1);
            final int accountType = queryRS.getInt(2);
            final boolean accountAdmin = queryRS.getBoolean(3);
            final int firstDayOfWeek = queryRS.getInt(4);
            final long accountID = queryRS.getLong(8);
            final long userPersonaID = queryRS.getLong("USER.persona_ID");
            final boolean accountReports = queryRS.getBoolean("USER.TEST_ACCOUNT_VISIBLE");

            UserInfo defaultUser = new UserInfo(queryRS.getString("USER.email"), queryRS.getString("USER.first_name"), queryRS.getString("USER.name"),
                    userName, ownerID, accountAdmin, userPersonaID);

            Set<UserInfo> userInfoSet = new HashSet<>();
            List<String> emails = reportDelivery.getEmails();
            getUsers(conn, reportDelivery.getUsers(), userInfoSet);

            getGroups(conn, reportDelivery.getGroups(), userInfoSet);

            String senderName = null;
            String senderEmail = null;

            PreparedStatement getSernderStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME, NAME FROM USER, REPORT_DELIVERY WHERE REPORT_DELIVERY.sender_user_id = user.user_id AND " +
                    "REPORT_DELIVERY.scheduled_account_activity_id = ?");
            getSernderStmt.setLong(1, activityID);
            ResultSet senderRS = getSernderStmt.executeQuery();
            if (senderRS.next()) {
                senderEmail = senderRS.getString(1);
                String senderFIrstName = senderRS.getString(2);
                String senderLastName = senderRS.getString(3);
                senderName = senderFIrstName + " " + senderLastName;
            }
            getSernderStmt.close();



            generateForUsers(userInfoSet, conn, subject, body, htmlEmail, timezoneOffset, Arrays.asList(deliveryInfo), accountType, firstDayOfWeek, accountID,
                    emails, defaultUser, senderEmail, senderName, sendIfNoData, accountReports);

            queryStmt.close();
        }
    }

    private void updateReportWithCustomFilters(WSAnalysisDefinition analysisDefinition, List<FilterDefinition> customFilters) {

        if (analysisDefinition.getFilterDefinitions() != null) {
            List<FilterDefinition> replacements = new ArrayList<>();
            Iterator<FilterDefinition> filterIter = analysisDefinition.getFilterDefinitions().iterator();
            while (filterIter.hasNext()) {
                FilterDefinition filterDefinition = filterIter.next();
                FilterDefinition matchedCustomFilter = null;
                for (FilterDefinition customFilter : customFilters) {
                    if (customFilter.getClass().equals(filterDefinition.getClass())) {
                        if (customFilter.getFilterName() != null && !"".equals(customFilter.getFilterName()) &&
                                filterDefinition.getFilterName() != null && !"".equals(filterDefinition.getFilterName())) {
                            if (customFilter.getFilterName().equals(filterDefinition.getFilterName())) {
                                matchedCustomFilter = customFilter;
                                break;
                            }
                        }
                        if (customFilter.getField() != null && filterDefinition.getField() != null && customFilter.getField().equals(filterDefinition.getField())) {
                            matchedCustomFilter = customFilter;
                            break;
                        }
                    }
                }
                if (matchedCustomFilter != null) {
                    replacements.add(matchedCustomFilter);
                    filterIter.remove();
                }
            }
            analysisDefinition.getFilterDefinitions().addAll(replacements);
        }
    }

    @Nullable
    public static String createHTMLTable(EIConnection conn, WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata,
                                         boolean sendIfNoData, boolean includeTitle, ExportProperties exportProperties) throws SQLException {
        String table;
        if (analysisDefinition.getReportType() == WSAnalysisDefinition.VERTICAL_LIST) {
            DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
            if (dataSet.getRows().size() == 0 && !sendIfNoData) {
                return null;
            }
            table = ExportService.verticalListToHTMLTable(analysisDefinition, dataSet, conn, insightRequestMetadata, exportProperties.isEmailed());
        } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.YTD) {
            if (!exportProperties.isEmailed()) {
                table = ExportService.ytdToHTMLTableWithActualCSS(analysisDefinition, conn, insightRequestMetadata, includeTitle);
            } else {
                table = ExportService.ytdToHTMLTable(analysisDefinition, conn, insightRequestMetadata, includeTitle);
            }
        } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.TREE ||
                analysisDefinition.getReportType() == WSAnalysisDefinition.SUMMARY) {

            if (!exportProperties.isEmailed()) {
                table = ExportService.treeReportToHTMLTableWithActualCSS(analysisDefinition, conn, insightRequestMetadata, includeTitle);
            } else {
                table = ExportService.treeReportToHTMLTable(analysisDefinition, conn, insightRequestMetadata, includeTitle);
            }
        } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.MULTI_SUMMARY) {
            WSMultiSummaryDefinition multiSummaryDefinition = (WSMultiSummaryDefinition) analysisDefinition;
            table = multiSummaryDefinition.toExportHTML(conn, insightRequestMetadata, exportProperties.isEmailed());
        } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.COMPARE_YEARS) {
            if (!exportProperties.isEmailed()) {
                table = ExportService.compareYearsToHTMLTableWithActualCSS(analysisDefinition, conn, insightRequestMetadata, includeTitle);
            } else {
                table = ExportService.compareYearsToHTMLTable(analysisDefinition, conn, insightRequestMetadata, includeTitle);
            }
        } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.CROSSTAB) {
            DataSet dataSet = DataService.listDataSet(analysisDefinition, insightRequestMetadata, conn);
            if (dataSet.getRows().size() == 0 && !sendIfNoData) {
                return null;
            }
            if (!exportProperties.isEmailed()) {
                table = ExportService.crosstabReportToHTMLTableWithActualCSS(analysisDefinition, dataSet, conn, insightRequestMetadata, includeTitle);
            } else {
                table = ExportService.crosstabReportToHTMLTable(analysisDefinition, dataSet, conn, insightRequestMetadata, includeTitle);
            }
        } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.FORM || analysisDefinition.getReportType() == WSAnalysisDefinition.TREND) {
            table = analysisDefinition.toExportHTML(conn, insightRequestMetadata, exportProperties.isEmailed());
        } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.TREND_GRID ||
                analysisDefinition.getReportType() == WSAnalysisDefinition.DIAGRAM) {
            if (exportProperties.isEmailed()) {
                table = ExportService.kpiReportToHtmlTable(analysisDefinition, conn, insightRequestMetadata, sendIfNoData, includeTitle);
            } else {
                table = ExportService.kpiReportToHtmlTableWithActualCSS(analysisDefinition, conn, insightRequestMetadata, sendIfNoData, includeTitle);
            }
        } else if (analysisDefinition.getReportType() == WSAnalysisDefinition.TEXT) {
            table = ExportService.textReportToHtml(analysisDefinition, conn, insightRequestMetadata, exportProperties.isEmailed());
        } else {
            ListDataResults listDataResults = (ListDataResults) DataService.list(analysisDefinition, insightRequestMetadata, conn);
            if (listDataResults.getRows().length == 0 && !sendIfNoData) {
                return null;
            }
            if (listDataResults.getReportFault() != null) {
                System.out.println("\tReport fault on " + analysisDefinition.getAnalysisID() + " - " + listDataResults.getReportFault().toString());
                return null;
            }
            if (!exportProperties.isEmailed()) {
                table = ExportService.listReportToHTMLTableWithActualCSS(analysisDefinition, listDataResults, conn, insightRequestMetadata, includeTitle, exportProperties);
            } else {
                table = ExportService.listReportToHTMLTable(analysisDefinition, listDataResults, conn, insightRequestMetadata, includeTitle, exportProperties);
            }

        }
        return table;
    }


}
