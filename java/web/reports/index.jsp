<%@ page import="java.text.MessageFormat" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="com.easyinsight.analysis.*" %>
<%@ page import="com.easyinsight.analysis.InsightResponse" %>
<%--
  Created by IntelliJ IDEA.
  User: jamesboe
  Date: Oct 23, 2009
  Time: 10:08:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    String reportID = request.getParameter("reportID");
    com.easyinsight.analysis.InsightResponse insightResponse = new com.easyinsight.analysis.AnalysisService().openAnalysisIfPossible(reportID);
    long dataSourceID = insightResponse.getInsightDescriptor().getDataFeedID();
    String reportName = insightResponse.getInsightDescriptor().getName();
    int reportType = insightResponse.getInsightDescriptor().getReportType();
    InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
    String authorName = null;
    String description = null;
    String tags = null;
    String reportCreatedDate = null;
    String dataAttribution = null;
    String keywords = "";
    String reportRatingHTML = "";
    StringBuilder reportRatingHTMLBuilder = new StringBuilder();
    try {
        EmbeddedResults results = new DataService().getEmbeddedResults(Long.parseLong(reportID), dataSourceID, null, insightRequestMetadata,
                null);
        authorName = results.getDefinition().getAuthorName();
        description = results.getDefinition().getDescription();
        if (description == null) {
            description = "";
        }
        dataAttribution = results.getAttribution() == null ? "" : results.getAttribution();
        reportCreatedDate = new java.text.SimpleDateFormat().format(results.getDefinition().getDateCreated());
        java.util.List<com.easyinsight.analysis.Tag> tagCloud = results.getDefinition().getTagCloud();
        StringBuilder tagBuilder = new StringBuilder();
        for (com.easyinsight.analysis.Tag tag : tagCloud) {
            tagBuilder.append(tag.getTagName());
            tagBuilder.append(",");
        }
        if (tagBuilder.length() > 0) {
            tagBuilder.deleteCharAt(tagBuilder.length() - 1);
        }
        tags = tagBuilder.toString();
        if (tags.length() > 0) {
            keywords = "easy insight," + tags;
        } else {
            keywords = "easy insight";
        }
        double ratingsAverage = results.getRatingsAverage();
        String grayStar = "/images/star_grey.png";
        String greenStar = "/images/star_green.png";
        for (int i = 1; i <= 5; i++) {
            String star;
            if (i <= ratingsAverage) {
                star = greenStar;
            } else {
                star = grayStar;
            }
            reportRatingHTMLBuilder.append("<img src=\""+ star + "\" alt=\"Rating Star\"/>");
        }
        reportRatingHTML = reportRatingHTMLBuilder.toString();
    } catch (SecurityException e) {
        response.sendRedirect("login.jsp?error=true");
    }

%>
<head>
    <title><%= "Easy Insight - " + reportName %></title>
    <meta name="keywords" content="<%= keywords %>">
    <meta name="description" content="<%= description %>">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link type="text/css" rel="stylesheet" media="screen" href="/css/base.css" />
    <link rel="stylesheet" href="/css/report.css" type="text/css" media="screen" />
</head>
<body style="width:100%;text-align:center;margin:0px auto;">
    <div style="width:1000px;border-left-style:solid;border-left-color:#DDDDDD;border-left-width:1px;border-right-style:solid;border-right-color:#DDDDDD;border-right-width:1px;margin:0 auto;">
    	<div style="width:100%;text-align:left;height:70px;position:relative">
        	<a href="/index.html"><img src="/images/logo.jpg" alt="Easy Insight Logo" name="logo" id="logo" /></a>
    <div class="signupHeadline"><a href="https://www.easy-insight.com/app/" class="signupButton"></a> <a href="https://www.easy-insight.com/app/" class="signupforfreeButton"></a></div>
                <div class="headline"><a id="productPage" href="/product.html">PRODUCT</a> <a id="dataPage" href="/data.html">DATA</a> <a id="solutionsPage" href="/webanalytics.html">SOLUTIONS</a> <a id="blogPage" href="http://jamesboe.blogspot.com/">BLOG</a>  <a id="companyPage" href="/company.html">COMPANY</a></div>

        </div>
	    <!-- InstanceBeginEditable name="submenu" -->
    	<!-- InstanceEndEditable -->
        <div id="content">
        <!-- InstanceBeginEditable name="content" -->
        <div style="width:30%;position:relative;float:right;">
            <table cellpadding="5">
                <tr>
                    <td class="reportViewHeader">Report Name:</td>
                    <td class="reportView">
                        <%= reportName %>
                    </td>
                </tr>
                <tr>
                    <td class="reportViewHeader">Author Name:</td>
                    <td class="reportView">
                        <%= authorName %>
                    </td>
                </tr>
                <tr>
                    <td class="reportViewHeader">Creation Date:</td>
                    <td class="reportView">
                        <%= reportCreatedDate %>
                    </td>
                </tr>
                <tr>
                    <td class="reportViewHeader">Tags:</td>
                    <td class="reportView">
                        <%= tags %>
                    </td>
                </tr>
                <tr>
                    <td class="reportViewHeader">Attribution:</td>
                    <td class="reportView">
                        <%= dataAttribution %>
                    </td>
                </tr>
                <tr>
                    <td class="reportViewHeader">Report Rating:</td>
                    <td class="reportView">
                        <%= reportRatingHTML %>
                    </td>
                </tr>
                <tr>
                    <td class="reportViewHeader">Description:</td>
                    <td class="reportView">
                        <%= description %>
                    </td>
                </tr>
            </table>
        </div>
        <div style="width:70%;text-align:left;background-color:#FFFFFF">
            <div style="width:100%;height:60px;background:url(/images/banner.jpg);text-align:center">
                <h1 style="margin:0px;padding:18px;font-size:24px;color:#FFFFFF;font-size:16px;"><!-- InstanceBeginEditable name="innerTitle" --><%= reportName %><!-- InstanceEndEditable --></h1>
            </div>
            <%
                String embedHTML = java.text.MessageFormat.format("<object width=\"450\" height=\"346\" codebase=\"http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab\"\n" +
                        "        classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\">\n" +
                        "    <param name=\"movie\" value=\"https://www.easy-insight.com/app/easyui-debug/EmbeddedInsight.swf\"/>\n" +
                        "    <param name=\"quality\" value=\"high\"/>\n" +
                        "    <param name=\"bgcolor\" value=\"#869ca7\"/>\n" +
                        "    <param name=\"wmode\" value=\"opaque\"/>\n" +
                        "    <param name=\"allowScriptAccess\" value=\"always\"/>\n" +
                        "    <param name=\"flashvars\" value=\"analysisID={0}&reportType={1}&dataSourceID={2}&reportName={3}\"/>\n" +
                        "    <embed src=\"/app/easyui-debug/EmbeddedInsight.swf\" quality=\"high\" bgcolor=\"#869ca7\"\n" +
                        "           width=\"700\" height=\"346\" name=\"PrimaryWorkspace\" align=\"middle\" play=\"true\" loop=\"false\" quality=\"high\"\n" +
                        "           allowScriptAccess=\"always\" wmode=\"opaque\" flashvars=\"analysisID={0}&reportType={1}&dataSourceID={2}&reportName={3}\"\n" +
                        "           type=\"application/x-shockwave-flash\" pluginspage=\"http://www.adobe.com/go/getflashplayer\"></embed>\n" +
                        "</object>", reportID, reportType, dataSourceID, reportName);
                out.println(embedHTML);
            %>
        </div>
    <!-- InstanceEndEditable -->
</div>
</div>

</body>
</html>