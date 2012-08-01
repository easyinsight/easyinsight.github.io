package com.easyinsight.sync;

import com.easyinsight.helper.*;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.ServletConfig;
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
 * User: jamesboe
 * Date: 4/1/12
 * Time: 11:58 AM
 */
public class VoseServlet extends HttpServlet {

    private String username;
    private String password;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        URL url = getClass().getClassLoader().getResource("passwords.properties");
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(url.getFile())));
            username = properties.get("vose.username").toString();
            password = properties.get("vose.password").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SyncRunnable syncRunnable = new SyncRunnable();
        syncRunnable.setDaemon(false);
        syncRunnable.start();
        resp.setContentType("text/xml");
        resp.setStatus(200);
        resp.getOutputStream().write("<response>Started.</response>".getBytes());
        resp.getOutputStream().flush();
    }
    
    private class SyncRunnable extends Thread {
        @Override
        public void run() {
            try {
                HttpClient client = new HttpClient();
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
                GetMethod getMethod = new GetMethod("https://www.easy-insight.com/app/reports/DzCnRMVfWLHSBJmxEWTX.xml");
                client.executeMethod(getMethod);
                Document doc = new Builder().build(getMethod.getResponseBodyAsStream());
                Nodes nodes = doc.query("/response/rows/row");
                DateFormat df = new SimpleDateFormat("MMM-dd-yy hh:mm aa");
                List<Info> infos = new ArrayList<Info>();
                for (int i = 0; i < nodes.size(); i++) {
                    Node rowNode = nodes.get(i);
                    Nodes values = rowNode.query("value");
                    String projectID = "";
                    String year = "";
                    Map<Integer, MonthRow> map = new HashMap<Integer, MonthRow>();
                    for (int j = 0; j < values.size(); j++) {
                        Element value = (Element) values.get(j);
                        String fieldName = value.getAttribute("field").getValue();
                        if (fieldName.equals("Detail ID#")) {
                            projectID = value.getValue();
                        } else if (fieldName.equals("MIP Details - Year (2011,2012)")) {
                            year = value.getValue();
                        } else {
                            String typeString = fieldName.substring(fieldName.length() - 3);
                            String monthString = fieldName.substring(fieldName.length() - 6, fieldName.length() - 3);
                            int month = 0;
                            if (monthString.equals("Jan")) {
                                month = Calendar.JANUARY;
                            } else if (monthString.equals("Feb")) {
                                month = Calendar.FEBRUARY;
                            } else if (monthString.equals("Mar")) {
                                month = Calendar.MARCH;
                            } else if (monthString.equals("Apr")) {
                                month = Calendar.APRIL;
                            } else if (monthString.equals("May")) {
                                month = Calendar.MAY;
                            } else if (monthString.equals("Jun")) {
                                month = Calendar.JUNE;
                            } else if (monthString.equals("Jul")) {
                                month = Calendar.JULY;
                            } else if (monthString.equals("Aug")) {
                                month = Calendar.AUGUST;
                            } else if (monthString.equals("Sep")) {
                                month = Calendar.SEPTEMBER;
                            } else if (monthString.equals("Oct")) {
                                month = Calendar.OCTOBER;
                            } else if (monthString.equals("Nov")) {
                                month = Calendar.NOVEMBER;
                            } else if (monthString.equals("Dec")) {
                                month = Calendar.DECEMBER;
                            }
                            MonthRow monthRow = map.get(month);
                            if (monthRow == null) {
                                monthRow = new MonthRow();
                                map.put(month, monthRow);
                            }
                            if (typeString.equals("Act")) {
                                monthRow.actual = Double.parseDouble(value.getValue());
                            } else if (typeString.equals("Adj")) {
                                monthRow.adjusted = Double.parseDouble(value.getValue());
                            } else if (typeString.equals("Org")) {
                                monthRow.forecast = Double.parseDouble(value.getValue());
                            }
                        }
                    }
                    Info info = new Info();
                    info.projectID = projectID;
                    info.map = map;
                    info.year = year;
                    infos.add(info);
                }
                System.out.println("info size = " + infos.size());

                DataSourceFactory dataSourceFactory = APIUtil.defineDataSource("Parsed MIPs", username, password);

                dataSourceFactory.addGrouping("SourceMipDetailID");
                dataSourceFactory.addDate("Date");
                dataSourceFactory.addMeasure("TranslatedActual");
                dataSourceFactory.addMeasure("TranslatedOriginal");
                dataSourceFactory.addMeasure("TranslatedAdjusted");

                DataSourceOperationFactory dataSourceOperationFactory = dataSourceFactory.defineDataSource();

                DataSourceTarget dataSourceTarget = dataSourceOperationFactory.replaceRowsOperation();

                dataSourceTarget.flush();

                dataSourceTarget = dataSourceOperationFactory.addRowsOperation();

                for (int i = 0; i < infos.size(); i++) {
                    Info info = infos.get(i);


                    for (Map.Entry<Integer, MonthRow> entry : info.map.entrySet()) {
                        if (!"0.0".equals(info.year)) {
                            try {
                                DataRow dataRow = dataSourceTarget.newRow();
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.MONTH, entry.getKey());
                                cal.set(Calendar.DAY_OF_MONTH, 15);
                                System.out.println(info.year);
                                if (info.year != null && !"0".equals(info.year)) {
                                    cal.set(Calendar.YEAR, Integer.parseInt(info.year.substring(0, 4)));
                                }
                                dataRow.addValue("Date", cal.getTime());
                                dataRow.addValue("SourceMipDetailID", info.projectID);
                                dataRow.addValue("TranslatedActual", entry.getValue().actual);
                                dataRow.addValue("TranslatedOriginal", entry.getValue().forecast);
                                dataRow.addValue("TranslatedAdjusted", entry.getValue().adjusted);
                            } catch (Exception e) {
                                // ignore
                            }
                        }
                    }
                    if ((i % 5000) == 0) {
                        System.out.println("flushing");
                        dataSourceTarget.flush();
                    }
                }


                dataSourceTarget.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class MonthRow {
        private double forecast;
        private double adjusted;
        private double actual;
    }

    private static class Info {
        private Map<Integer, MonthRow> map;
        private String projectID;
        private String year;
    }
}
