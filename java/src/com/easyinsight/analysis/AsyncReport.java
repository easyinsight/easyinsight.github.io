package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSCompareYearsDefinition;
import com.easyinsight.analysis.definitions.WSKPIDefinition;
import com.easyinsight.analysis.definitions.WSTextDefinition;
import com.easyinsight.analysis.definitions.WSYTDDefinition;
import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.CachedAddonDataSource;
import com.easyinsight.datafeeds.UserThreadMutex;
import com.easyinsight.datafeeds.database.DataSourceListener;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.userupload.*;
import com.mysql.jdbc.Statement;
import org.hibernate.Session;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/16/14
 * Time: 11:16 AM
 */
public class AsyncReport {

    private static ThreadLocal<AsyncReportLocal> asyncReportLocals = new ThreadLocal<>();


    private static class AsyncReportLocal {
        private boolean inAsync;

        private AsyncReportLocal(boolean inAsync) {
            this.inAsync = inAsync;
        }

        public boolean isInAsync() {
            return inAsync;
        }
    }

    public static void establishAsync() {
        asyncReportLocals.set(new AsyncReportLocal(true));
    }

    public static void releaseAsync() {
        asyncReportLocals.remove();
    }

    public static boolean isAsync() {
        return asyncReportLocals.get() != null && asyncReportLocals.get().inAsync;
    }

    private int serverID;

    public AsyncReport(int serverID) {
        this.serverID = serverID;
    }

    public static final int WAITING_ASSIGN = 1;
    public static final int ASSIGNED = 2;
    public static final int IN_PROGRESS = 3;
    public static final int FINISHED = 4;

    public void assign() {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<Integer> servers = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT server_id FROM server WHERE enabled = ? AND report_listener = ?");
            stmt.setBoolean(1, true);
            stmt.setBoolean(2, true);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                servers.add(rs.getInt(1));
            }
            stmt.close();
            PreparedStatement workStmt = conn.prepareStatement("SELECT async_report_request_id, assigned_server, request_state FROM async_report_request WHERE request_state = ? OR request_state = ? OR request_state = ?");
            workStmt.setInt(1, WAITING_ASSIGN);
            workStmt.setInt(2, ASSIGNED);
            workStmt.setInt(3, IN_PROGRESS);
            ResultSet workRS = workStmt.executeQuery();
            Map<Integer, Integer> loadMap = new HashMap<>();
            for (Integer serverID : servers) {
                loadMap.put(serverID, 0);
            }
            List<Long> waitingForAssign = new ArrayList<>(1);
            while (workRS.next()) {
                long requestID = workRS.getLong(1);
                int assignedServer = workRS.getInt(2);
                boolean serverAssigned = !workRS.wasNull();
                int requestState = workRS.getInt(3);
                if (requestState == WAITING_ASSIGN) {
                    waitingForAssign.add(requestID);
                }
                if (serverAssigned) {
                    Integer count = loadMap.get(assignedServer);
                    if (count != null) {
                        loadMap.put(assignedServer, count + 1);
                    }
                }
            }
            workStmt.close();

            if (waitingForAssign.size() > 0) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE async_report_request SET request_state = ?, assigned_server = ? WHERE async_report_request_id = ?");
                for (Long requestID : waitingForAssign) {
                    int leastLoaded = Integer.MAX_VALUE;
                    Integer server = null;
                    for (Map.Entry<Integer, Integer> entry : loadMap.entrySet()) {
                        if (entry.getValue() < leastLoaded) {
                            leastLoaded = entry.getValue();
                            server = entry.getKey();
                        }
                    }
                    if (server == null) {
                        System.out.println("No worker servers available--waiting and trying again.");
                        Thread.sleep(10000);
                    } else {
                        updateStmt.setInt(1, ASSIGNED);
                        updateStmt.setInt(2, server);
                        updateStmt.setLong(3, requestID);
                        updateStmt.executeUpdate();
                    }
                }
                updateStmt.close();
            }
            conn.commit();
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void resetState() {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement u = conn.prepareStatement("UPDATE async_report_request SET request_state = ? WHERE request_state = ? AND assigned_server = ?");
            u.setInt(1, FINISHED);
            u.setInt(2, IN_PROGRESS);
            u.setInt(3, serverID);
            u.executeUpdate();
            u.close();
        } catch (Throwable e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public synchronized void claimAndRun() throws InterruptedException {
        Long requestID = null;
        /*byte[] reportBytes = null;
        byte[] metadataBytes = null;*/
        int requestType = 0;
        long userID = 0;
        long dataSourceID = 0;
        String callID = null;
        long taskID = 0;
        long cacheID = 0;
        String uploadKey = null;
        String fileName = null;
        do {
            EIConnection conn = Database.instance().getConnection();
            try {
                conn.setAutoCommit(false);
                PreparedStatement q = conn.prepareStatement("SELECT async_report_request_id, request_type, " +
                        "user_id, data_source_id, call_id, task_id, cache_source_id, upload_key, file_name FROM async_report_request WHERE request_state = ? AND assigned_server = ?");
                q.setInt(1, ASSIGNED);
                q.setInt(2, serverID);
                ResultSet rs = q.executeQuery();
                if (rs.next()) {
                    requestID = rs.getLong(1);
                    requestType = rs.getInt(2);
                    userID = rs.getLong(3);
                    dataSourceID = rs.getLong(4);
                    callID = rs.getString(5);
                    taskID = rs.getLong(6);
                    cacheID = rs.getLong(7);
                    uploadKey = rs.getString(8);
                    fileName = rs.getString(9);
                    PreparedStatement u = conn.prepareStatement("UPDATE async_report_request SET request_state = ? WHERE async_report_request_id = ?");
                    u.setInt(1, IN_PROGRESS);
                    u.setLong(2, requestID);
                    u.executeUpdate();
                    u.close();
                }
                q.close();
                conn.commit();
            } catch (Throwable e) {
                LogClass.error(e);
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
                Database.closeConnection(conn);
            }
            if (requestID == null) {
                Thread.sleep(100);
            }
        } while (requestID == null);

        String name;
        if (userID == 0) {
            name = "anonymous user";
        } else {
            name = "User " + userID;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = sdf.format(new Date());
        if (requestType == REPORT_EDITOR || requestType == REPORT_END_USER || requestType == REPORT_DATA_SET) {
            // log internally...
            asyncReport(requestID, requestType, userID);
        } else if (requestType == DATA_SOURCE_REFRESH) {
            System.out.println(time + ":" + " Async data source refresh of " + dataSourceID + " by " + name);
            DataSourceListener.dataSource(dataSourceID, callID, requestID);
        } else if (requestType == SCHEDULED_TASK) {
            // load the task...
            //System.out.println(time + ":" + " Async scheduled task" + );
            asyncTask(requestID, taskID);
        } else if (requestType == CACHE_REBUILD) {
            System.out.println(time + ":" + " Async Cache Rebuild");
            asyncCache(cacheID, requestID);
        } else if (requestType == FILE_UPLOAD_ANALYZE) {
            System.out.println(time + ":" + " Async Analyze File Upload - " + uploadKey);
            asyncAnalyzeFileUpload(requestID, fileName, uploadKey);
        } else if (requestType == FILE_UPLOAD_CREATE) {
            System.out.println(time + ":" + " Async Create File Data Source - " + uploadKey);
            asyncCreateFileUpload(requestID, userID, uploadKey, fileName);
        } else if (requestType == FILE_REPLACE_ANALYZE) {
            System.out.println(time + ":" + " Async Analyze File Data Source Update");
            asyncAnalyzeFileReplace(requestID, userID, dataSourceID, uploadKey);
        } else if (requestType == FILE_REPLACE_UPLOAD) {
            System.out.println(time + ":" + " Async Replace or Append File Data Source");
            asyncFileReplace(requestID, userID, uploadKey);
        }

    }

    protected void asyncFileReplace(Long requestID, long userID, String uploadKey) {
        try {

            DataSourceThreadPool.instance().addActivity(() -> {
                EIConnection conn = Database.instance().getConnection();
                try {
                    conn.setAutoCommit(false);
                    populateThreadLocal(userID, conn);
                    try {
                        PreparedStatement query = conn.prepareStatement("SELECT report_bytes, metadata_bytes FROM async_report_request_details " +
                                "WHERE async_report_request_id = ?");
                        query.setLong(1, requestID);
                        ResultSet detailRS = query.executeQuery();
                        detailRS.next();
                        byte[] reportBytes = detailRS.getBytes(1);
                        query.close();
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(reportBytes));
                        final UploadContextBlob uploadContextBlob = (UploadContextBlob) ois.readObject();
                        new UserUploadService().updateData(uploadContextBlob.getDataSourceID(), uploadKey,
                                uploadContextBlob.isUpdate(), uploadContextBlob.getAnalysisItems(), true);
                        MemCachedManager.instance().add("async" + requestID, 100, true);
                    } finally {
                        SecurityUtil.clearThreadLocal();
                    }
                    conn.commit();
                } catch (Throwable e) {
                    conn.rollback();
                    LogClass.error(e);
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                }
                markDone(requestID);
            });
        } catch (Throwable e) {
            LogClass.error(e);
        }
    }

    protected void asyncAnalyzeFileReplace(Long requestID, long userID, long dataSourceID, String uploadKey) {
        try {
            DataSourceThreadPool.instance().addActivity(() -> {
                EIConnection conn = Database.instance().getConnection();
                try {
                    conn.setAutoCommit(false);
                    populateThreadLocal(userID, conn);
                    try {
                        AnalyzeUploadResponse response = new UserUploadService().analyzeUpdate(dataSourceID, uploadKey, true);
                        MemCachedManager.instance().add("async" + requestID, 100, response);
                    } finally {
                        SecurityUtil.clearThreadLocal();
                    }
                    conn.commit();
                } catch (Throwable e) {
                    conn.rollback();
                    LogClass.error(e);
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                }
                markDone(requestID);
            });
        } catch (Throwable e) {
            LogClass.error(e);
        }
    }

    protected void asyncCreateFileUpload(Long requestID, long userID, String uploadKey, String fileName) {
        try {

            DataSourceThreadPool.instance().addActivity(() -> {
                EIConnection conn = Database.instance().getConnection();
                try {
                    conn.setAutoCommit(false);
                    populateThreadLocal(userID, conn);
                    try {
                        PreparedStatement query = conn.prepareStatement("SELECT report_bytes, metadata_bytes FROM async_report_request_details " +
                                "WHERE async_report_request_id = ?");
                        query.setLong(1, requestID);
                        ResultSet detailRS = query.executeQuery();
                        detailRS.next();
                        byte[] reportBytes = detailRS.getBytes(1);
                        query.close();
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(reportBytes));
                        final UploadContextBlob uploadContextBlob = (UploadContextBlob) ois.readObject();
                        FlatFileUploadContext context = new FlatFileUploadContext();
                        context.setFileName(fileName);
                        context.setUploadKey(uploadKey);
                        UploadResponse response = new UserUploadService().createDataSourceWithConn(uploadContextBlob.getName(),
                                uploadContextBlob.getUploadContext(), uploadContextBlob.getAnalysisItems(), uploadContextBlob.isAccountVisible(), conn);
                        MemCachedManager.instance().add("async" + requestID, 100, response);
                    } finally {
                        SecurityUtil.clearThreadLocal();
                    }
                    conn.commit();
                } catch (Throwable e) {
                    conn.rollback();
                    LogClass.error(e);
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                }
                markDone(requestID);
            });
        } catch (Throwable e) {
            LogClass.error(e);
        }
    }

    protected void asyncAnalyzeFileUpload(Long frequestID, String fFileName, String fUploadKey) {
        DataSourceThreadPool.instance().addActivity(() -> {
            EIConnection conn = Database.instance().getConnection();
            try {
                FlatFileUploadContext context = new FlatFileUploadContext();
                context.setFileName(fFileName);
                context.setUploadKey(fUploadKey);
                UploadResponse response = new UserUploadService().analyzeUploadWithConn(context, conn);
                MemCachedManager.instance().add("async" + frequestID, 100, response);
            } catch (Throwable e) {
                LogClass.error(e);
            } finally {
                Database.closeConnection(conn);
            }
            markDone(frequestID);
        });
    }

    protected void asyncCache(long fcacheID, Long frequestID) {
        DataSourceThreadPool.instance().addActivity(() -> {
            EIConnection conn = Database.instance().getConnection();
            try {
                CachedAddonDataSource.runReport(conn, fcacheID, true);
            } catch (Throwable e) {
                LogClass.error(e);
            } finally {
                Database.closeConnection(conn);
            }
            markDone(frequestID);
        });
    }

    protected void asyncTask(Long requestID, long taskID) {
        ScheduledTask scheduledTask = null;
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List tasks = session.createQuery("from ScheduledTask where scheduledTaskID = ?").setLong(0, taskID).list();
            if (tasks.size() > 0) {
                scheduledTask = (ScheduledTask) tasks.get(0);
            }
            session.getTransaction().commit();
        } catch (Throwable e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        if (scheduledTask == null) {
            markDone(requestID);
        } else {
            final ScheduledTask fTask = scheduledTask;
            final Long frequestID = requestID;
            DataSourceThreadPool.instance().addActivity(() -> {
                fTask.run();
                markDone(frequestID);
            });
        }
    }

    protected void asyncReport(Long frequestID, int frequestType, long fuserID) {

        ReportThreadPool.instance().addActivity(new AsyncRequestRunnable(frequestID) {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            AsyncReport.establishAsync();

                                                            try {
                                                                byte[] freportBytes;
                                                                byte[] fmetadataBytes;
                                                                EIConnection conn = Database.instance().getConnection();
                                                                try {
                                                                    populateThreadLocal(fuserID, conn);
                                                                    PreparedStatement query = conn.prepareStatement("SELECT report_bytes, metadata_bytes FROM async_report_request_details " +
                                                                            "WHERE async_report_request_id = ?");
                                                                    query.setLong(1, frequestID);
                                                                    ResultSet detailRS = query.executeQuery();
                                                                    detailRS.next();
                                                                    freportBytes = detailRS.getBytes(1);
                                                                    fmetadataBytes = detailRS.getBytes(2);
                                                                    query.close();
                                                                } catch (Exception e) {
                                                                    throw new RuntimeException(e);
                                                                } finally {
                                                                    Database.closeConnection(conn);
                                                                }


                                                                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(freportBytes));
                                                                WSAnalysisDefinition report = (WSAnalysisDefinition) ois.readObject();
                                                                ois = new ObjectInputStream(new ByteArrayInputStream(fmetadataBytes));
                                                                InsightRequestMetadata insightRequestMetadata = (InsightRequestMetadata) ois.readObject();
                                                                InsightRequestMetadata localMetadata = new InsightRequestMetadata();
                                                                localMetadata.setUtcOffset(insightRequestMetadata.getUtcOffset());
                                                                localMetadata.setNoAsync(true);
                                                                localMetadata.setRunningAsync(true);
                                                                localMetadata.setCacheForHTML(insightRequestMetadata.isCacheForHTML());
                                                                localMetadata.setNoCache(insightRequestMetadata.isNoCache());

                                                                String name;
                                                                if (SecurityUtil.getUserID(false) == 0) {
                                                                    name = "anonymous user";
                                                                } else {
                                                                    name = SecurityUtil.getUserName();
                                                                }
                                                                String reportName;
                                                                if (report.getName() == null) {
                                                                    reportName = "temporary report";
                                                                } else {
                                                                    reportName = report.getName();
                                                                }
                                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                                                System.out.println(sdf.format(new Date()) + ": Asynchronous execution of " + reportName + " by " + name);

                                                                if (frequestType == REPORT_EDITOR) {
                                                                    DataResults results;
                                                                    if (report instanceof WSCrosstabDefinition) {
                                                                        results = new DataService().getCrosstabDataResults((WSCrosstabDefinition) report, localMetadata);
                                                                    } else if (report instanceof WSKPIDefinition) {
                                                                        results = new DataService().getTrendDataResults((WSKPIDefinition) report, localMetadata);
                                                                    } else if (report instanceof WSTreeDefinition) {
                                                                        results = new DataService().getTreeDataResults((WSTreeDefinition) report, localMetadata);
                                                                    } else if (report instanceof WSYTDDefinition) {
                                                                        results = new DataService().getYTDResults(report, localMetadata);
                                                                    } else if (report instanceof WSCompareYearsDefinition) {
                                                                        results = new DataService().getCompareYearsResults(report, localMetadata);
                                                                    } else if (report instanceof WSTextDefinition) {
                                                                        results = new DataService().getTextDataResults((WSTextDefinition) report, localMetadata);
                                                                    } else {
                                                                        results = new DataService().list(report, localMetadata);
                                                                    }
                                                                    ResultData rh = new ResultData();
                                                                    rh.dataResults = results;
                                                                    rh.report = report;
                                                                    MemCachedManager.instance().add("async" + frequestID, 100, rh);
                                                                } else if (frequestType == REPORT_END_USER) {
                                                                    EmbeddedResults results;
                                                                    ResultData rh = new ResultData();
                                                                    conn = Database.instance().getConnection();
                                                                    try {
                                                                        if (report instanceof WSCrosstabDefinition) {
                                                                            results = new DataService().getEmbeddedCrosstabResults((WSCrosstabDefinition) report, insightRequestMetadata, conn);
                                                                        } else if (report instanceof WSYTDDefinition) {
                                                                            results = new DataService().getEmbeddedYTDResults((WSYTDDefinition) report, insightRequestMetadata, conn);
                                                                        } else if (report instanceof WSKPIDefinition) {
                                                                            results = new DataService().getEmbeddedTrendDataResults((WSKPIDefinition) report, insightRequestMetadata, conn);
                                                                        } else if (report instanceof WSTreeDefinition) {
                                                                            results = new DataService().getEmbeddedTreeResults((WSTreeDefinition) report, insightRequestMetadata, conn);
                                                                        } else if (report instanceof WSCompareYearsDefinition) {
                                                                            results = new DataService().getEmbeddedCompareYearsResults((WSCompareYearsDefinition) report, insightRequestMetadata, conn);
                                                                        } else if (report instanceof WSTextDefinition) {
                                                                            results = new DataService().getEmbeddedTextResults((WSTextDefinition) report, insightRequestMetadata, conn);
                                                                        } else {
                                                                            results = new DataService().getEmbeddedResultsForReport(report, null, localMetadata, new ArrayList<>(), conn,
                                                                                    insightRequestMetadata.isNoCache());
                                                                        }
                                                                        rh.results = results;
                                                                        rh.report = report;
                                                                    } catch (Throwable e) {
                                                                        rh.exception = e;
                                                                    } finally {
                                                                        Database.closeConnection(conn);
                                                                    }
                                                                    //}
                                                                    MemCachedManager.instance().add("async" + frequestID, 100, rh);
                                                                } else if (frequestType == REPORT_DATA_SET) {
                                                                    conn = Database.instance().getConnection();
                                                                    try {
                                                                        ResultData rh = new ResultData();
                                                                        try {
                                                                            DataSet dataSet = DataService.listDataSet(report, localMetadata, conn);
                                                                            dataSet.setAsyncSavedReport(report);
                                                                            rh.dataSet = dataSet;
                                                                            rh.report = report;
                                                                            MemCachedManager.instance().add("async" + frequestID, 100, rh);
                                                                        } catch (Throwable e) {
                                                                            rh.exception = e;
                                                                            MemCachedManager.instance().add("async" + frequestID, 100, rh);
                                                                        }
                                                                    } finally {
                                                                        Database.closeConnection(conn);
                                                                    }
                                                                }
                                                                conn = Database.instance().getConnection();
                                                                try {
                                                                    PreparedStatement u = conn.prepareStatement("UPDATE async_report_request SET request_state = ? WHERE async_report_request_id = ?");
                                                                    u.setInt(1, FINISHED);
                                                                    u.setLong(2, frequestID);
                                                                    u.executeUpdate();
                                                                    u.close();
                                                                } finally {
                                                                    Database.closeConnection(conn);
                                                                }
                                                            } finally {
                                                                SecurityUtil.clearThreadLocal();
                                                                AsyncReport.releaseAsync();
                                                            }

                                                        } catch (Throwable e) {
                                                            markDone(frequestID);
                                                            LogClass.error(e);
                                                        }
                                                    }
                                                }
        );
    }

    protected void populateThreadLocal(long fuserID, EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, ACCOUNT.ACCOUNT_TYPE, USER.account_admin," +
                "ACCOUNT.FIRST_DAY_OF_WEEK, USER.first_name, USER.name, USER.email, USER.ACCOUNT_ID, USER.PERSONA_ID, USER.TEST_ACCOUNT_VISIBLE FROM USER, ACCOUNT " +
                "WHERE USER.USER_ID = ? AND USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID");
        queryStmt.setLong(1, fuserID);
        ResultSet queryRS = queryStmt.executeQuery();
        queryRS.next();
        String userName = queryRS.getString(1);
        int accountType = queryRS.getInt(2);
        boolean accountAdmin = queryRS.getBoolean(3);
        int firstDayOfWeek = queryRS.getInt(4);
        long accountID = queryRS.getLong(8);
        long userPersonaID = queryRS.getLong("USER.persona_ID");
        String personaName = null;
        if (userPersonaID > 0) {
            PreparedStatement personaNameStmt = conn.prepareStatement("SELECT persona.persona_name FROM persona WHERE persona_id = ?");
            personaNameStmt.setLong(1, userPersonaID);
            ResultSet personaRS = personaNameStmt.executeQuery();
            if (personaRS.next()) {
                personaName = personaRS.getString(1);
            }
            personaNameStmt.close();
        }
        SecurityUtil.populateThreadLocal(userName, fuserID, accountID, accountType, accountAdmin, firstDayOfWeek, personaName);
    }

    protected void markDone(Long frequestID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement u = conn.prepareStatement("UPDATE async_report_request SET request_state = ? WHERE async_report_request_id = ?");
            u.setInt(1, AsyncReport.FINISHED);
            u.setLong(2, frequestID);
            u.executeUpdate();
            u.close();
        } catch (Throwable e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static final int REPORT_EDITOR = 1;
    public static final int REPORT_END_USER = 2;
    public static final int REPORT_DATA_SET = 3;
    public static final int DATA_SOURCE_REFRESH = 4;
    public static final int SCHEDULED_TASK = 5;
    public static final int CACHE_REBUILD = 6;
    public static final int FILE_UPLOAD_ANALYZE = 7;
    public static final int FILE_UPLOAD_CREATE = 8;
    public static final int FILE_REPLACE_ANALYZE = 9;
    public static final int FILE_REPLACE_UPLOAD = 10;

    public static UploadResponse fileUpload(FlatFileUploadContext context) {
        long requestID;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement reqStmt = conn.prepareStatement("INSERT INTO async_report_request (request_state, upload_key, file_name, request_created, request_type, user_id) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            reqStmt.setInt(1, WAITING_ASSIGN);
            reqStmt.setString(2, context.getUploadKey());
            reqStmt.setString(3, context.getFileName());
            reqStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            reqStmt.setInt(5, FILE_UPLOAD_ANALYZE);
            reqStmt.setLong(6, SecurityUtil.getUserID());
            reqStmt.execute();
            requestID = Database.instance().getAutoGenKey(reqStmt);
            reqStmt.close();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        try {
            int elapsedTime = 0;
            Object result = null;
            while (result == null && elapsedTime < DATA_SOURCE_TIMEOUT) {
                result = MemCachedManager.instance().get("async" + requestID);
                if (result == null) {
                    elapsedTime += 100;
                    Thread.sleep(100);
                }
            }
            return (UploadResponse) result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static UploadResponse fileCreate(String fileName, FlatFileUploadContext context, List<AnalysisItem> items, boolean accountVisible) {
        long requestID;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement reqStmt = conn.prepareStatement("INSERT INTO async_report_request (request_state, request_created, request_type, user_id) " +
                            "VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            reqStmt.setInt(1, WAITING_ASSIGN);
            UploadContextBlob blob = new UploadContextBlob();
            blob.setName(fileName);
            blob.setUploadContext(context);
            blob.setAnalysisItems(items);
            blob.setAccountVisible(accountVisible);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(blob);
            oos.flush();
            byte[] blobBytes = baos.toByteArray();

            reqStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            reqStmt.setInt(3, FILE_UPLOAD_CREATE);
            reqStmt.setLong(4, SecurityUtil.getUserID());
            reqStmt.execute();
            requestID = Database.instance().getAutoGenKey(reqStmt);
            reqStmt.close();
            PreparedStatement detStmt = conn.prepareStatement("INSERT INTO async_report_request_details (async_report_request_id, report_bytes, create_time) VALUES (?, ?, ?)");
            detStmt.setLong(1, requestID);
            detStmt.setBytes(2, blobBytes);
            detStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            detStmt.execute();
            detStmt.close();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        try {
            int elapsedTime = 0;
            Object result = null;
            while (result == null && elapsedTime < DATA_SOURCE_TIMEOUT) {
                result = MemCachedManager.instance().get("async" + requestID);
                if (result == null) {
                    elapsedTime += 100;
                    Thread.sleep(100);
                }
            }
            return (UploadResponse) result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static AnalyzeUploadResponse analyzeFileUpdate(long feedID, String uploadKey) {
        long requestID;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement reqStmt = conn.prepareStatement("INSERT INTO async_report_request (request_state, upload_key, data_source_id, request_created, request_type, user_id) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            reqStmt.setInt(1, WAITING_ASSIGN);
            reqStmt.setString(2, uploadKey);
            reqStmt.setLong(3, feedID);
            reqStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            reqStmt.setInt(5, FILE_REPLACE_ANALYZE);
            reqStmt.setLong(6, SecurityUtil.getUserID());
            reqStmt.execute();
            requestID = Database.instance().getAutoGenKey(reqStmt);
            reqStmt.close();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        try {
            int elapsedTime = 0;
            Object result = null;
            while (result == null && elapsedTime < DATA_SOURCE_TIMEOUT) {
                result = MemCachedManager.instance().get("async" + requestID);
                if (result == null) {
                    elapsedTime += 100;
                    Thread.sleep(100);
                }
            }
            return (AnalyzeUploadResponse) result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void fileUpdateData(long feedID, String uploadKey, boolean update, List<AnalysisItem> newFields) {
        long requestID;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement reqStmt = conn.prepareStatement("INSERT INTO async_report_request (request_state, upload_key, request_created, request_type, user_id) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            reqStmt.setInt(1, WAITING_ASSIGN);
            UploadContextBlob blob = new UploadContextBlob();
            blob.setDataSourceID(feedID);
            blob.setAnalysisItems(newFields);
            blob.setUpdate(update);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(blob);
            oos.flush();
            byte[] blobBytes = baos.toByteArray();
            reqStmt.setString(2, uploadKey);
            reqStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            reqStmt.setInt(4, FILE_REPLACE_UPLOAD);
            reqStmt.setLong(5, SecurityUtil.getUserID());
            reqStmt.execute();
            requestID = Database.instance().getAutoGenKey(reqStmt);
            reqStmt.close();
            PreparedStatement detStmt = conn.prepareStatement("INSERT INTO async_report_request_details (async_report_request_id, report_bytes, create_time) VALUES (?, ?, ?)");
            detStmt.setLong(1, requestID);
            detStmt.setBytes(2, blobBytes);
            detStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            detStmt.execute();
            detStmt.close();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        try {
            int elapsedTime = 0;
            Object result = null;
            while (result == null && elapsedTime < DATA_SOURCE_TIMEOUT) {
                result = MemCachedManager.instance().get("async" + requestID);
                if (result == null) {
                    elapsedTime += 100;
                    Thread.sleep(100);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void cacheRebuildAndWait(long cacheDataSourceID) {
        long requestID;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement reqStmt = conn.prepareStatement("INSERT INTO async_report_request (request_state, cache_source_id, request_created, request_type) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            reqStmt.setInt(1, WAITING_ASSIGN);
            reqStmt.setLong(2, cacheDataSourceID);
            reqStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            reqStmt.setInt(4, CACHE_REBUILD);
            reqStmt.execute();
            requestID = Database.instance().getAutoGenKey(reqStmt);
            reqStmt.close();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        try {
            int elapsedTime = 0;
            Object result = null;
            while (result == null && elapsedTime < DATA_SOURCE_TIMEOUT) {
                result = MemCachedManager.instance().get("async" + requestID);
                if (result == null) {
                    elapsedTime += 100;
                    Thread.sleep(100);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void cacheRebuild(long cacheDataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement reqStmt = conn.prepareStatement("INSERT INTO async_report_request (request_state, cache_source_id, request_created, request_type) VALUES (?, ?, ?, ?)");
            reqStmt.setInt(1, WAITING_ASSIGN);
            reqStmt.setLong(2, cacheDataSourceID);
            reqStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            reqStmt.setInt(4, CACHE_REBUILD);
            reqStmt.execute();
            reqStmt.close();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static void schedulerTask(long taskID) {

        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement reqStmt = conn.prepareStatement("INSERT INTO async_report_request (request_state, task_id, request_created, request_type) VALUES (?, ?, ?, ?)");
            reqStmt.setInt(1, WAITING_ASSIGN);
            reqStmt.setLong(2, taskID);
            reqStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            reqStmt.setInt(4, SCHEDULED_TASK);
            reqStmt.execute();
            reqStmt.close();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static boolean dataSourceRefresh(long sourceID, String callID) throws Exception {

        long requestID;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement reqStmt = conn.prepareStatement("INSERT INTO async_report_request (request_state, data_source_id, call_id, request_created, request_type, user_id) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            reqStmt.setInt(1, WAITING_ASSIGN);
            reqStmt.setLong(2, sourceID);
            reqStmt.setString(3, callID);
            reqStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            reqStmt.setInt(5, DATA_SOURCE_REFRESH);
            reqStmt.setLong(6, SecurityUtil.getUserID());
            reqStmt.execute();
            requestID = Database.instance().getAutoGenKey(reqStmt);
            reqStmt.close();

        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        int elapsedTime = 0;
        Object result = null;
        while (result == null && elapsedTime < DATA_SOURCE_TIMEOUT) {
            result = MemCachedManager.instance().get("async" + requestID);
            if (result == null) {
                elapsedTime += 100;
                Thread.sleep(100);
            }
        }
        if (result instanceof Boolean) {
            return (Boolean) result;
        } else if (result instanceof Exception) {
            throw (Exception) result;
        } else {
            throw new RuntimeException("The data source refresh timed out.");
        }
    }

    public static DataResults asyncDataResults(final WSAnalysisDefinition analysisDefinition, final InsightRequestMetadata insightRequestMetadata, EIConnection conn) {
        boolean success;
        try {
            success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (ReportException e) {
            ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));
            return embeddedDataResults;
        }
        try {
            long reqID = createAsyncRequestID(analysisDefinition, insightRequestMetadata, conn, REPORT_EDITOR);

            int elapsedTime = 0;
            ResultData dataResults = null;
            while (dataResults == null && elapsedTime < REPORT_TIMEOUT) {
                dataResults = (ResultData) MemCachedManager.instance().get("async" + reqID);
                if (dataResults == null) {
                    elapsedTime += 100;
                    Thread.sleep(100);
                }
            }
            if (dataResults == null) {
                LogClass.error("Timeout");
                ListDataResults embeddedDataResults = new ListDataResults();
                embeddedDataResults.setReportFault(new ServerError("The report timed out."));
                return embeddedDataResults;
            }
            dataResults.dataResults.setReport(dataResults.report);
            return dataResults.dataResults;
        } catch (Throwable e) {
            LogClass.error(e);
            ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(new ServerError("Something went wrong in running the report."));
            return embeddedDataResults;
        } finally {
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
        }
    }

    private static long createAsyncRequestID(WSAnalysisDefinition analysisDefinition, InsightRequestMetadata insightRequestMetadata, EIConnection conn, int requestType) throws SQLException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(analysisDefinition);
        oos.flush();
        byte[] reportBytes = baos.toByteArray();
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        oos.writeObject(insightRequestMetadata);
        oos.flush();
        byte[] metadata = baos.toByteArray();
        PreparedStatement reqStmt = conn.prepareStatement("INSERT INTO async_report_request (request_state, request_created, request_type, user_id) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        reqStmt.setInt(1, WAITING_ASSIGN);
        reqStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        reqStmt.setInt(3, requestType);
        reqStmt.setLong(4, SecurityUtil.getUserID());
        reqStmt.execute();
        long requestID = Database.instance().getAutoGenKey(reqStmt);
        reqStmt.close();
        PreparedStatement detStmt = conn.prepareStatement("INSERT INTO async_report_request_details (async_report_request_id, report_bytes, metadata_bytes, create_time) VALUES (?, ?, ?, ?)");
        detStmt.setLong(1, requestID);
        detStmt.setBytes(2, reportBytes);
        detStmt.setBytes(3, metadata);
        detStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        detStmt.execute();
        detStmt.close();
        return requestID;
    }

    public static DataResults asyncDataResults(final WSAnalysisDefinition analysisDefinition, final InsightRequestMetadata insightRequestMetadata) {
        boolean success;
        try {
            success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        } catch (ReportException e) {
            ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(new ServerError(e.getMessage()));
            return embeddedDataResults;
        }
        try {
            long reqID;
            EIConnection conn = Database.instance().getConnection();
            try {
                reqID = createAsyncRequestID(analysisDefinition, insightRequestMetadata, conn, REPORT_EDITOR);
            } finally {
                Database.closeConnection(conn);
            }

            int elapsedTime = 0;
            ResultData dataResults = null;
            while (dataResults == null && elapsedTime < REPORT_TIMEOUT) {
                dataResults = (ResultData) MemCachedManager.instance().get("async" + reqID);
                if (dataResults == null) {
                    elapsedTime += 100;
                    Thread.sleep(100);
                }
            }
            if (dataResults == null) {
                LogClass.error("Timeout");
                ListDataResults embeddedDataResults = new ListDataResults();
                embeddedDataResults.setReportFault(new ServerError("The report timed out."));
                return embeddedDataResults;
            }
            dataResults.dataResults.setReport(dataResults.report);
            return dataResults.dataResults;
        } catch (Throwable e) {
            LogClass.error(e);
            ListDataResults embeddedDataResults = new ListDataResults();
            embeddedDataResults.setReportFault(new ServerError("Something went wrong in running the report."));
            return embeddedDataResults;
        } finally {
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
        }
    }

    public static final int REPORT_TIMEOUT = 1000 * 60 * 5; // 300 seconds
    public static final int DATA_SOURCE_TIMEOUT = 1000 * 60 * 300; // 1000 * 60 * 60

    public static ResultData asyncDataSet(final WSAnalysisDefinition analysisDefinition, final InsightRequestMetadata insightRequestMetadata) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        try {
            long reqID;
            EIConnection conn = Database.instance().getConnection();
            try {
                reqID = createAsyncRequestID(analysisDefinition, insightRequestMetadata, conn, REPORT_DATA_SET);
            } finally {
                Database.closeConnection(conn);
            }

            int elapsedTime = 0;
            ResultData dataResults = null;
            while (dataResults == null && elapsedTime < REPORT_TIMEOUT) {
                dataResults = (ResultData) MemCachedManager.instance().get("async" + reqID);
                if (dataResults == null) {
                    elapsedTime += 100;
                    Thread.sleep(100);
                }
            }
            if (dataResults == null) {
                throw new RuntimeException("Timeout");
            }
            if (dataResults.exception != null) {
                throw dataResults.exception;
            }
            return dataResults;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
        }
    }

    public static ResultData asyncEndUserResults(final WSAnalysisDefinition analysisDefinition, final InsightRequestMetadata insightRequestMetadata) {
        boolean success = UserThreadMutex.mutex().acquire(SecurityUtil.getUserID(false));
        try {
            long reqID;
            EIConnection conn = Database.instance().getConnection();
            try {
                reqID = createAsyncRequestID(analysisDefinition, insightRequestMetadata, conn, REPORT_END_USER);
            } finally {
                Database.closeConnection(conn);
            }

            // requestor, start date,

            int elapsedTime = 0;
            ResultData dataResults = null;
            while (dataResults == null && elapsedTime < REPORT_TIMEOUT) {
                dataResults = (ResultData) MemCachedManager.instance().get("async" + reqID);
                if (dataResults == null) {
                    elapsedTime += 100;
                    Thread.sleep(100);
                }
            }
            if (dataResults == null) {
                LogClass.error("Timeout");
                EmbeddedDataResults embeddedDataResults = new EmbeddedDataResults();
                embeddedDataResults.setReportFault(new ServerError("The report timed out."));
                ResultData resultData = new ResultData();
                resultData.results = embeddedDataResults;
                return resultData;
            }
            if (dataResults.exception != null) {
                throw dataResults.exception;
            }
            return dataResults;
        } catch (ReportException re) {
            EmbeddedResults embeddedDataResults;
            if (analysisDefinition instanceof WSCrosstabDefinition) {
                embeddedDataResults = new EmbeddedCrosstabDataResults();
            } else if (analysisDefinition instanceof WSYTDDefinition) {
                embeddedDataResults = new EmbeddedYTDDataResults();
            } else if (analysisDefinition instanceof WSKPIDefinition) {
                embeddedDataResults = new EmbeddedTrendDataResults();
            } else if (analysisDefinition instanceof WSTreeDefinition) {
                embeddedDataResults = new EmbeddedTreeDataResults();
            } else if (analysisDefinition instanceof WSCompareYearsDefinition) {
                embeddedDataResults = new EmbeddedCompareYearsDataResults();
            } else if (analysisDefinition instanceof WSTextDefinition) {
                embeddedDataResults = new EmbeddedTextDataResults();
            } else {
                embeddedDataResults = new EmbeddedDataResults();
            }
            embeddedDataResults.setReportFault(re.getReportFault());
            ResultData resultData = new ResultData();
            resultData.results = embeddedDataResults;
            return resultData;
        } catch (Throwable e) {
            LogClass.error(e);
            EmbeddedResults embeddedDataResults;
            if (analysisDefinition instanceof WSCrosstabDefinition) {
                embeddedDataResults = new EmbeddedCrosstabDataResults();
            } else if (analysisDefinition instanceof WSYTDDefinition) {
                embeddedDataResults = new EmbeddedYTDDataResults();
            } else if (analysisDefinition instanceof WSKPIDefinition) {
                embeddedDataResults = new EmbeddedTrendDataResults();
            } else if (analysisDefinition instanceof WSTreeDefinition) {
                embeddedDataResults = new EmbeddedTreeDataResults();
            } else if (analysisDefinition instanceof WSCompareYearsDefinition) {
                embeddedDataResults = new EmbeddedCompareYearsDataResults();
            } else if (analysisDefinition instanceof WSTextDefinition) {
                embeddedDataResults = new EmbeddedTextDataResults();
            } else {
                embeddedDataResults = new EmbeddedDataResults();
            }
            embeddedDataResults.setReportFault(new ServerError("Something went wrong in running the report."));
            ResultData resultData = new ResultData();
            resultData.results = embeddedDataResults;
            return resultData;
        } finally {
            if (success) {
                UserThreadMutex.mutex().release(SecurityUtil.getUserID(false));
            }
        }
    }
}