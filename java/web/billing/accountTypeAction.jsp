<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.users.UserAccountAdminService" %>
<%@ page import="com.easyinsight.users.AccountTypeChange" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    EIConnection conn = Database.instance().getConnection();
    Session hibernateSession = Database.instance().createSession(conn);
    try {
        conn.setAutoCommit(false);
        request.getSession().removeAttribute("errorString");

        Account account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);

        int targetType = Integer.parseInt(request.getParameter("targetType"));
        if (targetType != Account.BASIC && targetType != Account.PLUS && targetType != Account.PROFESSIONAL) {
            request.getSession().setAttribute("errorString", "Unknown target account type passed as parameter.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }
        int numberDesigners;

        if (targetType == Account.BASIC) {
            numberDesigners = 5;
        } else if (targetType == Account.PLUS) {
            numberDesigners = 500;
        } else if (targetType == Account.PROFESSIONAL) {
            numberDesigners = 500;
        } else {
            throw new RuntimeException("Unknown target type " + targetType);
        }


        if (numberDesigners < account.getUsers().size()) {
            request.getSession().setAttribute("errorString", "Your account currently has more Designers than the number you just specified.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }

        int storageSelection = 0;
        long storage = 0;

        if (targetType == Account.BASIC) {
            storage = Account.BASIC_MAX;
        } else if (targetType == Account.PLUS) {
            storage = Account.PLUS_MAX;
        } else if (targetType == Account.PROFESSIONAL) {
            storage = Account.PROFESSIONAL_MAX;
            storageSelection = 1;
        }


        long usedStorage = new UserAccountAdminService().getAccountStorage();
        if (usedStorage > storage) {
            request.getSession().setAttribute("errorString", "You are currently using " + Account.humanReadableByteCount(usedStorage,  true) + " of storage, greater than the " + Account.humanReadableByteCount(storage, true) + " in the account change you just specified. You'll need to remove some data before making this change.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }

        String billingInterval = request.getParameter("billingInterval");
        boolean yearly = "2".equals(billingInterval);

        AccountTypeChange accountTypeChange = new AccountTypeChange();
        accountTypeChange.setYearly(yearly);
        accountTypeChange.setAccountType(targetType);
        accountTypeChange.setDesigners(numberDesigners);
        accountTypeChange.setStorage(storageSelection);
        session.setAttribute("accountTypeChange", accountTypeChange);

        if (account.getAccountState() == Account.TRIAL) {
            // apply now, redirect to "do you want to add billing information?"
            accountTypeChange.apply(account, hibernateSession);
            response.sendRedirect("wantBilling.jsp");
        } else if (account.getAccountState() == Account.DELINQUENT || account.getAccountState() == Account.BILLING_FAILED ||
                account.getAccountState() == Account.CLOSED) {
            // redirect to billing
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing?accountTypeChange=accountTypeChange"));
        } else {
            // redirect to confirm/credit
            response.sendRedirect("confirmAccountType.jsp");
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