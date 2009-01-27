package com.easyinsight.images
{
	public class ImageFactory
	{		
		
		[Bindable]
        [Embed(source="../skin/osx/appleDesktop.jpg")]
        private static var desktop:Class;
        
		public function ImageFactory()
		{
		}

		public static function getBackgroundImage():Class {
			return desktop;
		}
	}
}