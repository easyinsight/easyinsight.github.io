<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.AccountTypeChange" %>
<%@ page import="com.easyinsight.users.AccountCreditCardBillingInfo" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    EIConnection conn = Database.instance().getConnection();
    Session hibernateSession = Database.instance().createSession();
    try {
        conn.setAutoCommit(false);
        AccountTypeChange accountTypeChange = (AccountTypeChange) session.getAttribute("accountTypeChange");
        double cost;
        double credit;

        Account account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
        cost = Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(), accountTypeChange.getDesigners(),
                accountTypeChange.getStorage(), accountTypeChange.isYearly());
        credit = Account.calculateCredit(account);

        if (credit >= cost) {
            accountTypeChange.apply(account, hibernateSession);
            AccountCreditCardBillingInfo creditInfo = new AccountCreditCardBillingInfo();
            creditInfo.setAmount(String.valueOf(cost));
            creditInfo.setTransactionTime(new Date());
            creditInfo.setResponseCode("100");
            creditInfo.setDays(accountTypeChange.isYearly() ? 365 : 28);
            hibernateSession.save(creditInfo);
            hibernateSession.flush();
            session.removeAttribute("accountTypeChange");
            response.sendRedirect("done.jsp");
        } else {
            AccountCreditCardBillingInfo billingInfo = account.upgradeBill(accountTypeChange, cost - credit, hibernateSession);
            if ("100".equals(billingInfo.getResponseCode())) {
                session.removeAttribute("accountTypeChange");
                response.sendRedirect("done.jsp");
            } else {
                response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/index.jsp?accountTypeChange=accountTypeChange&error=error&response_code=" + billingInfo.getResponseCode()));
            }
        }
    } catch (Exception e) {
        conn.rollback();
        throw e;
    } finally {
        SecurityUtil.clearThreadLocal();
        hibernateSession.close();
        conn.setAutoCommit(true);
        Database.closeConnection(conn);
    }
%>