<div class="modal fade" id="embedDashboardWindow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Embedding Dashboard</h3>
            </div>
            <div class="modal-body">
                <div class="alert alert-info" id="embedDashboardVisibility"></div>
                <hr/>
                <form class="form-horizontal">
                    <div class="control-group">

                        <div class="controls">

                            <label class="control-label" for="embedDashboardWidth">Width:</label><input type="number" id="embedDashboardWidth" name="embedDashboardWidth"
                                                                                          min="0" max="5000" value="500"/>
                            <label class="control-label" for="embedDashboardHeight">Height:</label><input type="number" id="embedDashboardHeight" name="embedDashboardGroup"
                                                                                           min="0" max="5000" value="500"/>
                        </div>

                    </div>
                </form>
                <hr/>
                <div><p>You can paste this HTML into any web page to embed the dashboard into the page.</p></div>
                <div><p id="embedDashboardURL"></p></div>
            </div>
        </div>
    </div>
</div>