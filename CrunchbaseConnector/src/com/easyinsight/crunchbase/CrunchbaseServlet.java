package com.easyinsight.crunchbase;

import com.easyinsight.helper.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
 * Date: 9/26/12
 * Time: 8:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrunchbaseServlet extends HttpServlet implements CommonConstants {

    public static final String CRUNCHBASE_NAME = "Name";
    public static final String PERMALINK = "Permalink";
    public static final String CRUNCHBASE_URL = "Crunchbase URL";
    public static final String HOMEPAGE_URL = "Homepage URL";
    public static final String TWITTER_USERNAME = "Twitter Username";
    public static final String CATEGORY_CODE = "Category Code";
    public static final String EMAIL_ADDRESS = "Email Address";
    public static final String PHONE_NUMBER = "Phone Number";
    public static final String DESCRIPTION = "Description";
    public static final String TOTAL_MONEY_RAISED = "Total Money Raised";
    public static final List<String> GROUPINGS = Arrays.asList(CRUNCHBASE_NAME, PERMALINK, CRUNCHBASE_URL, HOMEPAGE_URL, TWITTER_USERNAME,
            CATEGORY_CODE, EMAIL_ADDRESS, PHONE_NUMBER, DESCRIPTION, TOTAL_MONEY_RAISED);
    public static final String NUMBER_OF_EMPLOYEES = "Number of Employees";
    public static final String FOUNDED_DATE = "Founded Date";
    public static final String DEADPOOLED_DATE = "Deadpooled Date";
    public static final String CREATED_AT = "Created At";
    public static final String UPDATED_AT = "Updated At";
    public static final String TAG_LIST = "Tag List";

    public static final String ROUND_CODE = "Round Code";
    public static final String ROUND_SOURCE_URL = "Round Source URL";
    public static final String SOURCE_DESCRIPTION = "Source Description";
    public static final String RAISED_CURRENCY_CODE = "Raised Currency Code";
    public static final String RAISED_AMOUNT = "Raised Amount";
    public static final String FUNDED_DATE = "Funded Date";
    private static final String ROUND_PERMALINK = "Round Permalink";
    private static final String ROUND_KEY = "Round Key";
    public static final String FINANCIAL_ORG_PERMALINK = "Financial Org Permalink";
    public static final String FUNDING_ROUND_KEY = "Funding Round Key";
    public static final String COMPANY_PERMALINK = "Company Permalink";


    @Override
    public void init() throws ServletException {
        super.init();


    }

    public static void main(String args[]) {
        try {
            loadCrunchbaseData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private static void loadCrunchbaseData() throws Exception {

        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod("http://api.crunchbase.com/v/1/companies.js?api_key=5aspbecghgbdjgmdpjufm6rs");
        httpClient.executeMethod(method);
        JSONArray response = (JSONArray) JSONValue.parse(method.getResponseBodyAsStream());
        int count = 0;
        DataSourceOperationFactory companySource = createCompanySource();
        DataSourceOperationFactory fundingSource = createFundingSource();
        DataSourceOperationFactory investmentSource = createInvestmentSource();


        final TransactionTarget companyRowFactory = companySource.addRowsTransaction();
        final TransactionTarget fundingSourceRowFactory = fundingSource.addRowsTransaction();
        final TransactionTarget investmentSourceRowFactory = investmentSource.addRowsTransaction();

        fundingSourceRowFactory.beginTransaction();
        companyRowFactory.beginTransaction();
        investmentSourceRowFactory.beginTransaction();


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
                        try {
                            HttpClient httpClient = new HttpClient();
                            JSONObject curVal = (JSONObject) ff;
                            HttpMethod companyData = new GetMethod("http://api.crunchbase.com/v/1/company/" + curVal.get("permalink") + ".js?api_key=5aspbecghgbdjgmdpjufm6rs");
                            httpClient.executeMethod(companyData);
                            String s = companyData.getResponseBodyAsString();
                            try {
                                JSONObject company = (JSONObject) JSONValue.parse(s);
                                values.put(company);
                            } catch(Exception e) {

                                e.printStackTrace();
                                System.out.println(s);
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
                                        JSONObject company;
                                        while((company = values.take()) != null) {
                                            DataRow row = companyRowFactory.newRow();
                                            System.out.println(company.get("permalink"));
                                            row.addValue(CRUNCHBASE_NAME.replace(" ", "_"), (String) company.get("name"));
                                            row.addValue(PERMALINK.replace(" ", "_"), (String) company.get("permalink"));
                                            row.addValue(CRUNCHBASE_URL.replace(" ", "_"), (String) company.get("crunchbase_url"));
                                            row.addValue(HOMEPAGE_URL.replace(" ", "_"), (String) company.get("homepage_url"));
                                            row.addValue(TWITTER_USERNAME.replace(" ", "_"), (String) company.get("twitter_username"));
                                            row.addValue(CATEGORY_CODE.replace(" ", "_"), (String) company.get("category_code"));
                                            row.addValue(EMAIL_ADDRESS.replace(" ", "_"), (String) company.get("email_address"));
                                            row.addValue(PHONE_NUMBER.replace(" ", "_"), (String) company.get("phone_number"));
                                            row.addValue(DESCRIPTION.replace(" ", "_"), (String) company.get("description"));
                                            row.addValue(TOTAL_MONEY_RAISED.replace(" ", "_"), (String) company.get("total_money_raised"));
                                            if (company.get("number_of_employees") != null)
                                                row.addValue(NUMBER_OF_EMPLOYEES.replace(" ", "_"), (Integer) company.get("number_of_employees"));
                                            addCbDate(row, company, "founded", FOUNDED_DATE);
                                            addCbDate(row, company, "deadpooled", DEADPOOLED_DATE);
                                            if (company.get("created_at") != null)
                                                row.addValue(CREATED_AT.replace(" ", "_"), sdf.parse((String) company.get("created_at")));
                                            if (company.get("updated_at") != null)
                                                row.addValue(UPDATED_AT.replace(" ", "_"), sdf.parse((String) company.get("updated_at")));

                                            row.addValue(TAG_LIST.replace(" ", "_"), (String) company.get("tag_list"));

                                            JSONArray rounds = (JSONArray) company.get("funding_rounds");
                                            for (Object r : rounds) {
                                                JSONObject round = (JSONObject) r;
                                                DataRow roundRow = fundingSourceRowFactory.newRow();
                                                String roundKey = ((String) round.get("round_code")) + ((String) company.get("permalink")) + String.valueOf(getCbDate(round, "funded").getTime().getTime());
                                                roundRow.addValue(ROUND_PERMALINK.replace(" ", "_"), (String) company.get("permalink"));
                                                roundRow.addValue(ROUND_KEY.replace(" ", "_"), roundKey);
                                                roundRow.addValue(ROUND_SOURCE_URL.replace(" ", "_"), (String) round.get("source_url"));
                                                roundRow.addValue(ROUND_CODE.replace(" ", "_"), (String) round.get("round_code"));
                                                roundRow.addValue(SOURCE_DESCRIPTION.replace(" ", "_"), (String) round.get("source_description"));
                                                roundRow.addValue(RAISED_CURRENCY_CODE.replace(" ", "_"), (String) round.get("raised_currency_code"));
                                                if (round.get("raised_amount") != null)
                                                    roundRow.addValue(RAISED_AMOUNT.replace(" ", "_"), (Number) round.get("raised_amount"));
                                                addCbDate(roundRow, round, "funded", FUNDED_DATE);

                                                JSONArray investors = (JSONArray) round.get("investments");
                                                for (Object i : investors) {
                                                    JSONObject investor = (JSONObject) i;

                                                    String financialOrg = "";
                                                    JSONObject org = (JSONObject) investor.get("financial_org");
                                                    if (org != null) {
                                                        financialOrg = (String) org.get("permalink");
                                                    }
                                                    String companyPermalink = "";
                                                    JSONObject investingCompany = (JSONObject) investor.get("company");
                                                    if (investingCompany != null) {
                                                        companyPermalink = (String) investingCompany.get("permalink");
                                                    }

                                                    if (!(companyPermalink.equals("") && financialOrg.equals(""))) {
                                                        DataRow investorRow = investmentSourceRowFactory.newRow();
                                                        investorRow.addValue(FINANCIAL_ORG_PERMALINK.replace(" ", "_"), financialOrg);
                                                        investorRow.addValue(FUNDING_ROUND_KEY.replace(" ", "_"), roundKey);
                                                        investorRow.addValue(COMPANY_PERMALINK.replace(" ", "_"), companyPermalink);
                                                    }
                                                }

                                            }
                                            latch1.countDown();
                                            System.out.println(Double.valueOf(size - latch1.getCount()) / Double.valueOf(size));
                                        }
                                    } catch (Exception i) {
                                        i.printStackTrace();
                                    }
                                }
                            });
            t.start();

            latch.await();
            latch1.await();
            t.interrupt();
            companyRowFactory.flush();
            companyRowFactory.commit();
            fundingSourceRowFactory.flush();
            fundingSourceRowFactory.commit();
            investmentSourceRowFactory.flush();
            investmentSourceRowFactory.commit();
        } catch (Exception e) {
            e.printStackTrace();
            companyRowFactory.rollback();
            fundingSourceRowFactory.rollback();
            investmentSourceRowFactory.rollback();
        }
    }


    private static DataSourceOperationFactory createInvestmentSource() {
        DataSourceFactory investmentSources = APIUtil.defineDataSource("Crunchbase Investments", API_KEY, API_SECRET_KEY);
        List<String> groupings = Arrays.asList(FINANCIAL_ORG_PERMALINK, FUNDING_ROUND_KEY, COMPANY_PERMALINK);
        for (String s : groupings) {
            investmentSources.addGrouping(s.replace(" ", "_"), s);
        }
        return investmentSources.defineDataSource();
    }

    private static DataSourceOperationFactory createFundingSource() {
        DataSourceFactory fundingSources = APIUtil.defineDataSource("Crunchbase Funding Source Test", API_KEY, API_SECRET_KEY);

        List<String> groupings = Arrays.asList(ROUND_PERMALINK, ROUND_KEY, ROUND_CODE, ROUND_SOURCE_URL, SOURCE_DESCRIPTION, RAISED_CURRENCY_CODE);
        for (String s : groupings) {
            fundingSources.addGrouping(s.replace(" ", "_"), s);
        }
        List<String> measures = Arrays.asList(RAISED_AMOUNT);
        for (String s : measures) {
            fundingSources.addMeasure(s.replace(" ", "_"), s);
        }
        List<String> dates = Arrays.asList(FUNDED_DATE);
        for (String s : dates) {
            fundingSources.addDate(s.replace(" ", "_"), s);
        }


        return fundingSources.defineDataSource();
    }

    private static DataSourceOperationFactory createCompanySource() {
        DataSourceFactory companies = APIUtil.defineDataSource("Crunchbase Companies Test", API_KEY, API_SECRET_KEY);

        // TODO: Total money raised becomes a measure

        for (String s : GROUPINGS) {
            companies.addGrouping(s.replace(" ", "_"), s);
        }
        List<String> measures = Arrays.asList(NUMBER_OF_EMPLOYEES);
        for (String s : measures) {
            companies.addMeasure(s.replace(" ", "_"), s);
        }
        List<String> dates = Arrays.asList(FOUNDED_DATE, DEADPOOLED_DATE, CREATED_AT, UPDATED_AT);
        for (String s : dates) {
            companies.addDate(s.replace(" ", "_"), s);
        }
        List<String> tags = Arrays.asList(TAG_LIST);
        for (String s : tags) {
            companies.addTags(s.replace(" ", "_"), s);
        }


        return companies.defineDataSource();
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
