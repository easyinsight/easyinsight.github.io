<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.users.AccountTypeChange" %>
<%@ page import="com.easyinsight.users.AccountCreditCardBillingInfo" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="org.joda.time.Days" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    Session hibernateSession = Database.instance().createSession();
    try {
        hibernateSession.beginTransaction();
        AccountTypeChange accountTypeChange = (AccountTypeChange) session.getAttribute("accountTypeChange");
        double cost;
        double credit;

        Account account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);

        Calendar cal = Calendar.getInstance();
        Integer billingMonthOfYear = account.getBillingMonthOfYear();
        if (accountTypeChange.isYearly() && billingMonthOfYear == null) {
            billingMonthOfYear = cal.get(Calendar.MONTH);
        } else if (!accountTypeChange.isYearly() && billingMonthOfYear != null) {
            billingMonthOfYear = null;
        }
        cal.set(Calendar.DAY_OF_MONTH, account.getBillingDayOfMonth());
        if (billingMonthOfYear != null) {
            cal.set(Calendar.MONTH, billingMonthOfYear);
        }

        if (cal.getTime().before(new Date())) {
            if (accountTypeChange.isYearly()) {
                cal.add(Calendar.YEAR, 1);
            } else {
                cal.add(Calendar.MONTH, 1);
            }
        }

        Date date = cal.getTime();
        DateTime lastTime = new DateTime(date);
        DateTime now = new DateTime(System.currentTimeMillis());
        int daysBetween = Days.daysBetween(now, lastTime).getDays();

        // how many days until the next billing cycle?

        cost = Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(), accountTypeChange.getDesigners(),
                accountTypeChange.getStorage(), accountTypeChange.isYearly());
        credit = Account.calculateCredit(account);

        double proratedCost = cost * ((double) daysBetween / (double) ((accountTypeChange.isYearly() ? 365 : 31)));

        int storageType = 1;
        if (account.getAccountType() == Account.PROFESSIONAL) {
            if (account.getMaxSize() == Account.PROFESSIONAL_MAX_2) {
                storageType = 2;
            } else if (account.getMaxSize() == Account.PROFESSIONAL_MAX_3) {
                storageType = 3;
            } else if (account.getMaxSize() == Account.PROFESSIONAL_MAX_4) {
                storageType = 4;
            }
        } else if (account.getAccountType() == Account.PLUS) {
            if (account.getMaxSize() == Account.PLUS_MAX2) {
                storageType = 2;
            } else if (account.getMaxSize() == Account.PLUS_MAX3) {
                storageType = 3;
            }
        } else if (account.getAccountType() == Account.BASIC) {
            if (account.getMaxSize() == Account.BASIC_MAX2) {
                storageType = 2;
            } else if (account.getMaxSize() == Account.BASIC_MAX3) {
                storageType = 3;
            }
        }
        double priorCost = Account.createTotalCost(account.getPricingModel(), account.getAccountType(), account.getMaxUsers(),
                storageType, accountTypeChange.isYearly());


        String target;
        boolean removeAttribute = false;
        if (priorCost >= cost) {
            account.setBillingMonthOfYear(billingMonthOfYear);
            accountTypeChange.apply(account, hibernateSession);
            removeAttribute = true;
            target = "done.jsp";
        } else if (credit >= proratedCost) {
            accountTypeChange.apply(account, hibernateSession);
            // it'll bill on the next credit
            AccountCreditCardBillingInfo creditInfo = new AccountCreditCardBillingInfo();
            creditInfo.setAccountId(account.getAccountID());
            creditInfo.setAmount(String.valueOf(proratedCost));
            creditInfo.setTransactionTime(new Date());
            creditInfo.setResponseCode("100");
            creditInfo.setAgainstCredit(true);
            account.setBillingMonthOfYear(billingMonthOfYear);

            cal.set(Calendar.DAY_OF_MONTH, account.getBillingDayOfMonth());
            if (billingMonthOfYear != null) {
                cal.set(Calendar.MONTH, billingMonthOfYear);
            }

            if (cal.getTime().before(new Date())) {
                if (accountTypeChange.isYearly()) {
                    cal.add(Calendar.YEAR, 1);
                } else {
                    cal.add(Calendar.MONTH, 1);
                }
            }

            creditInfo.setDays(daysBetween);
            hibernateSession.save(creditInfo);
            hibernateSession.flush();
            removeAttribute = true;
            target = "done.jsp";
        } else {
            AccountCreditCardBillingInfo billingInfo = account.upgradeBill(accountTypeChange, proratedCost - credit, hibernateSession);
            if ("100".equals(billingInfo.getResponseCode())) {
                removeAttribute = true;
                target = "done.jsp";
            } else {
                target = RedirectUtil.getURL(request, "/app/billing/index.jsp?accountTypeChange=accountTypeChange&error=error&response_code=" + billingInfo.getResponseCode());
            }
        }
        hibernateSession.update(account);
        hibernateSession.flush();
        if (removeAttribute) {
            session.removeAttribute("accountTypeChange");
        }
        hibernateSession.getTransaction().commit();
        response.sendRedirect(target);
    } catch (Exception e) {
        hibernateSession.getTransaction().rollback();
        throw e;
    } finally {
        SecurityUtil.clearThreadLocal();
        hibernateSession.close();
    }
%>