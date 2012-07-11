package com.anotherflexdev.diagrammer {
import flash.geom.Point;

import mx.controls.Alert;

[Style(name="templateBottomLineColor", type="uint", format="Color")]
	[Style(name="tempalteLineColor", type="uint", format="Color")]
	[Style(name="bottomLineColor", type="uint", format="Color")]
	[Style(name="lineColor", type="uint", format="Color")]
	[Style(name="lineThickness", type="Number")]
	public class LinkDouble90Deg extends Link {

        override protected function getDrawDirectionFromPoints(fromX:Number, fromY:Number, fromW:Number, fromH:Number, toX:Number, toY:Number, toW:Number, toH:Number):String {
            var drawDirection:String;
            var fromCenter:Point = new Point(fromX + fromW/2.0, fromY + fromH / 2.0)
            var toCenter:Point = new Point(toX + toW / 2.0, toY + toH / 2.0)

            var point_point = function(x1, y1, x2, y2) {
                return function(x, y):Number {
                    return ((y2 - y1) / (x2 - x1)) * (x - x1) - (y - y1)
                }
            }

            var leftDiagonal:Number = point_point(fromCenter.x,  fromCenter.y, fromX,  fromY)(toCenter.x,  toCenter.y)
            var rightDiagonal:Number = point_point(fromCenter.x,  fromCenter.y,  fromX + fromW,  fromY)(toCenter.x,  toCenter.y)

            if(leftDiagonal < 0 && rightDiagonal < 0)
                drawDirection = "BOTTOM";
            else if(leftDiagonal > 0 && rightDiagonal < 0)
                drawDirection = "RIGHT"
            else if(leftDiagonal < 0 && rightDiagonal > 0)
                drawDirection = "LEFT"
            else
                drawDirection = "TOP"

            return drawDirection;
        }
		
		override protected function drawLine(fromNodeCenterPoint:Point, toNodeCenterPoint:Point, bottomColor:uint, topColor:uint):void {
			var point1:Point = null;
			var point2:Point = null;
            var point3:Point = null;
            var point4:Point = null;

            var direction:String = getDrawDirectionFromPoints(fromNode.x, fromNode.y, fromNode.width, fromNode.height, toNode == null ? toNodeCenterPoint.x: toNode.x, toNode == null ? toNodeCenterPoint.y : toNode.y, toNode == null ? 10 : toNode.width, toNode == null ? 10 : toNode.height);
			var directions:Array = ["LEFT", "TOP", "RIGHT", "BOTTOM"]
			if(this.toNode == null) {
				point1 = fromNodeCenterPoint;
				point4 = toNodeCenterPoint;
			} else {
                point1 = this.getRealBoundary(this.fromNode,  direction);
                point4 = this.getRealBoundary(this.toNode,  directions[(directions.indexOf(direction) + 2) % 4])

			}

            this.fromPoint = point1;
            this.toPoint = point4;

			
			this.graphics.clear();

            if(direction == "LEFT" || direction == "RIGHT") { // left/right midpoint, then top to bottom, then left-right again
                point2 = new Point(((point1.x - point4.x) / 2) + point4.x, point1.y)
                point3 = new Point(point2.x,  point4.y)
            } else {
                point2 = new Point(point1.x, ((point1.y - point4.y) / 2) + point4.y)
                point3 = new Point(point4.x, point2.y)
            }

			this.graphics.lineStyle(this.getStyle("lineThickness")+2, bottomColor, 0.70);
			this.graphics.moveTo(point1.x, point1.y);
            this.graphics.lineTo(point2.x, point2.y);
            this.graphics.lineTo(point3.x, point3.y);
            this.graphics.lineTo(point4.x, point4.y);


            drawArrow(point3.x, point3.y, point4.x, point4.y, bottomColor, topColor);
		}

	  	override protected function getBoundary(fromCenterPoint:Point, toCenterPoint:Point, node:BaseNode):Point{
			if(node != null) {

				
				var drawDirection:String = null;
				if(this.toNode == node) {
					drawDirection = getDrawDirection(this.fromNode, this.toNode);
				} else {
					drawDirection = getDrawDirection(this.toNode, this.fromNode);
				}

                if(drawDirection.indexOf("LEFT") == 0) {
                    return new Point(fromCenterPoint.x + node.width / 2.0, fromCenterPoint.y);
                } else if(drawDirection.indexOf("RIGHT") == 0) {
                    return new Point(fromCenterPoint.x - node.width / 2.0, fromCenterPoint.y);
                } else if(drawDirection.indexOf("TOP") == 0) {
                    return new Point(fromCenterPoint.x,  fromCenterPoint.y - node.height / 2.0);
                } else if(drawDirection.indexOf("BOTTOM") == 0) {
                    return new Point(fromCenterPoint.x,  fromCenterPoint.y + node.height / 2.0);
                }

	  	    }
            return new Point(fromCenterPoint.x,  fromCenterPoint.y)
          }

        private function getRealBoundary(node:BaseNode, direction:String):Point {
            var p:Point = new Point();
            if(direction == "LEFT" || direction == "RIGHT") {
                p.y = node.y + node.diagramHeight / 2.0;
                if(direction == "LEFT")
                    p.x = node.x;
                else
                    p.x = node.x + node.width;
            } else {
                p.x = node.x + node.width / 2.0;
                if(direction == "TOP")
                    p.y = node.y;
                else
                    p.y = node.y + node.height;
            }

            return p;
        }


    }
}