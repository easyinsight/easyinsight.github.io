<%@ page import="com.easyinsight.analysis.HTMLReportMetadata" %>
<%@ page import="com.easyinsight.analysis.WSAnalysisDefinition" %>
<%@ page import="com.easyinsight.analysis.AnalysisStorage" %>
<%
    String drillthroughArgh = request.getParameter("drillthroughKey");
    if ("null".equals(drillthroughArgh)) {
        drillthroughArgh = null;
    }
    WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(Long.parseLong(request.getParameter("reportID")));
%>
<script type="text/javascript">

    $(document).ready(function() {
        reportReady = true;
        refreshReport();
    });

    var filtersShown = true;

    function toggleFilters() {
        if (filtersShown) {
            $('#filterRow').hide();
        } else {
            $('#filterRow').show();
        }
        filtersShown = !filtersShown;
    }

    function refreshReport() {
        $('#refreshingReport').modal(true, true, true);
        var strParams = "";
    <%
if (drillthroughArgh != null) {
    %>
        strParams += "drillThroughKey=<%= drillthroughArgh %>&";
    <%
        }
    %>
        for (var key in filterBase) {
            var keyedFilter = filterBase[key];
            for (var filterValue in keyedFilter) {
                var value = keyedFilter[filterValue];
                strParams += filterValue + "=" + value + "&";
            }
        }
        <%
        HTMLReportMetadata metadata = new HTMLReportMetadata(100);
        metadata.setEmbedded(Boolean.parseBoolean(request.getParameter("embedded")));
        %>
    <%= report.toHTML("reportTarget", metadata) %>
    }
</script>