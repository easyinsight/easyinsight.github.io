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
        if (account.getPricingModel() == 0) {
            if (targetType == Account.BASIC) {
                numberDesigners = 5;
            } else if (targetType == Account.PLUS) {
                numberDesigners = 500;
            } else if (targetType == Account.PROFESSIONAL) {
                numberDesigners = 500;
            } else {
                throw new RuntimeException("Unknown target type " + targetType);
            }
        } else {
            try {
                numberDesigners = Integer.parseInt(request.getParameter("numberDesigners"));
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorString", "The number of designers must be a number.");
                response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
                return;
            }
            if (numberDesigners <= 0) {
                request.getSession().setAttribute("errorString", "The number of designers must be a number greater than zero.");
                response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
                return;
            }
            if (targetType == Account.BASIC && numberDesigners > 3) {
                request.getSession().setAttribute("errorString", "The Basic account is limited to three designers.");
                response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
                return;
            } else if (targetType == Account.PLUS && numberDesigners > 10) {
                request.getSession().setAttribute("errorString", "The Plus account is limited to ten designers.");
                response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
                return;
            }
        }

        if (numberDesigners < account.getUsers().size()) {
            request.getSession().setAttribute("errorString", "Your account currently has more Designers than the number you just specified.");
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
            return;
        }

        int storageSelection = 0;
        long storage = 0;
        if (account.getPricingModel() == 0) {
            if (targetType == Account.BASIC) {
                storage = Account.BASIC_MAX;
            } else if (targetType == Account.PLUS) {
                storage = Account.PLUS_MAX;
            } else if (targetType == Account.PROFESSIONAL) {
                storage = Account.PROFESSIONAL_MAX;
            }
        } else {
            if (targetType == Account.PROFESSIONAL) {
                String storageSelectionString = request.getParameter("storageSelection");
                storageSelection = Integer.parseInt(storageSelectionString);
                if (storageSelection == 1) {
                    storage = Account.PROFESSIONAL_MAX;
                } else if (storageSelection == 2) {
                    storage = Account.PROFESSIONAL_MAX_2;
                } else if (storageSelection == 3) {
                    storage = Account.PROFESSIONAL_MAX_3;
                } else if (storageSelection == 4) {
                    storage = Account.PROFESSIONAL_MAX_4;
                } else {
                    request.getSession().setAttribute("errorString", "Illegal storage selection parameter.");
                    response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/accountType.jsp?error=true"));
                    return;
                }
            } else if (targetType == Account.PLUS) {
                storage = Account.PLUS_MAX;
            } else if (targetType == Account.BASIC) {
                storage = Account.BASIC_MAX;
            }
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