<%
    HttpSession theSession = request.getSession( false );

    // print out the session id
    if( theSession != null ) {
      synchronized( theSession ) {
        // invalidating a session destroys it
        theSession.invalidate();
      }
    }
%>
<script type="text/javascript">
    $("#scorecard").html("");
    $("#notice").html("You have successfully been logged out.");
    $("#loginDialog").dialog('open');
    if(typeof(prefs) == typeof(Function)) {
        prefs.set("scorecardID", null);
    }
    $("#loginDialog input").each(function (i, elem) { elem.value = ""});
</script>