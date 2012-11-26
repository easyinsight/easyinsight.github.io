<script type="text/javascript">
    Date.firstDayOfWeek = 0;
    Date.format = 'yyyy/mm/dd';

    function refreshDataSource() {
        $("#refreshDiv").show();
        $.getJSON('/app/refreshDataSource?dataSourceID=<%= request.getParameter("dataSourceID") %>', function(data) {
            var callDataID = data["callDataID"];
            again(callDataID);
        });
    }

    function onDataSourceResult(data, callDataID) {
        var status = data["status"];
        if (status == 1) {
            // running
            again(callDataID);
        } else if (status == 2) {
            $("#refreshDiv").hide();
            refreshReport();
        } else {
            $("#refreshDiv").hide();
            $("#problemHTML").show();
            $("#problemHTML").html(data["problemHTML"]);
        }
    }

    function afterRefresh() {
        $('#refreshingReport').modal('hide');
    }

    function email() {
        var format = $('input:radio[name=emailGroup]:checked').val();
        var recipient = $('#input01').val();
        var subject = $('#input02').val();
        var body = $('#textarea').val();
        $.getJSON('/app/emailReport?reportID=<%= request.getParameter("reportID")%>&format=' + format + "&recipient="+recipient + "&subject=" + subject + "&body=" + body, function(data) {
            alert('Email sent.');
        });
    }

    function again(callDataID) {
        setTimeout(function() {
            $.getJSON('/app/refreshStatus?callDataID=' + callDataID, function(data) {
                onDataSourceResult(data, callDataID);
            });
        }, 5000);
    }
</script>