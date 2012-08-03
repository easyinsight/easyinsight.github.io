window.drawDiagram = function (j, selector, reportID) {
    var diagram = j;

    function createNode(node, key) {
        var n = document.createElement("div");

        $(n).addClass("node");
        n.style.top = node["y"] + "px";
        n.style.left = node["x"] + "px";

        var e = document.createElement("div");
        if (node["drillthrough"] != null) {
            $(e).addClass("drillthrough");
            $(e).bind("click", function (e) {
                drillThrough('reportID=' + reportID + "&drillthroughID=" + node["drillthrough"] + "&sourceField=" + key);
            })
        }

        var val = document.createElement("span");
        var img;
        if (node["image"] != null) {
            img = document.createElement("img");
            img.src = "/app/assets/icons/32x32/" + node["image"];
        } else {
            img = document.createElement("div");
            $(img).addClass("spacer");
        }
        $(val).html(node["value"]);

        var label = document.createElement("span");
        label.style.width = "100%";
        label.style.textAlign = "center";
        label.style.display = "block";
        $(label).html(node["name"]);

        $(n).append(e);
        $(n).append(label);
        $(e).append(img);

        if (node["type"] == "trend") {
            var trendBox = document.createElement("div");
            $(trendBox).append(val);
            var percentage = document.createElement("span");
            $(percentage).html(node["change"]);
            $(percentage).addClass("trend");
            var trendImage = document.createElement("img");
            trendImage.src = "/app/assets/icons/32x32/" + node["trendIcon"]
            $(percentage).append(trendImage);
            $(trendBox).append(document.createElement("br"));
            $(trendBox).append(percentage);

            $(e).append(trendBox);
        } else {
            $(e).append(val);
        }

        return n;
    }

    var Direction = {
        LEFT:"LEFT",
        RIGHT:"RIGHT",
        TOP:"TOP",
        BOTTOM:"BOTTOM"
    }

    var ORDER = [Direction.LEFT, Direction.TOP, Direction.RIGHT, Direction.BOTTOM]

    function stripPx(str) {
        return parseInt(str.substring(0, str.length - 2));
    }

    function getXY(node) {
        return {x:stripPx(node.style.left), y:stripPx(node.style.top) };
    }

    function pointPoint(x1, y1, x2, y2) {
        return function (x, y) {
            return ((y2 - y1) / (x2 - x1)) * (x - x1) - (y - y1);
        }
    }

    function bottomRightXY(node) {
        var xy = getXY(node);
        var n = $(node);
        return {x:xy.x + n.outerWidth(), y:xy.y + n.outerHeight() };
    }

    function centerXY(node) {
        var xy = getXY(node);
        var n = $(node)
        return {x:xy.x + n.outerWidth() / 2, y:xy.y + n.outerHeight() / 2 };
    }

    function upperRightXY(node) {
        var xy = getXY(node);
        return {x:xy.x + $(node).outerWidth(), y:xy.y };
    }

    function attachPointXY(node, direction) {
        var xy = getXY(node);
        var n = $(node);
        if (direction == Direction.LEFT) {
            var b = $("div", n).first();
            return {x:xy.x - 1, y:xy.y + b.outerHeight() / 2 }
        } else if (direction == Direction.RIGHT) {
            var b = $("div", n).first();
            return {x:xy.x + n.outerWidth() + 1, y:xy.y + b.outerHeight() / 2 }
        } else if (direction == Direction.TOP) {
            return {x:xy.x + n.outerWidth() / 2, y:xy.y - 1 }
        } else {
            return {x:xy.x + n.outerWidth() / 2, y:xy.y + n.outerHeight() + 1 }
        }
    }

    function drawArrow(canvas, point1, point2) {
        var curColor = canvas.strokeStyle;
        var curLineWidth = canvas.lineWidth;
        var curLineCap = canvas.lineCap;
        var rgb = Color.strToRGB(curColor);
        var hsv = Color.rgbToHsv(rgb[0], rgb[1], rgb[2]);
        var newHsv = [hsv[0], hsv[1] - .45, hsv[2] + .50];
        var newRgb = Color.hsvToRgb(newHsv[0], newHsv[1], newHsv[2]);
        var newColor = Color.rgbToStr(newRgb[0], newRgb[1], newRgb[2]);
        canvas.lineCap = "round";
        canvas.beginPath();
        canvas.lineWidth = curLineWidth + 3;
        canvas.strokeStyle = newColor;

        drawIndividualArrow(canvas, point1, point2);
        canvas.stroke();
        canvas.strokeStyle = curColor;
        canvas.lineWidth = curLineWidth;

        canvas.beginPath();
        drawIndividualArrow(canvas, point1, point2);
        canvas.stroke();
        canvas.lineCap = curLineCap;

    }

    function drawIndividualArrow(canvas, point1, point2) {
        var angle = Math.atan2(point2.y - point1.y, point2.x - point1.x);
        var arrowLength = 5;
        canvas.moveTo(point2.x, point2.y);
        canvas.lineTo(point2.x - arrowLength * (Math.cos(angle) - Math.sin(angle)), point2.y - arrowLength * (Math.cos(angle) + Math.sin(angle)));
        canvas.moveTo(point2.x, point2.y);
        canvas.lineTo(point2.x - arrowLength * (Math.cos(angle) + Math.sin(angle)), point2.y - arrowLength * (Math.sin(angle) - Math.cos(angle)));
    }

    function getDirection(fromNode, toNode) {
        var fromXY = getXY(fromNode);
        var fromCenter = centerXY(fromNode);
        var upperRight = upperRightXY(fromNode);
        var toCenter = centerXY(toNode);
        var inequality1 = pointPoint(fromXY.x, fromXY.y, fromCenter.x, fromCenter.y)(toCenter.x, toCenter.y);
        var inequality2 = pointPoint(fromCenter.x, fromCenter.y, upperRight.x, upperRight.y)(toCenter.x, toCenter.y);
        if (inequality1 > 0 && inequality2 > 0) {
            return Direction.TOP;
        } else if (inequality1 > 0 && inequality2 < 0) {
            return Direction.RIGHT;
        } else if (inequality1 < 0 && inequality2 < 0) {
            return Direction.BOTTOM;
        } else {
            return Direction.LEFT;
        }
    }

    function getMidPoints(fromCoord, toCoord, direction) {
        var point2, point3;
        if (direction == Direction.TOP || direction == Direction.BOTTOM) {
            var midPointY = (fromCoord.y + toCoord.y) / 2;
            point2 = {x:fromCoord.x, y:midPointY };
            point3 = {x:toCoord.x, y:midPointY };
        } else {
            var midPointX = (fromCoord.x + toCoord.x) / 2;
            point2 = {x:midPointX, y:fromCoord.y };
            point3 = {x:midPointX, y:toCoord.y };
        }

        return [point2, point3, toCoord];
    }

    (function () {
        if(Object.keys(diagram).length == 0) {
            $("#reportWell").hide();
            $("#reportWell")
        } else {

        }
        var index;
        var mapping = {};
        var i;
        var diagramElement = document.createElement("div");
        var canvas = document.createElement("canvas");
        selector.empty();
        selector.append(diagramElement);
        $(diagramElement).append(canvas);
        $(diagramElement).addClass("diagram");
        for (index in diagram["nodes"]) {
            mapping[index] = createNode(diagram["nodes"][index], index);
            $(diagramElement).append(mapping[index]);
            var bottomRight = bottomRightXY(mapping[index]);
            if (bottomRight.x > canvas.width) {
                canvas.width = bottomRight.x;
            }
            if (bottomRight.y > canvas.height) {
                canvas.height = bottomRight.y;
            }
        }

        var context = canvas.getContext("2d");

        for (i in diagram["links"]) {
            var link = diagram["links"][i];
            var fromNode = mapping[link["from"]];
            var toNode = mapping[link["to"]];
            var direction = getDirection(fromNode, toNode);

            var fromCoords = attachPointXY(fromNode, direction);

            var toCoords = attachPointXY(toNode, ORDER[(ORDER.indexOf(direction) + 2) % ORDER.length]);
            var points = getMidPoints(fromCoords, toCoords, direction);
            context.beginPath();
            context.moveTo(fromCoords.x, fromCoords.y);
            context.strokeStyle = "#1F1F45";
            context.lineWidth = 3;
            context.lineCap = "square"


            var point;
            for (point in points) {
                context.lineTo(points[point].x, points[point].y);
            }
            context.stroke();
            drawArrow(context, points[1], points[2]);
        }
        afterRefresh();

    })();
}