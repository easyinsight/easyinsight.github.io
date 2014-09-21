<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.easyinsight.admin.NewsEntry" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.jsphelpers.EIHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Easy Insight &mdash; SaaS Business Intelligence, Reporting, and Analytics</title>
    <meta name="description"
          content="Easy Insight provides a self service, business user friendly solution for quick, easy, and affordable business intelligence over the cloud."/>
    <meta name="keywords"
          content="business intelligence, bi, saas bi, saas business intelligence, saas reporting, saas analytics, saas analysis, google analytics, salesforce, basecamp, highrise, ec2, mysql, sql server, saas, software as a service"/>

    <!-- font -->
    <link href='https://fonts.googleapis.com/css?family=Cabin:regular,bold' rel='stylesheet' type='text/css'>

    <!-- main includes -->

    <script type="text/javascript" src="/js/modernizr.js"></script>
    <script type="text/javascript" src="/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/js/jquery-ui.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/jquery-ui.css" rel="stylesheet">
    <link href="/css/bootstrap-website.css" rel="stylesheet"/>
    <script type="text/javascript" src="/js/bootstrap.js"></script>

    <!-- lightbox -->

    <script type="text/javascript" src="/js/jqvideobox.js"></script>
    <script type="text/javascript" src="/js/swfobject.js"></script>
    <script type="text/javascript" src="/js/jquery.lightbox-0.5.js"></script>
    <script type="text/javascript" src="/js/startup.js"></script>
    <link rel="stylesheet" href="/css/jquery.lightbox-0.5.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="/css/jqvideobox.css" type="text/css" media="screen"/>
</head>
<body>
<nav class="navbar navbar-static-top navbar-main website-navbar" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand eiBrand" href="/index.html"><img src="/images/logo.jpg" alt="Easy Insight Logo"
                                                                    name="logo"/></a>
        </div>

        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav navbar-right navbar-main-item">
                <li><a href="/app/" class="fontA">Customer Login</a></li>
                <li><a href="/app/newaccount/" class="fontA">Free Trial</a></li>
                <li><a href="/app/news/" class="fontA">What's New</a></li>
            </ul>
        </div>
    </div>
</nav>
<nav class="navbar navbar-subheader website-navbar" role="navigation">
    <div class="container-fluid">
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav navbar-subheader-item">
                <li><a href="/product.html">Features</a></li>
                <li><a href="/data.html">Connections</a></li>
                <li><a href="/pricing.html">Pricing</a></li>
                <li><a href="/app/websiteDocs/">Documentation</a></li>
                <li><a href="/contactus.html">Contact Us</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container-fluid screenshotDiv">
    <div class="row">
        <div class="col-sm-12">
            <div class="container">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="dragDropDone" style="padding-top:0">What's New with Easy Insight</div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="dragDropDone" style="padding-top:0;font-size:16px">Drag. Drop. Done. Business Intelligence Made Easy.</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container corePageWell">
    <div class="row">
        <%
            String article = request.getParameter("article");

            List<NewsEntry> newsEntryList = new AdminService().getNews();
            Set<String> tags = new HashSet<String>();
            for (NewsEntry newsEntry : newsEntryList) {
                String tagString = newsEntry.getTags();
                if (tagString == null) {
                    continue;
                }

                String[] tagArray = tagString.split(",");
                for (String tag : tagArray) {
                    String trimmedTag = tag.trim();
                    if (!"".equals(trimmedTag)) {
                        tags.add(trimmedTag);
                    }
                }
            }
            List<String> tagList = new ArrayList<String>(tags);
            EIHelper.sort(tagList);

        %>
        <div class="col-md-3">
            <div class="container">
                <div class="row">
                    <div class="col-md-2">
                        <img src="/images/logo2.png" alt="Easy Insight Logo"/>
                    </div>
                </div>
                <div class="row" style="margin-top: 20px">
                    <div class="col-md-3">
                        <div class="well" style="text-align: left">
                            <div><a href="/app/news/" style="font-size:16px">All</a></div>
                            <%
                                for (String tag : tagList) {
                            %>
                            <div><a href="/app/news/<%= tag %>" style="font-size:16px"><%= tag %></a></div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <%
                String tag = request.getParameter("tag");

                Collections.reverse(newsEntryList);
                for (NewsEntry newsEntry : newsEntryList) {
                    if (newsEntry.getTitle() == null) {
                        continue;
                    }
                    if (article != null) {
                        String aliased = newsEntry.getTitle().replace(" ", "-").replace(",", "").toLowerCase();
                        if (!article.equals(aliased)) {
                            continue;
                        }
                    } else if (tag != null) {
                        boolean valid = false;
                        String tagString = newsEntry.getTags();
                        if (tagString == null) {
                            continue;
                        }
                        String[] tagArray = tagString.split(",");
                        for (String testTag : tagArray) {
                            String trimmed = testTag.trim();
                            if (trimmed.equals(tag)) {
                                valid = true;
                            }
                        }
                        if (!valid) {
                            continue;
                        }
                    }
                    String permalink = "/app/whatsnewweb.jsp?article="+newsEntry.getTitle().replace(" ", "-").replace(",", "").toLowerCase();
            %>
            <div class="row" style="padding-top:10px">
                <div class="col-md-12" style="text-align:left">
                    <a class="productHeader" style="color: #0084B4" href="<%=permalink%>"><%= newsEntry.getTitle() %></a>
                </div>
            </div>
            <div class="row" style="padding-top:10px">
                <div class="col-md-12" style="text-align: left">
                    Posted on <a href="<%=permalink%>" style="color: #0084B4"><%=new SimpleDateFormat("MMMM dd, yyyy").format(newsEntry.getDate()) %></a> by <a style="color:#0084B4" href="<%=permalink%>"><%=newsEntry.getAuthor()%></a>
                </div>
            </div>
            <div class="row" style="padding-top:10px">
                <div class="col-md-12" style="padding-top:10px">
                    <div style="width:100%;text-align:left">
                        <p><%= newsEntry.getNews() %></p>
                    </div>
                </div>
            </div>
            <hr style="color: #DDDDDD; border-color: #DDDDDD; background-color: #DDDDDD; height: 1px; border: 0"/>
            <%
                }
            %>
        </div>
    </div>
</div>
<div class="container-fluid" id="newFooter">
    <div class="row">
        <div class="col-sm-2 col-sm-offset-2">
            <div class="row">
                <div class="col-sm-12">
                    <a href="/contactus.html">Contact Us</a>
                </div>
            </div>
        </div>
        <div class="col-sm-2">
            <div class="row">
                <div class="col-sm-12">
                    <a href="/pricing.html">Pricing</a>
                </div>
            </div>
        </div>
        <div class="col-sm-2">
            <div class="row">
                <div class="col-sm-12">
                    <a href="/partners.html">Partners</a>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <a href="/api/">Developers</a>
                </div>
            </div>
        </div>
        <div class="col-sm-2">
            <div class="row">
                <div class="col-sm-12">
                    <a href="/privacy.html">Privacy Policy</a>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <a href="/terms.html">Terms of Service</a>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12 copyright">
            Copyright ©2008-2014 Easy Insight, LLC. All rights reserved.
        </div>
    </div>
</div>
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>

<script type="text/javascript">
    var pageTracker = _gat._getTracker("UA-8316271-1");
    pageTracker._trackPageview();
</script>
</body>
<!-- InstanceEnd --></html>