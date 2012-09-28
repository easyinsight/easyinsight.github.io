package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/24/12
 * Time: 5:02 PM
 */
public class CurrencyRetrieval {

    private Map<String, Double> ratesToEuro = new HashMap<String, Double>();

    private static CurrencyRetrieval instance;

    public static CurrencyRetrieval instance() {
        return instance;
    }

    public static void initialize() throws Exception {
        CurrencyRetrieval currencyRetrieval = new CurrencyRetrieval();
        currencyRetrieval.retrieve();
        instance = currencyRetrieval;
    }

    public double convertCurrency(String fromCurrencyCode, double amount, String toCurrencyCode) {
        Double toEuroRate = ratesToEuro.get(fromCurrencyCode);
        Double fromEuroRate = ratesToEuro.get(toCurrencyCode);
        if (toEuroRate == null || fromEuroRate == null) {
            return amount;
        }
        return amount * toEuroRate / fromEuroRate;
    }

    public void retrieve() throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT CURRENCY_CODE, CURRENCY_AMOUNT FROM CURRENCY_EXCHANGE_RATES");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String code = rs.getString(1);
                double amount = rs.getDouble(2);
                ratesToEuro.put(code, amount);
            }
        }  finally {
            Database.closeConnection(conn);
        }
        new Thread(new Runnable() {
            public void run() {
                EIConnection conn = Database.instance().getConnection();
                try {
                    conn.setAutoCommit(false);
                    PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM CURRENCY_EXCHANGE_RATES");
                    clearStmt.executeUpdate();
                    HttpClient httpClient = new HttpClient();
                    HttpMethod getMethod = new GetMethod("http://www.ecb.int/stats/eurofxref/eurofxref-daily.xml");
                    httpClient.executeMethod(getMethod);
                    String blah = getMethod.getResponseBodyAsString();
                    blah = blah.replace("xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\"", "");
                    blah = blah.replace("xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\"", "");
                    blah = blah.replace("gesmes:", "");
                    ByteArrayInputStream bais = new ByteArrayInputStream(blah.getBytes());
                    Document doc = new Builder().build(bais);
                    Nodes currencyNodes = doc.query("/Envelope/Cube/Cube/Cube");
                    PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO CURRENCY_EXCHANGE_RATES (CURRENCY_CODE, CURRENCY_AMOUNT) VALUES (?, ?)");
                    for (int i = 0; i < currencyNodes.size(); i++) {
                        Element element = (Element) currencyNodes.get(i);
                        ratesToEuro.put(element.getAttribute("currency").getValue(), Double.parseDouble(element.getAttribute("rate").getValue()));
                        saveStmt.setString(1, element.getAttribute("currency").getValue());
                        saveStmt.setDouble(2, Double.parseDouble(element.getAttribute("rate").getValue()));
                        saveStmt.execute();
                    }
                    ratesToEuro.put("EUR", 1.0);
                    conn.commit();
                } catch (Exception e) {
                    LogClass.error(e);
                    conn.rollback();
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                }
            }
        }).start();

    }

    public static void main(String[] args) throws Exception {
        new CurrencyRetrieval().retrieve();
    }
}
