package com.easyinsight.map
{
	import com.yahoo.maps.api.markers.Marker;
    
    import flash.display.Shape;
    import flash.events.MouseEvent;
    import flash.filters.DropShadowFilter;
    import flash.geom.Point;
    
    public class SimpleCustomMarker extends Marker
    {
        private var _dragEnabled:Boolean;
        private var _isDragging:Boolean;
        private var _graphic:Shape;
        
        public function SimpleCustomMarker()
        {
            super();
            init();
        }
        
        private function init():void
        {
            // draw the circle
            _graphic = new Shape();
            _graphic.graphics.lineStyle(1,0x333333);
            _graphic.graphics.beginFill(0xFF00A3,1);
            _graphic.graphics.drawCircle(0,0,7);
            _graphic.graphics.endFill();
            _graphic.filters = [new DropShadowFilter(0,90,0x000,0.7,5,8,2,2)]
            
            this.addChild(_graphic);
            
            // allow dragging.
            dragEnabled = true;
        }
        
        public function set dragEnabled(value:Boolean):void {
            _dragEnabled = value;
            if(_dragEnabled==true) {
                this.addEventListener(MouseEvent.MOUSE_DOWN, onMouseDown);
                this.addEventListener(MouseEvent.MOUSE_UP, onMouseUp);
            }else{
                this.removeEventListener(MouseEvent.MOUSE_DOWN, onMouseDown);
                this.removeEventListener(MouseEvent.MOUSE_UP, onMouseUp);
            }
        }
        
        public function get dragEnabled():Boolean {
            return _dragEnabled; 
        }
        
        private function onMouseDown(event:MouseEvent):void {
            this.addEventListener(MouseEvent.MOUSE_MOVE, onMouseMove, false, 0, true);
        }
        
        private function onMouseMove(event:MouseEvent):void {
            if(_isDragging==false) {
                this.startDrag();
            }
            _isDragging=true;
        }
        
        private function onMouseUp(event:MouseEvent):void {
            if(_isDragging==true) {
                this.stopDrag();
            }
            
            _isDragging=false;
            this.removeEventListener(MouseEvent.MOUSE_MOVE, onMouseMove);
            
            // get the new latlon point based on the markers x and y.
            var p:Point = new Point(this.x, this.y);
            this.latlon = this.getLocalPointToLatLon(p);
        }

    }
}