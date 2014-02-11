/**
 * jqPlot
 * Pure JavaScript plotting plugin using jQuery
 *
 * Version: 1.0.8
 * Revision: 1250
 *
 * Copyright (c) 2009-2013 Chris Leonello
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
    /**
     *  class: $.jqplot.measureAxisRenderer
     *  A plugin for jqPlot to render a category style axis, with equal pixel spacing between y data values of a series.
     *
     *  To use this renderer, include the plugin in your source
     *  > <script type="text/javascript" language="javascript" src="plugins/jqplot.categoryAxisRenderer.js"></script>
     *
     *  and supply the appropriate options to your plot
     *
     *  > {axes:{xaxis:{renderer:$.jqplot.CategoryAxisRenderer}}}
     **/
    $.jqplot.MeasureAxisRenderer = function (options) {
        $.jqplot.LinearAxisRenderer.call(this);
        // prop: sortMergedLabels
        // True to sort tick labels when labels are created by merging
        // x axis values from multiple series.  That is, say you have
        // two series like:
        // > line1 = [[2006, 4],            [2008, 9], [2009, 16]];
        // > line2 = [[2006, 3], [2007, 7], [2008, 6]];
        // If no label array is specified, tick labels will be collected
        // from the x values of the series.  With sortMergedLabels
        // set to true, tick labels will be:
        // > [2006, 2007, 2008, 2009]
        // With sortMergedLabels set to false, tick labels will be:
        // > [2006, 2008, 2009, 2007]
        //
        // Note, this property is specified on the renderOptions for the 
        // axes when creating a plot:
        // > axes:{xaxis:{renderer:$.jqplot.CategoryAxisRenderer, rendererOptions:{sortMergedLabels:true}}}
        this.sortMergedLabels = false;
    };

    $.jqplot.MeasureAxisRenderer.prototype = new $.jqplot.LinearAxisRenderer();
    $.jqplot.MeasureAxisRenderer.prototype.constructor = $.jqplot.MeasureAxisRenderer;

    // called with scope of axis
    $.jqplot.MeasureAxisRenderer.prototype.pack = function (pos, offsets) {
        pos = pos || {};
        offsets = offsets || this._offsets;

        var ticks = this._ticks;
        var max = this.max;
        var min = this.min;
        var offmax = offsets.max;
        var offmin = offsets.min;
        var lshow = (this._label == null) ? false : this._label.show;

        for (var p in pos) {
            this._elem.css(p, pos[p]);
        }

        this._offsets = offsets;
        // pixellength will be + for x axes and - for y axes becasue pixels always measured from top left.
        var pixellength = offmax - offmin;
        var unitlength = max - min;

        // point to unit and unit to point conversions references to Plot DOM element top left corner.
        if (this.breakPoints) {
            unitlength = unitlength - this.breakPoints[1] + this.breakPoints[0];

            this.p2u = function (p) {
                return (p - offmin) * unitlength / pixellength + min;
            };

            this.u2p = function (u) {
                if (u > this.breakPoints[0] && u < this.breakPoints[1]) {
                    u = this.breakPoints[0];
                }
                if (u <= this.breakPoints[0]) {
                    return (u - min) * pixellength / unitlength + offmin;
                }
                else {
                    return (u - this.breakPoints[1] + this.breakPoints[0] - min) * pixellength / unitlength + offmin;
                }
            };

            if (this.name.charAt(0) == 'x') {
                this.series_u2p = function (u) {
                    if (u > this.breakPoints[0] && u < this.breakPoints[1]) {
                        u = this.breakPoints[0];
                    }
                    if (u <= this.breakPoints[0]) {
                        return (u - min) * pixellength / unitlength;
                    }
                    else {
                        return (u - this.breakPoints[1] + this.breakPoints[0] - min) * pixellength / unitlength;
                    }
                };
                this.series_p2u = function (p) {
                    return p * unitlength / pixellength + min;
                };
            }

            else {
                this.series_u2p = function (u) {
                    if (u > this.breakPoints[0] && u < this.breakPoints[1]) {
                        u = this.breakPoints[0];
                    }
                    if (u >= this.breakPoints[1]) {
                        return (u - max) * pixellength / unitlength;
                    }
                    else {
                        return (u + this.breakPoints[1] - this.breakPoints[0] - max) * pixellength / unitlength;
                    }
                };
                this.series_p2u = function (p) {
                    return p * unitlength / pixellength + max;
                };
            }
        }
        else {
            this.p2u = function (p) {
                return (p - offmin) * unitlength / pixellength + min;
            };

            this.u2p = function (u) {
                return (u - min) * pixellength / unitlength + offmin;
            };

            if (this.name == 'xaxis' || this.name == 'x2axis') {
                this.series_u2p = function (u) {
                    return (u - min) * pixellength / unitlength;
                };
                this.series_p2u = function (p) {
                    return p * unitlength / pixellength + min;
                };
            }

            else {
                this.series_u2p = function (u) {
                    return (u - max) * pixellength / unitlength;
                };
                this.series_p2u = function (p) {
                    return p * unitlength / pixellength + max;
                };
            }
        }

        if (this.show) {
            if (this.name == 'xaxis' || this.name == 'x2axis') {
                for (var i = 0; i < ticks.length; i++) {
                    var t = ticks[i];
                    if (t.show && t.showLabel) {
                        var shim;

                        if (t.constructor == $.jqplot.CanvasAxisTickRenderer && t.angle) {
                            // will need to adjust auto positioning based on which axis this is.
                            var temp = (this.name == 'xaxis') ? 1 : -1;
                            switch (t.labelPosition) {
                                case 'auto':
                                    // position at end
                                    if (temp * t.angle < 0) {
                                        shim = -t.getWidth() + t._textRenderer.height * Math.sin(-t._textRenderer.angle) / 2;
                                    }
                                    // position at start
                                    else {
                                        shim = -t._textRenderer.height * Math.sin(t._textRenderer.angle) / 2;
                                    }
                                    break;
                                case 'end':
                                    shim = -t.getWidth() + t._textRenderer.height * Math.sin(-t._textRenderer.angle) / 2;
                                    break;
                                case 'start':
                                    shim = -t._textRenderer.height * Math.sin(t._textRenderer.angle) / 2;
                                    break;
                                case 'middle':
                                    shim = -t.getWidth() / 2 + t._textRenderer.height * Math.sin(-t._textRenderer.angle) / 2;
                                    break;
                                default:
                                    shim = -t.getWidth() / 2 + t._textRenderer.height * Math.sin(-t._textRenderer.angle) / 2;
                                    break;
                            }
                        }
                        else {
                            shim = -t.getWidth() / 2;
                        }
                        var val = this.u2p(t.value) + shim + 'px';
                        t._elem.css('left', val);
                        t.pack();
                    }
                }
                if (lshow) {
                    var w = this._label._elem.outerWidth(true);
                    this._label._elem.css('left', offmin + pixellength / 2 - w / 2 + 'px');
                    if (this.name == 'xaxis') {
                        this._label._elem.css('bottom', '0px');
                    }
                    else {
                        this._label._elem.css('top', '0px');
                    }
                    this._label.pack();
                }
            }
            else {
                for (var i = 0; i < ticks.length; i++) {
                    var t = ticks[i];
                    if (t.show && t.showLabel) {
                        var shim;
                        if (t.constructor == $.jqplot.CanvasAxisTickRenderer && t.angle) {
                            // will need to adjust auto positioning based on which axis this is.
                            var temp = (this.name == 'yaxis') ? 1 : -1;
                            switch (t.labelPosition) {
                                case 'auto':
                                // position at end
                                case 'end':
                                    if (temp * t.angle < 0) {
                                        shim = -t._textRenderer.height * Math.cos(-t._textRenderer.angle) / 2;
                                    }
                                    else {
                                        shim = -t.getHeight() + t._textRenderer.height * Math.cos(t._textRenderer.angle) / 2;
                                    }
                                    break;
                                case 'start':
                                    if (t.angle > 0) {
                                        shim = -t._textRenderer.height * Math.cos(-t._textRenderer.angle) / 2;
                                    }
                                    else {
                                        shim = -t.getHeight() + t._textRenderer.height * Math.cos(t._textRenderer.angle) / 2;
                                    }
                                    break;
                                case 'middle':
                                    // if (t.angle > 0) {
                                    //     shim = -t.getHeight()/2 + t._textRenderer.height * Math.sin(-t._textRenderer.angle) / 2;
                                    // }
                                    // else {
                                    //     shim = -t.getHeight()/2 - t._textRenderer.height * Math.sin(t._textRenderer.angle) / 2;
                                    // }
                                    shim = -t.getHeight() / 2;
                                    break;
                                default:
                                    shim = -t.getHeight() / 2;
                                    break;
                            }
                        }
                        else {
                            shim = -t.getHeight() / 2;
                        }

                        var val = this.u2p(t.value) + shim + 'px';
                        t._elem.css('top', val);
                        t.pack();
                    }
                }

                if (lshow) {
                    var h = this._label._elem.outerHeight(true);
                    this._label._elem.css('top', offmax - pixellength / 2 - h / 2 + 'px');
                    if (this.name == 'yaxis') {
                        this._label._elem.css('left', '0px');
                    }
                    else {
                        this._label._elem.css('right', '0px');
                    }
                    this._label.pack();
                }
            }

            var prev = null;
            var overlap = false;
            for (var i = 0; i < ticks.length; i++) {

                var t = ticks[i];
                var curElem = t._elem;
                if (typeof(curElem) != "undefined") {
                    if (prev != null) {
                        var ff = $(prev._elem);
                        var yy = $(curElem);
                        if (ff.offset().left + ff.outerWidth() > yy.offset().left) {
                            overlap = true;
                        }
                    }
                    prev = t;
                }
            }
            if (overlap) {
                var c = this._elem;
                var h = (parseInt(c.css("height").replace(/px/, "")) + 8) + "px";
                c.css("height", h);
                var j = 0;
                for (var i = 0; i < ticks.length; i++) {
                    var t = ticks[i];
                    if (t.constructor == $.jqplot.AxisTickRenderer && typeof(t._elem) != "undefined") {
                        if (j % 2 > 0) {
                            var y = $(t._elem);
                            var fs = y.css("font-size");
                            var fontSize = parseInt(fs.replace(/px/, ""));
                            y.css('top', (fontSize + 4) + "px");
                        }
                        j = j + 1;
                    }


                }
            }
        }

        ticks = null;
    };


})(jQuery);
