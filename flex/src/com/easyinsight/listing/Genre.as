package com.easyinsight.listing
{
	import mx.core.UIComponent;
	
	public class Genre
	{
		public var genreName:String;
		public var genreKey:String;
		public var component:UIComponent;
		public var icon:Class;
		
		public function Genre(genreName:String, genreKey:String, component:UIComponent, icon:Class)
		{
			this.genreName = genreName;
			this.genreKey = genreKey;
			this.component = component;
			this.icon = icon;
		}

	}
}