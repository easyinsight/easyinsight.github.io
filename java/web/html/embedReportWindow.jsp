<div class="modal fade" id="embedReportWindow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Embedding Report</h3>
            </div>
            <div class="modal-body">
                <div class="alert alert-info" id="embedReportVisibility"></div>
                <hr/>
                <form class="form-horizontal">
                    <div class="control-group">

                        <div class="controls">

                            <label class="control-label" for="embedReportWidth">Width:</label><input type="number" id="embedReportWidth" name="embedReportWidth"
                                                                                          min="0" max="5000" value="500"/>
                            <label class="control-label" for="embedReportHeight">Height:</label><input type="number" id="embedReportHeight" name="embedReportHeight"
                                                                                           min="0" max="5000" value="500"/>
                        </div>

                    </div>
                </form>
                <hr/>
                <div><p>You can paste this HTML into any web page to embed the report into the page.</p></div>
                <div><p id="embedReportURL"></p></div>
            </div>
        </div>
    </div>
</div>