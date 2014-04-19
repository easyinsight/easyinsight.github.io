package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountCreditCardBillingInfo;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Oct 28, 2010
 * Time: 11:22:02 AM
 */
public class InvoiceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            Long invoiceID = Long.parseLong(req.getParameter("invoiceID"));
            EIConnection conn = Database.instance().getConnection();
            Session session = Database.instance().createSession(conn);
            try {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT AMOUNT, " +
                        "RESPONSE, RESPONSE_CODE, RESPONSE_STRING, SUCCESSFUL, TRANSACTION_TIME, TRANSACTION_ID FROM ACCOUNT_CREDIT_CARD_BILLING_INFO, USER " +
                        "WHERE ACCOUNT_CREDIT_CARD_BILLING_INFO_ID = ? AND " +
                            "ACCOUNT_CREDIT_CARD_BILLING_INFO.ACCOUNT_ID = USER.ACCOUNT_ID AND USER.USER_ID = ?");
                queryStmt.setLong(1, invoiceID);
                queryStmt.setLong(2, SecurityUtil.getUserID());
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    String amount = rs.getString(1);
                    String response = rs.getString(2);
                    String responseCode = rs.getString(3);
                    String responseString = rs.getString(4);
                    boolean successful = rs.getBoolean(5);
                    Date transactionTime = rs.getTimestamp(6);
                    String transactionID = rs.getString(7);
                    Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
                    AccountCreditCardBillingInfo accountCreditCardBillingInfo = new AccountCreditCardBillingInfo();
                    accountCreditCardBillingInfo.setAmount(amount);
                    accountCreditCardBillingInfo.setResponse(response);
                    accountCreditCardBillingInfo.setResponseCode(responseCode);
                    accountCreditCardBillingInfo.setResponseString(responseString);
                    accountCreditCardBillingInfo.setSuccessful(successful);
                    accountCreditCardBillingInfo.setTransactionTime(transactionTime);
                    accountCreditCardBillingInfo.setTransactionID(transactionID);
                    byte[] bytes = new InvoiceUtil().createInvoicePDF(accountCreditCardBillingInfo, account);
                    resp.setContentType("application/x-download");
                    resp.setContentLength(bytes.length);
                    String reportName = "invoice";
                    reportName = URLEncoder.encode(reportName, "UTF-8");
                    resp.setHeader("Content-Disposition","attachment; filename=" + reportName+".pdf" );
                    String userAgent = req.getHeader("User-Agent");
                    if (userAgent != null && userAgent.contains("MSIE 8.0")) {
                        resp.setHeader("Cache-Control","private"); //HTTP 1.1
                        resp.setHeader("Pragma","token"); //HTTP 1.0
                    } else {
                        resp.setHeader("Cache-Control","no-cache"); //HTTP 1.1
                        resp.setHeader("Pragma","no-cache"); //HTTP 1.0
                    }
                    resp.setDateHeader ("Expires", 0); //prevents caching at the proxy server
                    resp.getOutputStream().write(bytes);
                    resp.getOutputStream().flush();
                }
            } catch (Exception e) {
                LogClass.error(e);
            } finally {
                session.close();
                Database.closeConnection(conn);
            }
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}
