Gauge = {
    gauges: {},
    getCallback: function(target, gaugeKey, bandColors) {
        var aGauge = new AquaGauge('gauge' + gaugeKey);
        Gauge.gauges[gaugeKey] = aGauge;
        aGauge.props.minValue = 0;
        aGauge.props.dialSubTitle = '';
        aGauge.props.dialTitle = '';
        aGauge.props.maxValue = 1000;
        aGauge.props.rangeSegments = bandColors;
        return function(data) {
            Utils.noData(data["value"], function() {
                var aGauge = Gauge.gauges[gaugeKey];
                aGauge.refresh(data["value"]);
                $('#benchmark' + gaugeKey).html(data['benchmark']);
            }, null, target);
        }
    }
}