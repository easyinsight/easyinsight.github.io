function drillThrough(params) {
    $.getJSON('/app/drillThrough?' + params, function(data) {
        var url = data["url"];
        window.location.href = url;
    });
}