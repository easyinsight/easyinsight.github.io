/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.1
 * Revision: 1096
 *
 * Copyright (c) 2009-2011 Chris Leonello
 * jqPlot is currently available for use in all personal or commercial projects
 * under both the MIT (http://www.opensource.org/licenses/mit-license.php) and GPL
 * version 2.0 (http://www.gnu.org/licenses/gpl-2.0.html) licenses. This means that you can
 * choose the license that best suits your project and use it accordingly.
 *
 * Although not required, the author would appreciate an email letting him
 * know of any substantial use of jqPlot.  You can reach the author at:
 * chris at jqplot dot com or see http://www.jqplot.com/info.php .
 *
 * If you are feeling kind and generous, consider supporting the project by
 * making a donation at: http://www.jqplot.com/donate.php .
 *
 * sprintf functions contained in jqplot.sprintf.js by Ash Searle:
 *
 *     version 2007.04.27
 *     author Ash Searle
 *     http://hexmen.com/blog/2007/03/printf-sprintf/
 *     http://hexmen.com/js/sprintf.js
 *     The author (Ash Searle) has placed this code in the public domain:
 *     "This code is unrestricted: you are free to use it however you like."
 *
 */
(function ($) {

    $.jqplot.GradientTableLegendRenderer = function () {
        $.jqplot.TableLegendRenderer.call(this);
    }

    $.jqplot.GradientTableLegendRenderer.prototype = new $.jqplot.TableLegendRenderer();

    $.jqplot.GradientTableLegendRenderer.prototype.constructor = $.jqplot.GradientTableLegendRenderer;
    $.jqplot.GradientTableLegendRenderer.prototype.draw = function () {
        if (this._elem) {
            this._elem.emptyForce();
            this._elem = null;
        }

        if (this.show) {
            var series = this._series;
            // make a table.  one line label per row.
            var elem = document.createElement('table');
            this._elem = $(elem);
            this._elem.addClass('jqplot-table-legend');

            var ss = {position:'absolute'};
            if (this.background) {
                ss['background'] = this.background;
            }
            if (this.border) {
                ss['border'] = this.border;
            }
            if (this.fontSize) {
                ss['fontSize'] = this.fontSize;
            }
            if (this.fontFamily) {
                ss['fontFamily'] = this.fontFamily;
            }
            if (this.textColor) {
                ss['textColor'] = this.textColor;
            }
            if (this.marginTop != null) {
                ss['marginTop'] = this.marginTop;
            }
            if (this.marginBottom != null) {
                ss['marginBottom'] = this.marginBottom;
            }
            if (this.marginLeft != null) {
                ss['marginLeft'] = this.marginLeft;
            }
            if (this.marginRight != null) {
                ss['marginRight'] = this.marginRight;
            }


            var pad = false,
                reverse = false,
                s;
            for (var i = 0; i < series.length; i++) {
                s = series[i];
                if (s._stack || s.renderer.constructor == $.jqplot.BezierCurveRenderer) {
                    reverse = true;
                }
                if (s.show && s.showLabel) {
                    var lt = this.labels[i] || s.label.toString();
                    if (lt) {
                        var color = s.color[0]["color"];
                        if (reverse && i < series.length - 1) {
                            pad = true;
                        }
                        else if (reverse && i == series.length - 1) {
                            pad = false;
                        }
                        this.renderer.addrow.call(this, lt, color, pad, reverse);
                        pad = true;
                    }
                    // let plugins add more rows to legend.  Used by trend line plugin.
                    for (var j = 0; j < $.jqplot.addLegendRowHooks.length; j++) {
                        var item = $.jqplot.addLegendRowHooks[j].call(this, s);
                        if (item) {
                            this.renderer.addrow.call(this, item.label, item.color, pad);
                            pad = true;
                        }
                    }
                    lt = null;
                }
            }
        }
        return this._elem;
    };

    // Class: $.jqplot.GradientBarRenderer
    // A plugin renderer for jqPlot to draw a bar plot.
    // Draws series as a line.

    $.jqplot.GradientBarRenderer = function () {
        $.jqplot.BarRenderer.call(this);
    };

    $.jqplot.GradientBarRenderer.prototype = new $.jqplot.BarRenderer;
    $.jqplot.GradientBarRenderer.prototype.constructor = $.jqplot.GradientBarRenderer;

    function computeHighlightColors (colors) {
        var ret = [];
        for (var i=0; i<colors.length; i++){
            var rgba = $.jqplot.getColorComponents(colors[i]);
            var newrgb = [rgba[0], rgba[1], rgba[2]];
            var sum = newrgb[0] + newrgb[1] + newrgb[2];
            for (var j=0; j<3; j++) {
                // when darkening, lowest color component can be is 60.
                newrgb[j] = (sum > 570) ?  newrgb[j] * 0.8 : newrgb[j] + 0.3 * (255 - newrgb[j]);
                newrgb[j] = parseInt(newrgb[j], 10);
            }
            ret.push('rgb('+newrgb[0]+','+newrgb[1]+','+newrgb[2]+')');
        }
        return ret;
    }

    function createGradientFill(color, point1, point2, ctx) {
        var g = ctx.createLinearGradient(point1[0], point1[1], point2[0], point2[1]);
        var i;
        var c = color;
        if (typeof(c) === "string" || typeof(c) === "undefined") {
            return c;
//                        c = [{"point": 0, "color": c}, {"point":.5, "color": c}, {"point": 1, "color": c}]
        }
        for (i = 0; i < c.length; i++) {

            g.addColorStop(c[i]["point"], c[i]["color"])
        }
        return g;
    }

    function getStart(sidx, didx, comp, plot, axis) {
        // check if sign change
        var seriesIndex = sidx,
            prevSeriesIndex = sidx - 1,
            start,
            prevVal,
            aidx = (axis === 'x') ? 0 : 1;

        // is this not the first series?
        if (seriesIndex > 0) {
            prevVal = plot.series[prevSeriesIndex]._plotData[didx][aidx];

            // is there a sign change
            if ((comp * prevVal) < 0) {
                start = getStart(prevSeriesIndex, didx, comp, plot, axis);
            }

            // no sign change.
            else {
                start = plot.series[prevSeriesIndex].gridData[didx][aidx];
            }

        }

        // if first series, return value at 0
        else {

            start = (aidx === 0) ? plot.series[seriesIndex]._xaxis.series_u2p(0) : plot.series[seriesIndex]._yaxis.series_u2p(0);
        }

        return start;
    }

    $.jqplot.GradientBarRenderer.prototype.draw = function (ctx, gridData, options, plot) {
        var i;
        // Ughhh, have to make a copy of options b/c it may be modified later.
        var opts = $.extend({}, options);
        var shadow = (opts.shadow != undefined) ? opts.shadow : this.shadow;
        var showLine = (opts.showLine != undefined) ? opts.showLine : this.showLine;
        var fill = (opts.fill != undefined) ? opts.fill : this.fill;
        var xaxis = this.xaxis;
        var yaxis = this.yaxis;
        var xp = this._xaxis.series_u2p;
        var yp = this._yaxis.series_u2p;
        var pointx, pointy;
        // clear out data colors.
        this._dataColors = [];
        this._barPoints = [];

        if (this.barWidth == null) {
            this.renderer.setBarWidth.call(this);
        }

        var temp = this._plotSeriesInfo = this.renderer.calcSeriesNumbers.call(this);
        var nvals = temp[0];
        var nseries = temp[1];
        var pos = temp[2];
        var points = [];

        if (this._stack) {
            this._barNudge = 0;
        }
        else {
            this._barNudge = (-Math.abs(nseries/2 - 0.5) + pos) * (this.barWidth + this.barPadding);
        }
        if (showLine) {
            var negativeColors = new $.jqplot.ColorGenerator(this.negativeSeriesColors);
            var positiveColors = new $.jqplot.ColorGenerator(this.seriesColors);
            var negativeColor = negativeColors.get(this.index);
            if (! this.useNegativeColors) {
                negativeColor = opts.fillStyle;
            }
            var positiveColor = opts.fillStyle;
            var base;
            var xstart;
            var ystart;

            if (this.barDirection == 'vertical') {
                for (var i=0; i<gridData.length; i++) {
                    if (!this._stack && this.data[i][1] == null) {
                        continue;
                    }
                    points = [];
                    base = gridData[i][0] + this._barNudge;

                    // stacked
                    if (this._stack && this._prevGridData.length) {
                        ystart = getStart(this.index, i, this._plotData[i][1], plot, 'y');
                    }

                    // not stacked
                    else {
                        if (this.fillToZero) {
                            ystart = this._yaxis.series_u2p(0);
                        }
                        else if (this.waterfall && i > 0 && i < this.gridData.length-1) {
                            ystart = this.gridData[i-1][1];
                        }
                        else if (this.waterfall && i == 0 && i < this.gridData.length-1) {
                            if (this._yaxis.min <= 0 && this._yaxis.max >= 0) {
                                ystart = this._yaxis.series_u2p(0);
                            }
                            else if (this._yaxis.min > 0) {
                                ystart = ctx.canvas.height;
                            }
                            else {
                                ystart = 0;
                            }
                        }
                        else if (this.waterfall && i == this.gridData.length - 1) {
                            if (this._yaxis.min <= 0 && this._yaxis.max >= 0) {
                                ystart = this._yaxis.series_u2p(0);
                            }
                            else if (this._yaxis.min > 0) {
                                ystart = ctx.canvas.height;
                            }
                            else {
                                ystart = 0;
                            }
                        }
                        else {
                            ystart = ctx.canvas.height;
                        }
                    }
                    if ((this.fillToZero && this._plotData[i][1] < 0) || (this.waterfall && this._data[i][1] < 0)) {
                        if (this.varyBarColor && !this._stack) {
                            if (this.useNegativeColors) {
                                opts.fillStyle = negativeColors.next();
                            }
                            else {
                                opts.fillStyle = positiveColors.next();
                            }
                        }
                        else {
                            opts.fillStyle = negativeColor;
                        }
                    }
                    else {
                        if (this.varyBarColor && !this._stack) {
                            opts.fillStyle = positiveColors.next();
                        }
                        else {
                            opts.fillStyle = positiveColor;
                        }
                    }

                    if (!this.fillToZero || this._plotData[i][1] >= 0) {
                        points.push([base-this.barWidth/2, ystart]);
                        points.push([base-this.barWidth/2, gridData[i][1]]);
                        points.push([base+this.barWidth/2, gridData[i][1]]);
                        points.push([base+this.barWidth/2, ystart]);
                        p1 = points[0];
                        p2 = points[3];

                    }
                    // for negative bars make sure points are always ordered clockwise
                    else {
                        points.push([base-this.barWidth/2, gridData[i][1]]);
                        points.push([base-this.barWidth/2, ystart]);
                        points.push([base+this.barWidth/2, ystart]);
                        points.push([base+this.barWidth/2, gridData[i][1]]);
                        p1 = points[0];
                        p2 = points[3];
                    }
                    if (opts.fillStyle)
                        opts.fillStyle = createGradientFill(opts.fillStyle, p1, p2, ctx);

                    this._barPoints.push(points);
                    // now draw the shadows if not stacked.
                    // for stacked plots, they are predrawn by drawShadow
                    if (shadow && !this._stack) {
                        var sopts = $.extend(true, {}, opts);
                        opts.offset = (opts.shadowOffset != undefined) ? opts.shadowOffset : this.shadowOffset;
                        // need to get rid of fillStyle on shadow.
                        delete sopts.fillStyle;
                        this.renderer.shadowRenderer.draw(ctx, points, sopts);
                    }

                    var clr = opts.fillStyle || this.color[0]["color"];
                    this._dataColors.push(this.color[0]["color"]);
                    opts.fillStyle = opts.fillStyle || createGradientFill(this.color, p1, p2, ctx);
                    this.renderer.shapeRenderer.draw(ctx, points, opts);
                }
            }

            else if (this.barDirection == 'horizontal'){
                for (var i=0; i<gridData.length; i++) {
                    if (!this._stack && this.data[i][0] == null) {
                        continue;
                    }
                    points = [];
                    base = gridData[i][1] - this._barNudge;
                    xstart;

                    if (this._stack && this._prevGridData.length) {
                        xstart = getStart(this.index, i, this._plotData[i][0], plot, 'x');
                    }
                    // not stacked
                    else {
                        if (this.fillToZero) {
                            xstart = this._xaxis.series_u2p(0);
                        }
                        else if (this.waterfall && i > 0 && i < this.gridData.length-1) {
                            xstart = this.gridData[i-1][0];
                        }
                        else if (this.waterfall && i == 0 && i < this.gridData.length-1) {
                            if (this._xaxis.min <= 0 && this._xaxis.max >= 0) {
                                xstart = this._xaxis.series_u2p(0);
                            }
                            else if (this._xaxis.min > 0) {
                                xstart = 0;
                            }
                            else {
                                xstart = 0;
                            }
                        }
                        else if (this.waterfall && i == this.gridData.length - 1) {
                            if (this._xaxis.min <= 0 && this._xaxis.max >= 0) {
                                xstart = this._xaxis.series_u2p(0);
                            }
                            else if (this._xaxis.min > 0) {
                                xstart = 0;
                            }
                            else {
                                xstart = ctx.canvas.width;
                            }
                        }
                        else {
                            xstart = 0;
                        }
                    }
                    if ((this.fillToZero && this._plotData[i][0] < 0) || (this.waterfall && this._data[i][0] < 0)) {
                        if (this.varyBarColor && !this._stack) {
                            if (this.useNegativeColors) {
                                opts.fillStyle = negativeColors.next();
                            }
                            else {
                                opts.fillStyle = positiveColors.next();
                            }
                        }
                        else {
                            opts.fillStyle = negativeColor;
                        }
                    }
                    else {
                        if (this.varyBarColor && !this._stack) {
                            opts.fillStyle = positiveColors.next();
                        }
                        else {
                            opts.fillStyle = positiveColor;
                        }
                    }

                    var p1, p2;

                    if (!this.fillToZero || this._plotData[i][0] >= 0) {
                        points.push([xstart, base + this.barWidth / 2]);
                        points.push([xstart, base - this.barWidth / 2]);
                        points.push([gridData[i][0], base - this.barWidth / 2]);
                        points.push([gridData[i][0], base + this.barWidth / 2]);
                        p1 = points[0];
                        p2 = points[1];
                    }
                    else {
                        points.push([gridData[i][0], base + this.barWidth / 2]);
                        points.push([gridData[i][0], base - this.barWidth / 2]);
                        points.push([xstart, base - this.barWidth / 2]);
                        points.push([xstart, base + this.barWidth / 2]);
                        p1 = points[0];
                        p2 = points[1];
                    }

                    if (opts.fillStyle) {
                        this._dataColors.push(opts.fillStyle[0]["color"]);
                        opts.fillStyle = createGradientFill(opts.fillStyle, p1, p2, ctx);
                    }

                    this._barPoints.push(points);
                    // now draw the shadows if not stacked.
                    // for stacked plots, they are predrawn by drawShadow
                    if (shadow && !this._stack) {
                        var sopts = $.extend(true, {}, opts);
                        delete sopts.fillStyle;
                        this.renderer.shadowRenderer.draw(ctx, points, sopts);
                    }
                    var clr = opts.fillStyle || this.color[0]["color"];
                    opts.fillStyle = opts.fillStyle || createGradientFill(this.color, p1, p2, ctx);
                    this.renderer.shapeRenderer.draw(ctx, points, opts);
                }
            }
        }

        if (this.highlightColors.length == 0) {
            this.highlightColors = $.jqplot.computeHighlightColors(this._dataColors);
        }

        else if (typeof(this.highlightColors) == 'string') {
            var temp = this.highlightColors;
            this.highlightColors = [];
            for (var i=0; i<this._dataColors.length; i++) {
                this.highlightColors.push(temp);
            }
        }

    };

})(jQuery);