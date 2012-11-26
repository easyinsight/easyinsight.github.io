function drillThrough(params) {
    $.getJSON('/app/drillThrough?' + params, function(data) {
        var url = data["url"];
        window.location.href = url;
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