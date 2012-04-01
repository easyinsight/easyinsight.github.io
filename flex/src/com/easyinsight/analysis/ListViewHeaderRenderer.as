package com.easyinsight.analysis
{
    import mx.controls.AdvancedDataGrid;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridHeaderRenderer;

	public class ListViewHeaderRenderer extends AdvancedDataGridHeaderRenderer
	{

        private var _eiColor:uint;
		
		[Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;
				
		public function ListViewHeaderRenderer()
		{
			super();
            //setStyle("fontSize", 13);
            setStyle("fontFamily", "Tahoma");
		}

        public function set eiColor(value:uint):void {
            _eiColor = value;
        }

        /*override protected function createInFontContext(classObj:Class):Object {
            var text:Object = super.createInFontContext(classObj);
            trace("argh");
            return text;
        }*/
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
            if (data && parent) {
                var labelColor:uint;
                if (!enabled)
                    labelColor = getStyle("disabledColor");
                else if (AdvancedDataGrid(listData.owner).isItemHighlighted(listData.uid))
                    labelColor = getStyle("textRollOverColor");
                else if (AdvancedDataGrid(listData.owner).isItemSelected(listData.uid))
                    labelColor = getStyle("textSelectedColor");
                else
                    labelColor = _eiColor;

               label.setColor(labelColor);
            }
		}
	}
}