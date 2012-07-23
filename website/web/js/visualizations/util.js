Utils = {
    noData:function (data, f, cleanup, target) {
        cleanup(target);
        if ((data instanceof Array && data.length > 0) || (typeof data == "object" && Object.keys(data).length > 0)) {
            $("#" + target + " .reportArea").show();
            $("#" + target + " .noData").hide();
            f();
        } else {
            $("#" + target + " .reportArea").hide();
            $("#" + target + " .noData").show();
        }
        afterRefresh();
    }
};

Array.prototype.flatten = function flatten() {
    var flat = [];
    for (var i = 0, l = this.length; i < l; i++) {
        var type = Object.prototype.toString.call(this[i]).split(' ').pop().split(']').shift().toLowerCase();
        if (type) {
            flat = flat.concat(/^(array|collection|arguments|object)$/.test(type) ? flatten.call(this[i]) : this[i]);
        }
    }
    return flat;
};