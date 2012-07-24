Chart = {
    getCallback:function (target, params, showLabels, extras) {
        return function (data) {
            Utils.noData(data["values"].flatten(), function () {
                if (showLabels) {
                    var labels = data["labels"];
                    params.legend = {show:true, labels:labels};
                }
                var s1 = data["values"];
                var plot1 = $.jqplot(target + 'ReportArea', s1, params);
                Chart.charts[target] = plot1
                if (extras) {
                    extras(data);
                }
            }, Chart.cleanup, target);
        };
    },
    charts:{},

    cleanup:function (target) {
        if (Chart.charts[target]) {
            Chart.charts[target].destroy();
            delete Chart.charts[target];
            Chart.charts[target] = null;
        }
    },


    getColumnChartCallback:function (target, params) {
        return Chart.getCallback(target, params, false, function (data) {
            var tt = $("#" + target);
            tt.bind("jqplotDataHighlight", Chart.columnToolTipHover(data["ticks"]));
            tt.bind("jqplotDataUnhighlight", Chart.columnToolTipOut);
        })
    },

    columnToolTipHover:function (ticks) {
        return function (ev, seriesIndex, pointIndex, data) {
            var mouseX = ev.pageX; //these are going to be how jquery knows where to put the div that will be our tooltip
            var mouseY = ev.pageY;
            $('#chartpseudotooltip').html(ticks[pointIndex] + ', ' + data[1]);

            var cssObj = {
                'position':'absolute',
                'font-weight':'bold',
                'left':mouseX + 'px',
                'top':mouseY + 'px'
            };

            $('#chartpseudotooltip').css(cssObj);
            $('#chartpseudotooltip').show();
        };
    },
    columnToolTipOut:function (ev) {
        $('#chartpseudotooltip').html('');
        $('#chartpseudotooltip').hide();
    },

    getPieChartCallback:function (target, params) {
        return Chart.getCallback(target, params, false, function (data) {
            var tt = $("#" + target);
            tt.bind("jqplotDataHighlight", Chart.pieToolTipHover);
            tt.bind("jqplotDataUnhighlight", Chart.pieToolTipOut);
        });
    },

    pieToolTipHover:function (ev, seriesIndex, pointIndex, data) {
        var $this = $(this);
        $this.attr('title', data[0] + ": " + data[1]);
    },
    pieToolTipOut: function(ev) {
        var $this = $(this);
        $this.attr('title', "");
    }

}