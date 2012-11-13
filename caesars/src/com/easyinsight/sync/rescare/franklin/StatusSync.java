package com.easyinsight.sync.rescare.franklin;

import com.easyinsight.helper.*;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 7/6/12
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusSync extends HttpServlet {

    private String username;
    private String password;

    @Override
    public void init() throws ServletException {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        URL url = getClass().getClassLoader().getResource("passwords.properties");
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(url.getFile())));
            username = properties.get("rescare.username").toString();
            password = properties.get("rescare.password").toString();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("getting...");
        try {
            SyncRunnable syncRunnable = new SyncRunnable();
            syncRunnable.setDaemon(false);

            syncRunnable.start();
            SyncReferralStatus sr = new SyncReferralStatus();
            sr.setDaemon(false);
            WprSync wprSync = new WprSync(username, password);
            wprSync.setDaemon(false);

            EmploymentSync employmentSync = new EmploymentSync(username, password);
            employmentSync.setDaemon(false);

            sr.start();
            wprSync.start();
            employmentSync.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.setContentType("text/xml");
        resp.setStatus(200);
        resp.getOutputStream().write("<response>Started.</response>".getBytes());
        resp.getOutputStream().flush();
    }

    public static void main(String args[]) {
        try {
            new StatusSync().run().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Thread run() {
        try {
            init();
            SyncRunnable syncRunnable = new SyncRunnable();
            syncRunnable.setDaemon(false);

            syncRunnable.start();
            SyncReferralStatus sr = new SyncReferralStatus();
            sr.setDaemon(false);
            sr.start();
            return syncRunnable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class SyncReferralStatus extends Thread {
        @Override
        public void run() {
            try {
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                HttpClient client = new HttpClient();
                client.getParams().setAuthenticationPreemptive(true);
                System.out.println(username + ":" + password);
                Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
                GetMethod getMethod = new GetMethod("https://www.easy-insight.com/app/xml/reports/gLZlVqicWwHcEoHBLFWZ");
                client.executeMethod(getMethod);
                Document doc = new Builder().build(getMethod.getResponseBodyAsStream());

                Nodes nodes = doc.query("/response/rows/row");
                List<ReferralStatusRecord> fields = new ArrayList<ReferralStatusRecord>();
                DateFormat df = new SimpleDateFormat("MMM-dd-yy hh:mm aa");
                for (int i = 0; i < nodes.size(); i++) {
                    Node rowNode = nodes.get(i);
                    String participantID = rowNode.query("value[@field='Participant ID']").get(0).getValue();
                    String status = rowNode.query("value[@field='Referral Status']").get(0).getValue();
                    String countedDate = rowNode.query("value[@field='Counted Date']").get(0).getValue();
                    String originalRosterDate = rowNode.query("value[@field='Original Roster Date Formula']").get(0).getValue();
                    fields.add(new ReferralStatusRecord(participantID, status, countedDate, originalRosterDate));
                }

                DataSourceFactory dataSourceFactory = APIUtil.defineDataSource("tZRnXCwCAgQe", username, password);

                dataSourceFactory.addGrouping("ParticipantID");
                dataSourceFactory.addGrouping("ReferralStatus");
                dataSourceFactory.addDate("CountedDate");
                dataSourceFactory.addDate("OriginalRosterDate");

                DataSourceOperationFactory dataSourceOperationFactory = dataSourceFactory.defineDataSource();

                BulkUpdateDataSourceTarget dataSourceTarget = dataSourceOperationFactory.bulkUpdateRowsOperation();

                for (int i = 0; i < fields.size(); i++) {
                    ReferralStatusRecord record = fields.get(i);
                    if (!record.getOriginalRosterDate().equals("(Empty)")) {
                        Update update = dataSourceTarget.createUpdate();
                        DataRow dataRow = update.newRow();
                        dataRow.addValue("ParticipantID", record.getParticipantID());
                        dataRow.addValue("ReferralStatus", record.getStatus());
                        if (!record.getCountedDate().equals("(Empty)"))
                            dataRow.addValue("CountedDate", record.getCountedDate());
                        if (!record.getOriginalRosterDate().equals("(Empty)"))
                            dataRow.addValue("OriginalRosterDate", record.getOriginalRosterDate());


                        Date d = sdf.parse(record.getOriginalRosterDate());
                        Calendar c = Calendar.getInstance();
                        c.setTime(d);
                        update.setWhereClauses(new StringWhereClause("ParticipantID", record.getParticipantID()), new DayWhereClause("OriginalRosterDate", c.get(Calendar.DAY_OF_YEAR), c.get(Calendar.YEAR)));
                    }
                    if ((i % 5000) == 0) {
                        dataSourceTarget.flush();
                    }
                }

                dataSourceTarget.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SyncRunnable extends Thread {
        @Override
        public void run() {
            try {
                HttpClient client = new HttpClient();
                client.getParams().setAuthenticationPreemptive(true);
                System.out.println(username + ":" + password);
                Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
                GetMethod getMethod = new GetMethod("https://www.easy-insight.com/app/xml/reports/rQMdCPYQhNzOHBSrOmHE");
                client.executeMethod(getMethod);
                Document doc = new Builder().build(getMethod.getResponseBodyAsStream());

                Nodes nodes = doc.query("/response/rows/row");
                List<Record> fields = new ArrayList<Record>();
                for (int i = 0; i < nodes.size(); i++) {
                    Node rowNode = nodes.get(i);
                    String participantID = rowNode.query("value[@field='Participant ID']").get(0).getValue();
                    String status = rowNode.query("value[@field='Status']").get(0).getValue();
                    fields.add(new Record(participantID, status));

                }

                DataSourceFactory dataSourceFactory = APIUtil.defineDataSource("Participant Statuses", username, password);

                dataSourceFactory.addGrouping("ParticipantID");
                dataSourceFactory.addGrouping("Status");

                DataSourceOperationFactory dataSourceOperationFactory = dataSourceFactory.defineDataSource();

                DataSourceTarget dataSourceTarget = dataSourceOperationFactory.replaceRowsOperation();

                dataSourceTarget.flush();

                dataSourceTarget = dataSourceOperationFactory.addRowsOperation();

                for (int i = 0; i < fields.size(); i++) {
                    Record record = fields.get(i);
                    DataRow dataRow = dataSourceTarget.newRow();
                    dataRow.addValue("ParticipantID", record.getParticipantID());
                    dataRow.addValue("Status", record.getStatus());
                    if ((i % 5000) == 0) {
                        dataSourceTarget.flush();
                    }
                }

                dataSourceTarget.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class ReferralStatusRecord {
        private String participantID;
        private String status;
        private String countedDate;

        public String getOriginalRosterDate() {
            return originalRosterDate;
        }

        public void setOriginalRosterDate(String originalRosterDate) {
            this.originalRosterDate = originalRosterDate;
        }

        private String originalRosterDate;

        public String getParticipantID() {
            return participantID;
        }

        public void setParticipantID(String participantID) {
            this.participantID = participantID;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCountedDate() {
            return countedDate;
        }

        public void setCountedDate(String countedDate) {
            this.countedDate = countedDate;
        }

        private ReferralStatusRecord(String participantID, String status, String countedDate, String originalRosterDate) {
            this.participantID = participantID;
            this.status = status;
            this.countedDate = countedDate;
            this.originalRosterDate = originalRosterDate;
        }
    }

    private static class Record {
        private String participantID;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getParticipantID() {
            return participantID;
        }

        public void setParticipantID(String participantID) {
            this.participantID = participantID;
        }

        private Record(String participantID, String status) {
            this.participantID = participantID;
            this.status = status;
        }

        private Record() {

        }
    }
}
