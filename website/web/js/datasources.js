
$(function() {
    _.templateSettings = {
        interpolate: /\<\@\=(.+?)\@\>/gim,
        evaluate: /\<\@(.+?)\@\>/gim,
        escape: /\<\@\-(.+?)\@\>/gim
    };

    var dsTemplate = _.template($("#data_source_template").html());
    $("#data_sources").append(dsTemplate({data_sources: dataSources }));
    $("a.tag-select").click(function(e) {
        e.preventDefault();
        var c = $(e.target);
        c.toggleClass("selected");
        var a = [];
        $("a.tag-select.selected").each(function() {a.push(parseInt($(this).attr("data-tag-id"))); })
        var filteredSources = _.select(dataSources, function (e, i, l) {
            return a.length == 0 || _.any(e.tags, function(ee, ii, ll) {
                return _.contains(a, ee.id);
            });
        })
        $("#data_sources").html(dsTemplate({data_sources: filteredSources }))
    })
})