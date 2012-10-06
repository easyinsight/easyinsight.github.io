<div class="modal hide fade" id="exportModalWindow">
    <div class="modal-header">
        <button data-dismiss="modal">?</button>
        <h3>Export Options</h3>
    </div>
    <div class="modal-body">
        <a href="../exportExcel?reportID=<%= request.getParameter("reportID") %>" class="btn">Export to Excel</a>
        <button class="btn" onclick="$('#exportModalWindow').modal('hide'); $('#emailReportWindow').modal(true, true, true)">Email Report</button>
    </div>
</div>
<div class="modal hide" id="refreshingReport">
    <div class="modal-body">
        Refreshing the report...
        <div class="progress progress-striped active">
            <div class="bar"
                 style="width: 100%;"></div>
        </div>
    </div>
</div>