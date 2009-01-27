package com.easyinsight.listing
{
	import flash.events.Event;

	public class GenreChangedEvent extends Event
	{
		public static const GENRE_CHANGE:String = "genreChanged";
		public var genre:Genre;
		
		public function GenreChangedEvent(genre:Genre)
		{
			super(GENRE_CHANGE);
			this.genre = genre;
		}
		
	}
}