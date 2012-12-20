<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.billing.BrainTreeBlueBillingSystem" %>
<%@ page import="com.easyinsight.users.NewModelAccountTypeChange" %>
<%@ page import="com.braintreegateway.Customer" %>
<%@ page import="com.braintreegateway.Subscription" %>
<%@ page import="com.braintreegateway.CreditCard" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    Session hibernateSession = Database.instance().createSession();
    try {
        hibernateSession.beginTransaction();
        NewModelAccountTypeChange accountTypeChange = (NewModelAccountTypeChange) session.getAttribute("accountTypeChange");

        Account account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);

        Customer customer = new BrainTreeBlueBillingSystem().getCustomer(account);
        Subscription currentSubscription = null;
        CreditCard curCC = null;
        for (CreditCard cc : customer.getCreditCards()) {
            if (cc.isDefault())
                curCC = cc;
            for (Subscription s : cc.getSubscriptions()) {
                if (s.getStatus() != Subscription.Status.CANCELED && s.getStatus() != Subscription.Status.EXPIRED)
                    currentSubscription = s;
            }
        }

        String target;
        if (curCC != null && currentSubscription != null) {
            if (currentSubscription.getPlanId().equals("1")) {
                new BrainTreeBlueBillingSystem().updateMonthly(currentSubscription, accountTypeChange.getAddonDesigners(),
                        accountTypeChange.getAddonStorage(), accountTypeChange.getAddonConnections());
            } else if (currentSubscription.getPlanId().equals("2")) {
                new BrainTreeBlueBillingSystem().updateYearly(currentSubscription, accountTypeChange.getAddonDesigners(),
                        accountTypeChange.getAddonStorage(), accountTypeChange.getAddonConnections());
            }
            accountTypeChange.apply(account, hibernateSession);
            hibernateSession.update(account);
            hibernateSession.flush();
            session.removeAttribute("accountTypeChange");
            target = "done.jsp";
        } else {
            target = RedirectUtil.getURL(request, "/app/billing/index.jsp?accountTypeChange=accountTypeChange&error=error");
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