function drillThrough(params) {
    $.ajax( {
        dataType: "json",
        url: '/app/drillThrough?' + params,
        success: function(data) {
            var url = data["url"];
            window.location.href = url;
        },
        error: function(a, b, c) {
            window.location.href = "/app/serverError.jsp"
        }
});
}

function drillThroughParameterized(params, val) {
    var first = true;
    var s = "";
    var p = function(p, param) {
        if(params[param] != null) {
            s = s + (first ? "" : "&") + p + "=" + params[param];
            first = false;
        }
    }
    p("embedded", "embedded");
    p("drillthroughID", "id");
    p("reportID", "reportID");
    p("sourceField", "source");
    if(val != null) {
        if(!first) {
            s = s + "&";
            first = false;
        }
        s = s + "f" + params["xaxis"] + "=" + encodeURI(val);
    }
    drillThrough(s);
}