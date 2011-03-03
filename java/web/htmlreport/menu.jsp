<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Easy Insight Mobile</title>
	<link rel="stylesheet" href="/css/jquery.mobile-1.0a1.min.css" />
	<script src="/js/jquery-1.4.3.min.js"></script>
	<script src="/js/jquery.mobile-1.0a1.min.js"></script>
    <script type="text/javascript">
        $(function() {
            $("div[data-role*='page']").live('pageshow', function(event, ui) {

                if (this.children[0].id == "page-report-display") {

                    if ($("#reportImage").length > 0) {

                        var image = document.createElement("img");
                        image.src = "/app/htmlimage?width="+(document.documentElement.clientWidth - 30) +"&height="+(document.documentElement.clientHeight - 30);
                        $(image).load(function(event) {
                            $("#waitingLogo").remove();
                            $("#reportImage").append(event.target);
                        });

                    }
                }
            });
        });
    </script>
</head>
<body>

<div data-role="page">

	<div data-role="header">
		<h1>Easy Insight Mobile</h1>
	</div><!-- /header -->

	<div data-role="content">
        <ul data-role="listview">
            <li><a href="dataSources.jsp">Reports</a></li>
            <li><a href="scorecards.jsp">Scorecards</a></li>    
        </ul>

	</div><!-- /content -->

	<div data-role="footer">
		<h4>Easy Insight Mobile</h4>
	</div><!-- /footer -->
</div><!-- /page -->

</body>
</html>