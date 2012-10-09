package com.easyinsight.crunchbase;

import com.easyinsight.helper.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/4/12
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class FinancialOrganizationLoader implements CommonConstants {

    public static final String NAME = "Name";
    public static final String PERMALINK = "Permalink";
    public static final String CRUNCHBASE_URL = "Crunchbase URL";
    public static final String HOMEPAGE_URL = "Homepage URL";
    public static final String BLOG_URL = "Blog URL";
    public static final String BLOG_FEED_URL = "Blog Feed URL";
    public static final String TWITTER_USERNAME = "Twitter Username";
    public static final String PHONE_NUMBER = "Phone Number";
    public static final String DESCRIPTION = "Description";
    public static final String EMAIL_ADDRESS = "Email Address";
    public static final String OVERVIEW = "Overview";
    public static final String NUMBER_OF_EMPLOYEES = "Number of Employees";
    public static final String FOUNDED = "Founded";
    public static final String CREATED_AT = "Created At";
    public static final String UPDATED_AT = "Updated At";
    public static final String TAG_LIST = "Tag List";
    public static final String ALIAS_LIST = "Alias List";
    public static final String FUND_NAME = "Fund Name";
    public static final String RAISED_CURRENCY_CODE = "Raised Currency Code";
    public static final String SOURCE_URL = "Source URL";
    public static final String SOURCE_DESCRIPTION = "Source Description";
    public static final String RAISED_AMOUNT = "Raised Amount";
    public static final String FUNDED = "Funded";
    private static final String FUNDING_SOURCE_PERMALINK = "Funding Source Permalink";

    private static DataSourceOperationFactory createFinancialOrgSource() {
        DataSourceFactory source = APIUtil.defineDataSource("Crunchbase Financial Orgs", API_KEY, API_SECRET_KEY);
        List<String> groupings = Arrays.asList(NAME, PERMALINK, CRUNCHBASE_URL, HOMEPAGE_URL, BLOG_URL, BLOG_FEED_URL,
                TWITTER_USERNAME, PHONE_NUMBER, DESCRIPTION, EMAIL_ADDRESS, OVERVIEW);

        for (String s : groupings) {
            source.addGrouping(s.replace(" ", "_"), s);
        }

        List<String> measures = Arrays.asList(NUMBER_OF_EMPLOYEES);

        for (String s : measures) {
            source.addMeasure(s.replace(" ", "_"), s);
        }

        List<String> dates = Arrays.asList(FOUNDED, CREATED_AT, UPDATED_AT);
        for (String s : dates) {
            source.addDate(s.replace(" ", "_"), s);
        }

        List<String> tags = Arrays.asList(TAG_LIST, ALIAS_LIST);
        for (String s : tags) {
            source.addTags(s.replace(" ", "_"), s);
        }

        return source.defineDataSource();  //To change body of created methods use File | Settings | File Templates.
    }

    private static DataSourceOperationFactory createFundsSource() {
        DataSourceFactory source = APIUtil.defineDataSource("Crunchbase Funds", API_KEY, API_SECRET_KEY);
        List<String> groupings = Arrays.asList(FUNDING_SOURCE_PERMALINK, FUND_NAME, RAISED_CURRENCY_CODE, SOURCE_URL, SOURCE_DESCRIPTION);
        for (String s : groupings) {
            source.addGrouping(s.replace(" ", "_"), s);
        }

        List<String> measures = Arrays.asList(RAISED_AMOUNT);

        for (String s : measures) {
            source.addMeasure(s.replace(" ", "_"), s);
        }

        List<String> dates = Arrays.asList(FUNDED);
        for (String s : dates) {
            source.addDate(s.replace(" ", "_"), s);
        }

        return source.defineDataSource();  //To change body of created methods use File | Settings | File Templates.
    }

    public static void main(String[] args) {
        try {
            loadFinancialOrganizations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadFinancialOrganizations() throws Exception {
        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod("http://api.crunchbase.com/v/1/financial-organizations.js?api_key=5aspbecghgbdjgmdpjufm6rs");
        httpClient.executeMethod(method);
        JSONArray response = (JSONArray) JSONValue.parse(method.getResponseBodyAsStream());
        int count = 0;
        DataSourceOperationFactory financialOrg = createFinancialOrgSource();
        DataSourceOperationFactory fundsSource = createFundsSource();

        final TransactionTarget financialOrgFactory = financialOrg.addRowsTransaction();
        final TransactionTarget fundsFactory = fundsSource.addRowsTransaction();

        financialOrgFactory.beginTransaction();
        fundsFactory.beginTransaction();

        final DateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final BlockingQueue<JSONObject> values = new LinkedBlockingQueue<JSONObject>();
        try {
            BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
            ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 5, 5000, TimeUnit.MINUTES, queue);
            final long size = response.size();
            final CountDownLatch latch = new CountDownLatch(response.size());
            final CountDownLatch latch1 = new CountDownLatch(response.size());

            for (Object v : response) {

                final Object ff = v;
                tpe.execute(new Runnable() {

                    public void run() {

                        HttpClient httpClient = new HttpClient();
                        JSONObject curVal = (JSONObject) ff;
                        try {
                            try {
                                HttpMethod companyData = new GetMethod("http://api.crunchbase.com/v/1/financial-organization/" + curVal.get("permalink") + ".js?api_key=5aspbecghgbdjgmdpjufm6rs");
                                httpClient.executeMethod(companyData);
                                Object o = JSONValue.parse(companyData.getResponseBodyAsStream());
                                JSONObject org = (JSONObject) o;
                                if (org.containsKey("error")) {
                                    System.out.println(curVal.get("permalink") + " - " + org.get("error"));
                                }
                                values.put(org);
                            } catch (Exception e) {
                                values.put(new JSONObject());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.countDown();

                        }

                    }
                });
            }
            Thread t = new Thread(new Runnable() {

                public void run() {
                    try {
                        JSONObject org;
                        while ((org = values.take()) != null) {
                            try {
                                DataRow row = financialOrgFactory.newRow();


                                String permalink = (String) org.get("permalink");
                                System.out.println(permalink);
                                addKeyVal(row, NAME, (String) org.get("name"));
                                addKeyVal(row, PERMALINK, permalink);
                                addKeyVal(row, CRUNCHBASE_URL, (String) org.get("crunchbase_url"));
                                addKeyVal(row, HOMEPAGE_URL, (String) org.get("blog_url"));
                                addKeyVal(row, BLOG_FEED_URL, (String) org.get("blog_feed_url"));
                                addKeyVal(row, TWITTER_USERNAME, (String) org.get("twitter_username"));
                                addKeyVal(row, PHONE_NUMBER, (String) org.get("phone_number"));
                                addKeyVal(row, DESCRIPTION, (String) org.get("description"));
                                addKeyVal(row, EMAIL_ADDRESS, (String) org.get("email_address"));
                                addKeyVal(row, OVERVIEW, (String) org.get("overview"));
                                if (org.get("number_of_employees") != null)
                                    addKeyVal(row, NUMBER_OF_EMPLOYEES, (Number) org.get("number_of_employees"));
                                addKeyVal(row, TAG_LIST, (String) org.get("tag_list"));
                                addKeyVal(row, ALIAS_LIST, (String) org.get("alias_list"));
                                addCbDate(row, org, "founded", FOUNDED);
                                if (org.get("created_at") != null)
                                    addKeyVal(row, CREATED_AT, sdf.parse((String) org.get("created_at")));
                                if (org.get("updated_at") != null)
                                    addKeyVal(row, UPDATED_AT, sdf.parse((String) org.get("updated_at")));

                                JSONArray funds = (JSONArray) org.get("funds");
                                for (Object j : funds) {
                                    JSONObject fund = (JSONObject) j;
                                    DataRow fundRow = fundsFactory.newRow();
                                    addKeyVal(fundRow, FUNDING_SOURCE_PERMALINK, permalink);
                                    addKeyVal(fundRow, FUND_NAME, (String) fund.get("name"));
                                    addKeyVal(fundRow, RAISED_CURRENCY_CODE, (String) fund.get("raised_currency_code"));
                                    addKeyVal(fundRow, SOURCE_URL, (String) fund.get("source_url"));
                                    addKeyVal(fundRow, SOURCE_DESCRIPTION, (String) fund.get("source_description"));
                                    addCbDate(fundRow, fund, "funded", FUNDED);
                                    if (fund.get("raised_amount") != null)
                                        addKeyVal(fundRow, RAISED_AMOUNT, (Number) fund.get("raised_amount"));
                                }


                            } catch (Exception e) {
                                if (e instanceof InterruptedException)
                                    throw e;
                                else
                                    e.printStackTrace();

                            } finally {
                                latch1.countDown();
                                System.out.println(Double.valueOf(size - latch1.getCount()) / Double.valueOf(size));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            latch.await();
            latch1.await();
            t.interrupt();
            financialOrgFactory.flush();
            financialOrgFactory.commit();
            fundsFactory.flush();
            fundsFactory.commit();



        } catch (Exception e) {
            e.printStackTrace();
            financialOrgFactory.rollback();
            fundsFactory.rollback();
        }
    }

    private static void addKeyVal(DataRow row, String key, String value) {
        row.addValue(key.replace(" ", "_"), value);
    }

    private static void addKeyVal(DataRow row, String key, Number value) {
        row.addValue(key.replace(" ", "_"), value);
    }

    private static void addKeyVal(DataRow row, String key, Date value) {
        row.addValue(key.replace(" ", "_"), value);
    }


    private static void addCbDate(DataRow row, JSONObject company, String prefix, String key) {
        Calendar c = getCbDate(company, prefix);

        if (company.get(prefix + "_year") != null || company.get(prefix + "_month") != null || company.get(prefix + "_day") != null) {
            row.addValue(key.replace(" ", "_"), c.getTime());
        }
    }

    private static Calendar getCbDate(JSONObject company, String prefix) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(0));
        Integer year = (Integer) company.get(prefix + "_year");
        if (year != null) {
            c.set(Calendar.YEAR, year);
        }
        Integer month = (Integer) company.get(prefix + "_month");
        if (month != null) {
            c.set(Calendar.MONTH, month);
        }
        Integer day = (Integer) company.get(prefix + "_day");
        if (day != null) {
            c.set(Calendar.DAY_OF_MONTH, day);
        }
        return c;
    }


}
