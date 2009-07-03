<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.billing.BrainTreeBillingSystem" %>
<%@ page import="com.easyinsight.billing.BillingUtil" %>
<%@ page import="com.easyinsight.users.AccountCreditCardBillingInfo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.*" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Jun 25, 2009
  Time: 8:00:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String hashStr = request.getParameter("orderid") + "|" + request.getParameter("amount") + "|" + request.getParameter("response") + "|" + request.getParameter("transactionid") + "|" + request.getParameter("avsresponse") + "|" + request.getParameter("cvvresponse") + "|" + request.getParameter("customer_vault_id") + "|" + request.getParameter("time") + "|" + BillingUtil.getKey();
    String hashed = BillingUtil.MD5Hash(hashStr);


    if(!hashed.equals(request.getParameter("hash")) || !request.getParameter("response").equals("1") || !request.getParameter("cvvresponse").equals("M") || !Arrays.asList("X", "Y", "D", "M", "W", "Z", "P", "L").contains(request.getParameter("avsresponse")))
        response.sendRedirect("billing.jsp?error=true");
    else
    {
        long accountID = (Long) request.getSession().getAttribute("accountID");
        Account account = null;

        EIConnection conn = Database.instance().getConnection();
        conn.setAutoCommit(false);
        Session s = Database.instance().createSession(conn);
        try {
            account = (Account) s.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            if(!(account.isBillingInformationGiven() == null) && !account.isBillingInformationGiven()) {
                account.setBillingInformationGiven(true);
                s.save(account);
            }

            if(account.getAccountState() == Account.DELINQUENT) {
                account.setAccountState(Account.ACTIVE);
                AccountCreditCardBillingInfo info = new AccountCreditCardBillingInfo();
                info.setTransactionID(request.getParameter("transactionid"));
                info.setAmount(request.getParameter("amount"));
                info.setResponse(request.getParameter("response"));
                info.setResponseCode(request.getParameter("response_code"));
                info.setResponseString(request.getParameter("responsetext"));
                info.setAccountId(account.getAccountID());
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date transTime = df.parse(request.getParameter("time"));
                Calendar c = Calendar.getInstance();
                c.setTime(transTime);
                account.setBillingDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
                info.setTransactionTime(transTime);
                account.getBillingInfo().add(info);
                s.save(info);
                s.save(account);
            }

            s.flush();

            conn.commit();
        }
        catch(Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
        finally {
          conn.close();
        }
    }

%>
<html>

</html>