
$(function() {
    _.templateSettings = {
        interpolate: /\<\@\=(.+?)\@\>/gim,
        evaluate: /\<\@(.+?)\@\>/gim,
        escape: /\<\@\-(.+?)\@\>/gim
    };
    var curFolder = "1";
    var reportsTemplate = _.template($("#report_list_template").html());
    $("#report_list_target").append(reportsTemplate({reports: folderInfo["reports"][curFolder] }));
    $("a.tag-select").click(function(e) {
        e.preventDefault();
        var c = $(e.target);
        c.toggleClass("selected");
        var a = [];
        $("a.tag-select.selected").each(function() {a.push(parseInt($(this).attr("data-tag-id"))); })
        var allReports = [];
        _.each(folderInfo["reports"], function(e, i, l) {
            allReports = allReports.concat(e);
        });

        if(a.length == 0) {
            $("#report_list_target").html(reportsTemplate({reports: folderInfo["reports"][curFolder] }));
        } else {
            var filteredReports = _.select(allReports, function(e, i, l) {
                return a.length == 0 || _.any(e.tags, function(ee, ii, ll) {
                    return _.contains(a, ee.id);
                });
            })

            console.log(filteredReports);
            $("#report_list_target").html(reportsTemplate({reports: filteredReports }));
        }
    })
    $("a.folder").click(function(e) {
        e.preventDefault();
        curFolder = $(e.target).attr("data-folder-id");
        if(curFolder == "AdditionalViews")
            curFolder = "2";
        if($("a.tag-select.selected").length == 0)
            $("#report_list_target").html(reportsTemplate({reports: folderInfo["reports"][curFolder] }));

    })
})