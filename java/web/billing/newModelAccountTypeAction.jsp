<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.users.NewModelAccountTypeChange" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.easyinsight.users.UserAccountAdminService" %>
<%@ page import="com.easyinsight.users.AccountStats" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    EIConnection conn = Database.instance().getConnection();
    Session hibernateSession = Database.instance().createSession(conn);
    try {
        conn.setAutoCommit(false);
        request.getSession().removeAttribute("errorString");

        Account account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);

        AccountStats stats = new UserAccountAdminService().getAccountStats(conn);

        int numberDesigners;
        try {
            numberDesigners = Integer.parseInt(request.getParameter("numberDesigners"));
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorString", "The number of designers must be a number.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }
        if (numberDesigners < 0) {
            request.getSession().setAttribute("errorString", "The number of designers must be a number greater than or equal to zero.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }

        if (numberDesigners > (stats.getUsedDesigners())) {
            request.getSession().setAttribute("errorString", "Your account currently has more Designers than the number you just specified.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }

        int numberConnections;
        try {
            numberConnections = Integer.parseInt(request.getParameter("numberConnections"));
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorString", "The number of connections must be a number.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }

        if (numberConnections > stats.getCurrentSmallBizConnections()) {
            request.getSession().setAttribute("errorString", "Your account currently has more small business connections than the number you just specified.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }

        int numberStorageBlocks;
        try {
            numberStorageBlocks = Integer.parseInt(request.getParameter("numberStorageBlocks"));
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorString", "The number of storage blocks must be a number.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }

        long used = account.getCoreStorage() + (numberStorageBlocks * 250000000L);
        if (used < stats.getUsedSpace()) {
            request.getSession().setAttribute("errorString", "Your account currently has more custom data storage in use than the amount of storage you just specified.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }

        String billingInterval = request.getParameter("billingInterval");
        boolean yearly = "2".equals(billingInterval);

        if (yearly && (account.getBillingMonthOfYear() == null || account.getBillingMonthOfYear() == 0) && account.isBillingInformationGiven()) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/monthlyToYearly.jsp"));
            return;
        } else if (!yearly && (account.getBillingMonthOfYear() != null && account.getBillingMonthOfYear() > 0)) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/monthlyToYearly.jsp"));
            return;
        }

        NewModelAccountTypeChange accountTypeChange = new NewModelAccountTypeChange();
        accountTypeChange.setYearly(yearly);
        accountTypeChange.setAddonConnections(numberConnections);
        accountTypeChange.setAddonDesigners(numberDesigners);
        accountTypeChange.setAddonStorage(numberStorageBlocks);
        session.setAttribute("accountTypeChange", accountTypeChange);

        if (account.getAccountState() == Account.TRIAL) {
            // apply now, redirect to "do you want to add billing information?"
            accountTypeChange.apply(account, hibernateSession);
            response.sendRedirect("newModelWantBilling.jsp");
        } else if (account.getAccountState() == Account.DELINQUENT || account.getAccountState() == Account.BILLING_FAILED ||
                account.getAccountState() == Account.CLOSED) {
            // redirect to billing
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing?accountTypeChange=accountTypeChange"));
        } else {
            // redirect to confirm/credit
            response.sendRedirect("newConfirmAccountType.jsp");
        }
        conn.commit();

    } catch (Exception e) {
        conn.rollback();
        throw e;
    } finally {
        hibernateSession.close();
        conn.setAutoCommit(true);
        Database.closeConnection(conn);
        SecurityUtil.clearThreadLocal();
    }
%>