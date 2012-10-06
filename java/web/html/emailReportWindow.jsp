<div class="modal hide fade" id="emailReportWindow">
    <div class="modal-header">
        <button class="close" data-dismiss="modal">?</button>
        <h3>Email Report</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal">
            <div class="control-group">
                <%--<label class="control-label" for="input01">Which format?</label>--%>
                <div class="controls">
                    <input type="radio" name="emailGroup" value="4">HTML
                    <input type="radio" name="emailGroup" value="1">Excel
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="input01">Who will this email go to?</label>
                <div class="controls">
                    <input type="text" class="input-xlarge" id="input01">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="input02">What should the subject of the email be?</label>
                <div class="controls">
                    <input type="text" class="input-xlarge" id="input02">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="textarea">Any message to include with the email?</label>
                <div class="controls">
                    <textarea class="input-xlarge" id="textarea" rows="5"></textarea>
                </div>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" onclick="email()">Send</button>
    </div>
</div>