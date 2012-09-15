<!DOCTYPE html>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.users.UserAccountAdminService" %>
<%@ page import="com.easyinsight.users.AccountActivityStorage" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Account Management</title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/js/jquery-ui-1.8.20.custom.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet">



    <style type="text/css">
        body {
            padding-top: 45px;
            padding-bottom: 40px;
        }
    </style>
    <link href='https://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'/>
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
    <%
        String userName = (String) session.getAttribute("userName");
        EIConnection conn = Database.instance().getConnection();
        Session hibernateSession = Database.instance().createSession(conn);
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);

        try {
            if (!SecurityUtil.isAccountAdmin()) {
                response.sendRedirect("access.jsp");
                return;
            }
            if((SecurityUtil.getAccountTier() == Account.PREMIUM || SecurityUtil.getAccountTier() == Account.ENTERPRISE)) {
                response.sendRedirect("access.jsp");
                return;
            }

            Account account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);

            if (account.getPricingModel() == 0) {
                response.sendRedirect("legacyAccountType.jsp");
                return;
            }

            boolean trial = account.getAccountState() == Account.TRIAL;

            boolean annual = account.getBillingDayOfMonth() != null;

            String discountString;
            if (annual) {
                discountString = NumberFormat.getCurrencyInstance().format(account.createCost() / 12);
            } else {
                discountString = "$0.00";
            }

            long usedStorage = new UserAccountAdminService().getAccountStorage();
            String usedStorageString = Account.humanReadableByteCount(usedStorage,  true) + " / " + Account.humanReadableByteCount(account.getMaxSize(), true);
    %>
    <script type="text/javascript">

        var designers = <%= account.getMaxUsers() %>;
        var accountType = <%= account.getAccountType() %>;
        var storageCost = 0;
        var billingInterval = <%= account.getBillingMonthOfYear() != null ? 2 : 1%>;

        function updateAccountValue(designerInput) {
            designers = parseInt(designerInput.value);
            updatePrice();
        }

        function updateBillingInterval(selector) {
            billingInterval = parseInt(selector.value);
            updatePrice();
        }

        function updatePrice() {
            var cost;
            var discount;
            if (accountType == 2) {
                cost = (25 * (billingInterval == 2 ? 12 : 1)) + (designers - 1) * 10 * (billingInterval == 2 ? 12 : 1);
                discount = (billingInterval == 2 ? cost / 12 : 0);
            } else if (accountType == 3) {
                cost = (75 * (billingInterval == 2 ? 12 : 1)) + (designers - 1) * 25 * (billingInterval == 2 ? 12 : 1);
                discount = (billingInterval == 2 ? cost / 12 : 0);
            } else if (accountType == 4) {
                cost = (200 * (billingInterval == 2 ? 12 : 1)) + (designers - 1) * 50 * (billingInterval == 2 ? 12 : 1) + storageCost * (billingInterval == 2 ? 12 : 1);
                discount = (billingInterval == 2 ? cost / 12 : 0);
            }
            var costString = "$" + cost + ".00";
            var endCost = "$" + (cost - discount) + ".00";
            var discountString = "$" + discount + ".00";
            if (accountType == 2) {
                $('#basicPrice').html(costString);
                $('#basicDiscount').html(discountString);
                $('#basicTotal').html(endCost);
            } else if (accountType == 3) {
                $('#plusPrice').html(costString);
                $('#plusDiscount').html(discountString);
                $('#plusTotal').html(endCost);
            } else if (accountType == 4) {
                $('#proPrice').html(costString);
                $('#proDiscount').html(discountString);
                $('#proTotal').html(endCost);
            }
        }

        function updateStorageCost(source) {
            var value = parseInt(source.value);
            if (value == 1) {
                storageCost = 0;
            } else if (value == 2) {
                storageCost = 150;
            } else if (value == 3) {
                storageCost = 300;
            } else if (value == 4) {
                storageCost = 450;
            }
            updatePrice();
        }

        $(function() {
            $('#accountTab').bind('show', function(e) {
                var pattern=/#.+/gi;
                var contentID = e.target.toString().match(pattern)[0];
                if (contentID == "#tab1") {
                    if (designers > 3) {
                        designers = 3;
                    }
                    accountType = 2;
                    $('#numberDesignersBasic').val(designers);
                    $('#basicBillingInterval').val(billingInterval);
                } else if (contentID == "#tab2") {
                    accountType = 3;
                    $('#numberDesignersPlus').val(designers);
                    $('#plusBillingInterval').val(billingInterval);
                } else if (contentID == "#tab3") {
                    accountType = 4;
                    $('#numberDesignersPro').val(designers);
                    $('#proBillingInterval').val(billingInterval);
                }

                updatePrice();
            })
        });
    </script>
</head>
<body>

<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <%--<a class="brand" href="#"><img src="/images/logo3.jpg"/></a>--%>
            <div class="btn-group pull-right">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(userName) %>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <%
                        if (account.getAccountState() == Account.TRIAL || account.getAccountState() == Account.ACTIVE) {
                    %>
                    <li><a href="../html/flashAppAction.jsp">Back to Full Interface</a></li>
                    <%
                        }
                    %>
                    <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                </ul>
            </div>
            <div class="nav-collapse">
                <ul class="nav">
                    <li class="active"><a href="#">Account Configuration</a></li>
                    <li><a href="/app/billing">Billing Setup</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="span12">
            <div style="width:100%;text-align: center">
                <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
            </div>
        </div>
        <div class="row">
            <div class="span8 offset1" style="background-color: #E6E6E6;padding:10px;border-radius: 5px">
                <div class="row">
                    <div class="span6">
                        <h3>Account Info</h3>
                    </div>
                </div>
                <% if (request.getParameter("success") != null) { %>
                <div class="row">
                    <div class="span8">
                        <p style="font-size: 12px;padding: 0;margin-bottom: 5px;color: #009900">Your account type has been changed.</p>
                    </div>
                </div>
                <% } %>
                <div class="row">
                    <div class="offset2 span4">
                        <div class="well" style="background-color: #FBFBFB">
                            <%
                                if (trial) {
                            %>
                            <div style="float:left;height:90px;padding-top:30px;padding-right:60px"><h4>Your Free Trial Account</h4></div>
                            <div style="height:90px">
                                <p><%= account.planName() %></p>
                                <p><%= account.designers() %></p>
                                <p><%= account.billingInterval() %></p>
                                <p><%= usedStorageString %></p>
                            </div>
                            <%
                                } else {
                            %>
                            <div style="float:left;height:90px;padding-top:30px;padding-right:60px"><h4>Your Current Account</h4></div>
                            <div style="height:90px">
                                <p><%= account.planName() %></p>
                                <p><%= account.designers() %></p>
                                <p><%= account.billingInterval() %></p>
                                <p><%= usedStorageString %></p>
                            </div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
                <div class="tabbable" style="background-color: #FFFFFF;padding: 10px">
                    <ul class="nav nav-pills" id="accountTab">
                        <li <%= account.getAccountType() == Account.BASIC ? "class=\"active\"" : ""%>><a href="#tab1" data-toggle="tab" style="margin-right:250px;font-size: 16px">Basic</a></li>
                        <li <%= account.getAccountType() == Account.PLUS ? "class=\"active\"" : ""%>><a href="#tab2" data-toggle="tab" style="margin-right:250px;font-size:16px">Plus</a></li>
                        <li <%= account.getAccountType() == Account.PROFESSIONAL ? "class=\"active\"" : ""%>><a href="#tab3" data-toggle="tab" style="font-size:16px">Professional</a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane <%= account.getAccountType() == Account.BASIC ? "active" : ""%>" id="tab1" style="background-color: #F0F0F0;padding: 8px">
                            <div class="row" style="margin-bottom: 8px">
                                <div class="span3">
                                    <h3>You have selected Basic</h3>
                                </div>
                                <div class="offset2 span2">
                                    <h4>$10/mo/designer</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="span4">
                                    <h4>WHAT YOU GET</h4>
                                    <ul>
                                        <li>Maximum of three designers</li>
                                        <li>Connections to a wide variety of SaaS applications</li>
                                        <li>35 MB of data storage</li>
                                    </ul>
                                    <h4>WHAT YOU ARE MISSING</h4>
                                    <ul>
                                        <li>Additional report viewers</li>
                                        <li>Schedule email report delivery to anyone</li>
                                        <li>Connections to Salesforce and QuickBase</li>
                                    </ul>
                                </div>
                                <div class="span3" style="background-color:#FFFFFF;padding: 10px;width: 310px">
                                    <form method="post" action="/app/billing/accountTypeAction.jsp?targetType=<%=Account.BASIC%>">
                                        <label>Number of Designers</label>
                                        <input type="text" style="width:290px" name="numberDesigners" value="<%= account.getMaxUsers() %>" id="numberDesignersBasic" onchange="updateAccountValue(this)">
                                        <label>Bill Me</label>
                                        <select style="width:280px" name="billingInterval" onchange="updateBillingInterval(this)" id="basicBillingInterval">
                                            <option value="1">Monthly</option>
                                            <option <%= annual ? "selected=\"selected\"" : "" %> value="2">Yearly</option>
                                        </select>
                                        <div style="float:right">
                                            <span style="font-size: 14px" id="basicPrice"><%= account.createCostString() %></span>
                                        </div>
                                        <div>
                                            <p style="font-size: 14px">Price</p>
                                        </div>
                                        <div style="float:right">
                                            <span style="font-size: 14px" id="basicDiscount"><%= discountString %></span>
                                        </div>
                                        <div>
                                            <p style="font-size: 14px">Discount</p>
                                        </div>
                                        <hr>
                                        <div style="float:right">
                                            <span style="font-size: 16px" id="basicTotal"><strong><%= account.createTotalCostString() %></strong></span>
                                        </div>
                                        <div>
                                            <p style="font-size: 16px">Total</p>
                                        </div>
                                        <% if (request.getSession().getAttribute("errorString") != null) { %>
                                        <fieldset class="control-group error">
                                            <label class="formAreaP control-label"><%= request.getSession().getAttribute("errorString")%></label>
                                        </fieldset>
                                        <% } %>
                                        <button class="btn btn-success" type="submit">Update Account</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane <%= account.getAccountType() == Account.PLUS ? "active" : ""%>" id="tab2" style="background-color: #F0F0F0;padding: 8px">
                            <div class="row" style="margin-bottom: 8px">
                                <div class="span3">
                                    <h3>You have selected Plus</h3>
                                </div>
                                <div class="offset2 span2">
                                    <h4>$25/mo/designer</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="span4">
                                    <h4>WHAT YOU GET</h4>
                                    <ul>
                                        <li>Maximum of ten designers</li>
                                        <li>Schedule email report delivery to anyone</li>
                                        <li>Connections to a wide variety of SaaS applications</li>
                                        <li>90 MB of data storage</li>
                                    </ul>
                                    <h4>WHAT YOU ARE MISSING</h4>
                                    <ul>
                                        <li>Data level customization access</li>
                                        <li>Connections to Salesforce and QuickBase</li>
                                    </ul>
                                </div>
                                <div class="span3" style="background-color:#FFFFFF;padding: 10px;width: 310px">
                                    <form method="post" action="/app/billing/accountTypeAction.jsp?targetType=<%=Account.PLUS%>">
                                        <label>Number of Designers</label>
                                        <input type="text" style="width:290px" name="numberDesigners" value="<%= account.getMaxUsers() %>" id="numberDesignersPlus" onchange="updateAccountValue(this)">
                                        <label>Bill Me</label>
                                        <select style="width:280px" name="billingInterval" onchange="updateBillingInterval(this)" id="plusBillingInterval">
                                            <option value="1">Monthly</option>
                                            <option <%= annual ? "selected=\"selected\"" : "" %> value="2">Yearly</option>
                                        </select>
                                        <div style="float:right">
                                            <span style="font-size: 14px" id="plusPrice"><%= discountString %></span>
                                        </div>
                                        <div>
                                            <p style="font-size: 14px">Price</p>
                                        </div>
                                        <div style="float:right">
                                            <span style="font-size: 14px" id="plusDiscount"><%= account.getBillingMonthOfYear() != null ? "$75.00" : "$0.00" %></span>
                                        </div>
                                        <div>
                                            <p style="font-size: 14px">Discount</p>
                                        </div>
                                        <hr>
                                        <div style="float:right">
                                            <span style="font-size: 16px" id="plusTotal"><strong><%= account.createTotalCostString() %></strong></span>
                                        </div>
                                        <div>
                                            <p style="font-size: 16px">Total</p>
                                        </div>
                                        <% if (request.getSession().getAttribute("errorString") != null) { %>
                                        <fieldset class="control-group error">
                                            <label class="formAreaP control-label"><%= request.getSession().getAttribute("errorString")%></label>
                                        </fieldset>
                                        <% } %>
                                        <button class="btn btn-success" type="submit">Update Account</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane <%= account.getAccountType() == Account.PROFESSIONAL ? "active" : ""%>" id="tab3" style="background-color: #F0F0F0;padding: 8px">
                            <div class="row" style="margin-bottom: 8px">
                                <div class="span3">
                                    <h3>You have selected Professional</h3>
                                </div>
                                <div class="offset2 span2">
                                    <h4>$50/mo/designer</h4>
                                </div>
                            </div>
                            <div class="row">
                                <div class="span4">
                                    <h4>WHAT YOU GET</h4>
                                    <ul>
                                        <li>Scalable data storage, starting at 250 MB</li>
                                        <li>Unlimited report viewers</li>
                                        <li>Connections to all systems supported by Easy Insight</li>
                                        <li>Data level access control</li>
                                    </ul>
                                </div>
                                <div class="span3" style="background-color:#FFFFFF;padding: 10px;width: 310px">
                                    <form method="post" action="/app/billing/accountTypeAction.jsp?targetType=<%= Account.PROFESSIONAL %>">
                                        <label>Number of Designers</label>
                                        <input type="text" style="width:290px" name="numberDesigners" value="<%= account.getMaxUsers() %>" id="numberDesignersPro" onchange="updateAccountValue(this)">
                                        <label>Storage</label>
                                        <select style="width:280px" name="storageSelection" onchange="updateStorageCost(storageSelection)" id="storageSelection">
                                            <option <%= account.getMaxSize() == Account.PROFESSIONAL_MAX ? "selected=\"selected\"" : "" %> value="1">250 MB (included)</option>
                                            <option <%= account.getMaxSize() == Account.PROFESSIONAL_MAX_2 ? "selected=\"selected\"" : "" %> value="2">500 MB ($150/mo)</option>
                                            <option <%= account.getMaxSize() == Account.PROFESSIONAL_MAX_3 ? "selected=\"selected\"" : "" %> value="3">750 MB ($300/mo)</option>
                                            <option <%= account.getMaxSize() == Account.PROFESSIONAL_MAX_4 ? "selected=\"selected\"" : "" %> value="4">1 GB ($450/mo)</option>
                                        </select>
                                        <label>Bill Me</label>
                                        <select style="width:280px" name="billingInterval" onchange="updateBillingInterval(this)" id="proBillingInterval">
                                            <option value="1">Monthly</option>
                                            <option <%= annual ? "selected=\"selected\"" : "" %> value="2">Yearly</option>
                                        </select>
                                        <div style="float:right">
                                            <span style="font-size: 14px" id="proPrice"><%= discountString %></span>
                                        </div>
                                        <div>
                                            <p style="font-size: 14px">Price</p>
                                        </div>
                                        <div style="float:right">
                                            <span style="font-size: 14px" id="proDiscount"><%= account.getBillingMonthOfYear() != null ? "$200.00" : "$0.00" %></span>
                                        </div>
                                        <div>
                                            <p style="font-size: 14px">Discount</p>
                                        </div>
                                        <hr>
                                        <div style="float:right">
                                            <span style="font-size: 16px" id="proTotal"><strong><%= account.createTotalCostString() %></strong></span>
                                        </div>
                                        <div>
                                            <p style="font-size: 16px">Total</p>
                                        </div>
                                        <% if (request.getSession().getAttribute("errorString") != null) { %>
                                            <fieldset class="control-group error">
                                                <label class="formAreaP control-label"><%= request.getSession().getAttribute("errorString")%></label>
                                            </fieldset>
                                        <% } %>
                                        <button class="btn btn-success" type="submit">Update Account</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="span3">
                <div class="well" style="background-color: #d5d5d5">
                    <p><strong>Have questions?</strong></p>
                    <p>You can contact Easy Insight at 1-720-285-8652 or sales@easy-insight.com if you have any questions or concerns around your account billing.</p>
                </div>
            </div>
        </div>
    </div>
</div>

        <%
            } finally {
                hibernateSession.close();
                Database.closeConnection(conn);
                SecurityUtil.clearThreadLocal();
            }
        %>
</body>
</html>
